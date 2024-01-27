package com.jess.nbcamp.challnge2.assignment.todo.list

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jess.nbcamp.challnge2.assignment.todo.TodoContentType
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel
import com.jess.nbcamp.challnge2.assignment.todo.list.adapter.TodoListAdapter
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentActivity
import com.jess.nbcamp.challnge2.databinding.TodoListFragmentBinding

class TodoListFragment : Fragment() {

    companion object {
        fun newInstance() = TodoListFragment()
    }

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoListViewModel by viewModels()

    private var _adapter: TodoListAdapter? = null
    private val adapter get() = _adapter

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

    private fun initView() {
        setAdapter()
    }

    private fun initViewModel() = with(viewModel) {
        uiState.observe(viewLifecycleOwner) {
            adapter?.submitList(it.list)
        }
    }

    private fun setAdapter() {
        _adapter = TodoListAdapter { view, _, item ->
            when (view) {
                is SwitchCompat -> item.bookmark
                else -> {
                    val intent = TodoContentActivity.newIntent(
                        context = requireContext(),
                        entryType = TodoContentType.UPDATE,
                        todoModel = item
                    )
                    updateToLauncher.launch(intent)
                }
            }
        }
        binding.todoList.adapter = adapter
        binding.todoList.layoutManager = LinearLayoutManager(requireContext())
    }

    private val updateToLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                activityResult(result)
            }
        }

    private fun activityResult(result: ActivityResult) {
        val entryType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result.data?.getParcelableExtra(
                TodoContentActivity.EXTRA_TODO_TYPE,
                TodoContentType::class.java
            )
        } else {
            result.data?.getParcelableExtra(
                TodoContentActivity.EXTRA_TODO_TYPE
            )
        }
        val model = getTodoModeResult(result)

        when (entryType){
            TodoContentType.UPDATE -> model?.let { updateToItem(it) }
            TodoContentType.DELETE -> model?.let { deleteToItem(it) }
            else -> model?.let { addTodoItem(it) }
        }
    }
    private fun getTodoModeResult(result: ActivityResult): TodoModel? {
        val todo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result.data?.getParcelableExtra(
                TodoContentActivity.EXTRA_TODO_MODEL,
                TodoModel::class.java
            )
        } else {
            result.data?.getParcelableExtra(
                TodoContentActivity.EXTRA_TODO_MODEL
            )
        }
        return todo
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun addTodoItem(model: TodoModel?) {
        viewModel.addTodoItem(model)
    }

    private fun updateToItem(model: TodoModel) {
        viewModel.updateToItem(model)
    }

    private fun deleteToItem(model: TodoModel) {
        viewModel.deleteToItem(model)
    }
}