package com.jess.nbcamp.challnge2.assignment.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentEntryType
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListUiState

class TodoMainViewModel : ViewModel() {

    private val _uiState: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val uiState: LiveData<TodoListUiState> get() = _uiState

    private val _bookmarkList: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val bookmarkList: LiveData<TodoListUiState> get() = _bookmarkList

    fun addTodoItem(
        model: TodoEntity?
    ) {
        if (model == null) {
            return
        }

        val mutableList = uiState.value?.list.orEmpty().toMutableList()
        _uiState.value = uiState.value?.copy(
            list = mutableList.apply {
                add(
                    createTodoItem(model)
                )
            }
        )
    }

    private fun createTodoItem(entity: TodoEntity, isBookmark: Boolean = false): TodoListItem = TodoListItem.Item(
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

    fun onBookmarkChecked(position: Int, item: TodoListItem) {
        val mutableList = uiState.value?.list.orEmpty().toMutableList()

        _uiState.value = uiState.value?.copy(
            list = mutableList.also { list ->
                list[position] = item
            }
        )
    }

    fun setBookmarkList() {
        val mutableList = uiState.value?.list.orEmpty()
        val bookmarkItems = mutableList.filter {
            when(it) {
                is TodoListItem.Item -> it.isBookmark
                else -> false
            }
        }
        _bookmarkList.value = TodoListUiState(bookmarkItems)
    }
}