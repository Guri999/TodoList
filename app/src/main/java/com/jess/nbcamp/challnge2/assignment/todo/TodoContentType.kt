package com.jess.nbcamp.challnge2.assignment.todo


enum class TodoContentType {
    CREATE, UPDATE, DELETE;

    companion object {

        fun getEntryType(ordinal: Int?): TodoContentType =
            TodoContentType.values().firstOrNull {
                it.ordinal == ordinal
            } ?: CREATE
    }
}