package com.jess.nbcamp.challnge2.assignment.bookmark

enum class BookmarkListViewType {
    ITEM,
    UNKNOWN
    ;

    companion object {
        fun from(ordinal: Int): BookmarkListViewType = BookmarkListViewType.values().find {
            it.ordinal == ordinal
        } ?: UNKNOWN
    }
}