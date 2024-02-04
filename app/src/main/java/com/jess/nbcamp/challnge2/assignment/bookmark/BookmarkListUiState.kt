package com.jess.nbcamp.challnge2.assignment.bookmark

import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem

data class BookmarkListUiState(
    val list: List<TodoListItem>
) {
    companion object {

        fun init() = BookmarkListUiState(
            list = emptyList()
        )
    }
}