package com.jess.nbcamp.challnge2.assignment.main

import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem

data class BookmarkEvent(
    val entryType: BookmarkEventState,
    val item: TodoListItem
)
sealed interface BookmarkEventState {
    object Create : BookmarkEventState
    object Update : BookmarkEventState
    object Delete : BookmarkEventState
}