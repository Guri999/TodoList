package com.jess.nbcamp.challnge2.assignment.todo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoEntity(
    val title: String?,
    val content: String?
) : Parcelable
