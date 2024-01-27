package com.jess.nbcamp.challnge2.assignment.todo.content

import com.jess.nbcamp.challnge2.assignment.todo.TodoModel

data class ContentUiState(
    val todoModel: TodoModel,
    val btnVisible: Boolean = false
)
