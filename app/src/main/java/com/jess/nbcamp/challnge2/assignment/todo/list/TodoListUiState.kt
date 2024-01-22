package com.jess.nbcamp.challnge2.assignment.todo.list

import com.jess.nbcamp.challnge2.assignment.todo.TodoModel

data class TodoListUiState(
    val list: List<TodoModel>
) {
    companion object {

        fun init() = TodoListUiState(
            list = emptyList()
        )
    }
}