package com.jess.nbcamp.challnge2.assignment.todo.create

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoCreateModel(
    val title: String?,
    val content: String?,
) : Parcelable
