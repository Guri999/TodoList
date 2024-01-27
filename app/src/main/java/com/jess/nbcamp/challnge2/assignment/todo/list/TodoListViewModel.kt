package com.jess.nbcamp.challnge2.assignment.todo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.camp.util.SingleLiveEvent
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentEntryType

class TodoListViewModel : ViewModel() {

    private val _uiState: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val uiState: LiveData<TodoListUiState> get() = _uiState

    private val _event: SingleLiveEvent<TodoListEvent> = SingleLiveEvent()
    val event: LiveData<TodoListEvent> get() = _event


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

    private fun createTodoItem(entity: TodoEntity): TodoListItem = TodoListItem.Item(
        id = entity.id,
        title = entity.title,
        content = entity.content
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

                uiState.value?.copy(
                    list = mutableList.also { list ->
                        list[position] = createTodoItem(
                            entity
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
}
