package com.jess.nbcamp.challnge2.assignment.todo.list

data class TodoListUiState(
    val list: List<TodoListItem>
) {
    companion object {

        fun init() = TodoListUiState(
            list = emptyList()
        )
    }
}