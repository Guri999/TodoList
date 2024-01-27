package com.jess.nbcamp.challnge2.assignment.bookmark

import com.jess.nbcamp.challnge2.assignment.todo.TodoModel

data class BookmarkListUiState (
    val list: List<TodoModel>
) {
    companion object {

        fun init() = BookmarkListUiState(
            list = emptyList()
        )
    }
}