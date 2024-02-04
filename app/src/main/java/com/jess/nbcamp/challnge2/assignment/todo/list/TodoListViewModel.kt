package com.jess.nbcamp.challnge2.assignment.todo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.camp.util.SingleLiveEvent
import com.jess.nbcamp.challnge2.assignment.main.BookmarkEventState
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentEntryType
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentUiState

class TodoListViewModel : ViewModel() {

    private val _uiState: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val uiState: LiveData<TodoListUiState> get() = _uiState

    private val _event: SingleLiveEvent<TodoListEvent> = SingleLiveEvent()
    val event: LiveData<TodoListEvent> get() = _event

    private val _updateItem: MutableLiveData<Pair<TodoContentEntryType,TodoListItem>> = MutableLiveData()
    val updateItem: LiveData<Pair<TodoContentEntryType,TodoListItem>> get() = _updateItem


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

    private fun createTodoItem(entity: TodoEntity, isBookmark: Boolean = false): TodoListItem =
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
                        if (isBookmark){
                            _updateItem.value = Pair(entryType,list[position])
                        }
                    }
                )
            }

            TodoContentEntryType.DELETE -> {
                val itemToRemove = mutableList.find { it is TodoListItem.Item && it.id == entity.id }
                if (itemToRemove != null) {
                    _updateItem.value = Pair(TodoContentEntryType.DELETE, itemToRemove)
                }
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
            is TodoListItem.Item -> TodoListEvent.OpenContent(
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
                            find {
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
