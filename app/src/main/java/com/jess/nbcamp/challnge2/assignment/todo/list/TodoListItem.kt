package com.jess.nbcamp.challnge2.assignment.todo.list

sealed interface TodoListItem {

    val id: Long

    data class Item(
        override val id: Long,
        val title: String?,
        val content: String?,
        val isBookmark: Boolean = false,
    ) : TodoListItem
}