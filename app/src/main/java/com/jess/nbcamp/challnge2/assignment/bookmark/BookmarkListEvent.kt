package com.jess.nbcamp.challnge2.assignment.bookmark

import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity

sealed interface BookmarkListEvent {

    data class OpenContent(
        val position: Int,
        val item: TodoEntity
    ) : BookmarkListEvent
}