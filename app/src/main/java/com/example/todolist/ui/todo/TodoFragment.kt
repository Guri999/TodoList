package com.example.todolist.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.TodoList
import com.example.todolist.databinding.FragmentTodoBinding


class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private var _adapter: TodoAdapter? = null
    private val adapter get() = _adapter

    private var _viewModel: TodoViewModel? = null
    private val viewModel get() = _viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        _adapter = TodoAdapter(TodoList.totalTodo)
        _viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        viewModel?.setList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.setList()
    }

    private fun initView() {
        setList()
    }

    private fun setList() {
        binding.rcTodoList.adapter = adapter
        binding.rcTodoList.layoutManager = LinearLayoutManager(context)
        viewModel?.todoList?.observe(viewLifecycleOwner) {
            adapter?.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
        _viewModel = null
    }
}