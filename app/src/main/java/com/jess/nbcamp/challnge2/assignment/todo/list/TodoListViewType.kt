package com.jess.nbcamp.challnge2.assignment.todo.list

enum class TodoListViewType {
    ITEM,
    UNKNOWN
    ;

    companion object {
        fun from(ordinal: Int): TodoListViewType = TodoListViewType.values().find {
            it.ordinal == ordinal
        } ?: UNKNOWN
    }
}