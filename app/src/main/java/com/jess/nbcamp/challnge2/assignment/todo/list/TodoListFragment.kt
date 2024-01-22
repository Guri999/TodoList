package com.jess.nbcamp.challnge2.assignment.todo.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel
import com.jess.nbcamp.challnge2.databinding.TodoListFragmentBinding

class TodoListFragment : Fragment() {

    companion object {
        fun newInstance() = TodoListFragment()
    }

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoListViewModel by viewModels()

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

    }

    private fun initViewModel() = with(viewModel) {
        uiState.observe(viewLifecycleOwner) {
            // it.list
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun addTodoItem(model: TodoModel?) {
        viewModel.addTodoItem(model)
    }
}