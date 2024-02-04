package com.jess.nbcamp.challnge2.assignment.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.camp.util.SingleLiveEvent
import com.jess.nbcamp.challnge2.assignment.main.BookmarkEventState
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentEntryType
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem

class BookmarkListViewModel: ViewModel() {

    private val _uiState: MutableLiveData<BookmarkListUiState> =
        MutableLiveData(BookmarkListUiState.init())
    val uiState: LiveData<BookmarkListUiState> get() = _uiState

    private val _event: SingleLiveEvent<BookmarkListEvent> = SingleLiveEvent()
    val event: LiveData<BookmarkListEvent> get() = _event

    private fun createTodoItem(entity: TodoEntity, isBookmark: Boolean = true): TodoListItem =
        TodoListItem.Item(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            isBookmark = isBookmark
        )
    fun updateTodoItem(
        entryType: TodoContentEntryType?,
        entity: TodoEntity?
    ) {
        if (entity == null) {
            return
        }

        val mutableList = uiState.value?.list.orEmpty().toMutableList()

        when (entryType) {
            TodoContentEntryType.UPDATE -> {
                val position = mutableList.indexOfFirst {
                    when (it) {
                        is TodoListItem.Item -> {
                            it.id == entity.id
                        }
                    }
                }

                val isBookmark = (mutableList[position] as TodoListItem.Item).isBookmark

                uiState.value?.copy(
                    list = mutableList.also { list ->
                        list[position] = createTodoItem(
                            entity,
                            isBookmark
                        )
                    }
                )
            }

            TodoContentEntryType.DELETE -> {
                uiState.value?.copy(
                    list = mutableList.apply {
                        removeIf {
                            when (it) {
                                is TodoListItem.Item -> {
                                    it.id == entity.id
                                }
                            }
                        }
                    }
                )
            }

            else -> null
        }?.also { state ->
            _uiState.value = state
        }
    }

    fun onClickItem(
        position: Int,
        item: TodoListItem
    ) {
        _event.value = when (item) {
            is TodoListItem.Item -> BookmarkListEvent.OpenContent(
                position,
                TodoEntity(
                    id = item.id,
                    title = item.title,
                    content = item.content
                )
            )
        }
    }
    fun setBookmarkItem(event: BookmarkEventState, item: TodoListItem) {

        val mutableList = uiState.value?.list.orEmpty().toMutableList()
        if (item is TodoListItem.Item) {
            when (event) {
                BookmarkEventState.Create -> {
                    _uiState.value = uiState.value?.copy(
                        list = mutableList.apply {
                            add(
                                item
                            )
                        }
                    )
                }
                BookmarkEventState.Update -> {
                    val position = mutableList.indexOfFirst {
                        when (it) {
                            is TodoListItem.Item -> {
                                it.id == item.id
                            }
                        }
                    }

                    _uiState.value = uiState.value?.copy(
                        list = mutableList.also { list ->
                            list[position] = item
                        }
                    )
                }

                BookmarkEventState.Delete -> {
                    _uiState.value = uiState.value?.copy(
                        list = mutableList.apply {
                            removeIf {
                                when (it) {
                                    is TodoListItem.Item -> {
                                        it.id == item.id
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
