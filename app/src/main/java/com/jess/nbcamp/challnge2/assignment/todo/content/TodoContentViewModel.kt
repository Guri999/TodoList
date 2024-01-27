package com.jess.nbcamp.challnge2.assignment.todo.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jess.nbcamp.challnge2.assignment.todo.TodoContentType
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel

class TodoContentViewModel(private val entryType: TodoContentType, private val lastTodoModel: TodoModel) : ViewModel() {
    private val _contentUiState: MutableLiveData<ContentUiState> = MutableLiveData()
    val contentUiState: LiveData<ContentUiState> get() = _contentUiState

    init {
        initListUiState()
    }
    private fun initListUiState() {
        if (entryType == TodoContentType.UPDATE){
            _contentUiState.value = ContentUiState(
                todoModel = lastTodoModel,
                btnVisible = true
            )
        }
    }
}

class TodoContentViewModelFactory(
    private val entryType: TodoContentType,
    private val todoModel: TodoModel?
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoContentViewModel::class.java)) {
            return TodoContentViewModel(
                entryType,
                todoModel ?: TodoModel()
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}