package com.example.todolist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val title: String,
    val info: String
): Parcelable

object TodoList{
    val totalTodo = mutableListOf<Todo>(Todo("result 0","description"))
}