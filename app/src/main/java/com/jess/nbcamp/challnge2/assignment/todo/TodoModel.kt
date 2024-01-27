package com.jess.nbcamp.challnge2.assignment.todo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class TodoModel(
    val title: String? = null,
    val content: String? = null,
    val bookmark: Boolean = false,
    val key: String = UUID.randomUUID().toString()
) : Parcelable
