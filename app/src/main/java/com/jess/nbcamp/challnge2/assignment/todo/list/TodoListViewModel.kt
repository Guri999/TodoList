package com.jess.nbcamp.challnge2.assignment.todo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel
import java.util.concurrent.atomic.AtomicLong

class TodoListViewModel : ViewModel() {

    private val id = AtomicLong()

    private val _uiState: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val uiState: LiveData<TodoListUiState> get() = _uiState

    fun addTodoItem(
        model: TodoModel?
    ) {
        if (model == null) {
            return
        }

        _uiState.value = uiState.value?.copy(
            list = uiState.value?.list.orEmpty().toMutableList().apply {
                add(createTodoItem(model))
            }
        )
    }

    private fun createTodoItem(model: TodoModel): TodoListItem = TodoListItem.Item(
        id = id.getAndIncrement(),
        title = model.title,
        content = model.content
    )
}
