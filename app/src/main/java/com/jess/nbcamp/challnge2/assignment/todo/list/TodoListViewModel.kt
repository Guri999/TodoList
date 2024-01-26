package com.jess.nbcamp.challnge2.assignment.todo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.camp.util.SingleLiveEvent
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import java.util.concurrent.atomic.AtomicLong

class TodoListViewModel : ViewModel() {

    private val id = AtomicLong()

    private val _uiState: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val uiState: LiveData<TodoListUiState> get() = _uiState

    private val _event: SingleLiveEvent<TodoListEvent> = SingleLiveEvent()
    val event: LiveData<TodoListEvent> get() = _event

    private val mutableList get() = uiState.value?.list.orEmpty().toMutableList()

    fun addTodoItem(
        model: TodoEntity?
    ) {

        fun createTodoItem(model: TodoEntity): TodoListItem = TodoListItem.Item(
            id = id.getAndIncrement(),
            title = model.title,
            content = model.content
        )

        if (model == null) {
            return
        }

        _uiState.value = uiState.value?.copy(
            list = mutableList.apply {
                add(createTodoItem(model))
            }
        )
    }

    fun onClickItem(
        position: Int,
        item: TodoListItem
    ) {
        _event.value = when (item) {
            is TodoListItem.Item -> TodoListEvent.OpenContent(
                position,
                TodoEntity(
                    title = item.title,
                    content = item.content
                )
            )
        }
    }
}
