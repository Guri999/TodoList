package com.jess.nbcamp.challnge2.assignment.todo.content

data class TodoContentUiState(
    val title: String?,
    val content: String?
) {
    companion object {

        fun init() = TodoContentUiState(
            title = null,
            content = null
        )
    }
}