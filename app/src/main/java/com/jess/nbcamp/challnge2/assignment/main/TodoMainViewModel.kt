package com.jess.nbcamp.challnge2.assignment.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.nbcamp.challnge2.assignment.bookmark.BookmarkListUiState
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem

class TodoMainViewModel : ViewModel() {

    private val _bookmarkEvent: MutableLiveData<BookmarkEvent> = MutableLiveData() //SingleLiveEvent
    val bookmarkEvent: LiveData<BookmarkEvent> get() = _bookmarkEvent

    fun createBookmark(item: TodoListItem) {
        _bookmarkEvent.value = BookmarkEvent(BookmarkEventState.Create, item)
    }

    fun updateBookmark(item: TodoListItem) {
        _bookmarkEvent.value = BookmarkEvent(BookmarkEventState.Update, item)
    }

    fun deleteBookmark(item: TodoListItem) {
        _bookmarkEvent.value = BookmarkEvent(BookmarkEventState.Delete, item)
    }

}