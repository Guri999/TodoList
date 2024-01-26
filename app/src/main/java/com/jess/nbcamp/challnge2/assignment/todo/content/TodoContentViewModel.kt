package com.jess.nbcamp.challnge2.assignment.todo.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity

class TodoContentViewModel(
    private val entryType: TodoContentEntryType,
    private val entity: TodoEntity?
) : ViewModel() {

    private val _uiState: MutableLiveData<TodoContentUiState> =
        MutableLiveData(TodoContentUiState.init())
    val uiState: LiveData<TodoContentUiState> get() = _uiState

    init {
        _uiState.value = uiState.value?.copy(
            title = entity?.title,
            content = entity?.content
        )
    }

}

class TodoContentViewModelFactory(
    private val entryType: TodoContentEntryType,
    private val entity: TodoEntity?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoContentViewModel::class.java)) {
            return TodoContentViewModel(
                entryType,
                entity
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}