package com.jess.nbcamp.challnge2.assignment.todo.list

sealed interface TodoListItem {

    data class Item(
        val id: String?,
        val title: String?,
        val content: String?,
        val isBookmark: Boolean = false,
    ) : TodoListItem
}