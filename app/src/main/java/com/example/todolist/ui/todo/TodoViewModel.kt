package com.example.todolist.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.Todo
import com.example.todolist.TodoList

class TodoViewModel: ViewModel() {
    private val _todoList: MutableLiveData<List<Todo>> = MutableLiveData()
    val  todoList: LiveData<List<Todo>> get() = _todoList

    fun setList() {
        _todoList.value = TodoList.totalTodo
    }
}