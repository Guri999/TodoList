package com.jess.nbcamp.challnge2.assignment.todo.content

sealed interface TodoContentEvent {
    data class Create(
        val title: String,
        val content: String
    ) : TodoContentEvent
}