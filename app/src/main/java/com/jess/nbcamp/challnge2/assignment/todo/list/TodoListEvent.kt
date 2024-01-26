package com.jess.nbcamp.challnge2.assignment.todo.list

import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity

sealed interface TodoListEvent {

    data class OpenContent(
        val position: Int,
        val item: TodoEntity
    ) : TodoListEvent
}