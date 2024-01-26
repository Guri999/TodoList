package com.jess.nbcamp.challnge2.assignment.todo.list

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentActivity
import com.jess.nbcamp.challnge2.databinding.TodoListFragmentBinding

class TodoListFragment : Fragment() {

    companion object {
        fun newInstance() = TodoListFragment()
    }

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoListViewModel by viewModels()

    private val updateTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }

    private val listAdapter: TodoListAdapter by lazy {
        TodoListAdapter(
            onClickItem = { position, item ->
                viewModel.onClickItem(
                    position,
                    item as TodoListItem.Item
                )
            },
            onBookmarkChecked = { position, item ->

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
    }

    fun addTodoItem(model: TodoEntity?) {
        viewModel.addTodoItem(model)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}