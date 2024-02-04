package com.jess.nbcamp.challnge2.assignment.todo.list

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.IntentCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jess.nbcamp.challnge2.assignment.main.TodoMainViewModel
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentActivity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_ENTITY
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_ENTRY_TYPE
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentEntryType
import com.jess.nbcamp.challnge2.databinding.TodoListFragmentBinding

class TodoListFragment : Fragment() {

    companion object {
        fun newInstance() = TodoListFragment()
    }

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoListViewModel by viewModels()

    private val sharedViewModel: TodoMainViewModel by activityViewModels()

    private val updateTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // Entry Type
                val entryType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra(
                        EXTRA_TODO_ENTRY_TYPE,
                        TodoContentEntryType::class.java
                    )
                } else {
                    result.data?.getSerializableExtra(EXTRA_TODO_ENTRY_TYPE) as TodoContentEntryType
                }

                // Entity
                val entity = IntentCompat.getParcelableExtra(
                    result.data ?: Intent(),
                    EXTRA_TODO_ENTITY,
                    TodoEntity::class.java
                )

                viewModel.updateTodoItem(
                    entryType,
                    entity
                )
            }
        }

    private val listAdapter: TodoListAdapter by lazy {
        TodoListAdapter(
            onClickItem = { position, item ->
                viewModel.onClickItem(
                    position,
                    item
                )
            },
            onBookmarkChecked = { position, item ->
                when (item) {
                    is TodoListItem.Item -> {
                        if (item.isBookmark) {
                            sharedViewModel.createBookmark(
                                item
                            )
                        } else {
                            sharedViewModel.deleteBookmark(
                                item
                            )
                        }
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        list.adapter = listAdapter
    }

    private fun initViewModel() = with(viewModel) {
        uiState.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.list)
        }

        event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is TodoListEvent.OpenContent -> updateTodoLauncher.launch(
                    TodoContentActivity.newIntentUpdate(
                        requireContext(),
                        event.position,
                        event.item
                    )
                )
            }
        }

        updateItem.observe(viewLifecycleOwner) { (event, item) ->
            when (event) {
                TodoContentEntryType.UPDATE -> sharedViewModel.updateBookmark(item)
                TodoContentEntryType.DELETE -> sharedViewModel.deleteBookmark(item)
                TodoContentEntryType.CREATE -> null
            }
        }

        sharedViewModel.bookmarkEvent.observe(viewLifecycleOwner) { event ->
            setBookmarkItem(event.entryType, event.item)
        }
    }

    fun addTodoItem(model: TodoEntity?) {
        viewModel.addTodoItem(model)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}