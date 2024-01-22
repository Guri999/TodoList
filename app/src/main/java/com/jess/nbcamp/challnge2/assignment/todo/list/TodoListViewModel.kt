package com.jess.nbcamp.challnge2.assignment.todo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel

class TodoListViewModel : ViewModel() {

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
                add(model)
            }
        )
    }
}
