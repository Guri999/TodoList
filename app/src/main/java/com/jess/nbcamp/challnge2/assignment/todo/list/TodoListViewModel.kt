package com.jess.nbcamp.challnge2.assignment.todo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jess.nbcamp.challnge2.assignment.todo.TodoContentType
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel

class TodoListViewModel : ViewModel() {

    private val _uiState: MutableLiveData<TodoListUiState> =
        MutableLiveData(TodoListUiState.init())
    val uiState: LiveData<TodoListUiState> get() = _uiState

    fun addTodoItem(
        model: TodoModel?
    ) {
        if (model?.title == null && model?.content == null) {
            return
        }

        _uiState.value = uiState.value?.copy(
            list = uiState.value?.list.orEmpty().toMutableList().apply {
                add(model)
            }
        )
    }

    private fun updateToItem(model: TodoModel) {
        val currentList = _uiState.value?.list.orEmpty()
        val updatedList = currentList.map { todoModel ->
            if (todoModel.key == model.key) model.copy() else todoModel.copy()
        }
        _uiState.value = _uiState.value?.copy(
            list = updatedList.toMutableList()
        )
    }

    private fun deleteToItem(model: TodoModel) {
        val currentList = _uiState.value?.list.orEmpty()
        val updatedList = currentList.filter { it.key != model.key }

        _uiState.value = _uiState.value?.copy(
            list = updatedList
        )
    }

    fun handleToItem(entryType: Int , todoModel: TodoModel?) {
        when (entryType){
            TodoContentType.UPDATE.ordinal -> todoModel?.let { updateToItem(it) }
            TodoContentType.DELETE.ordinal -> todoModel?.let { deleteToItem(it) }
            else -> todoModel?.let { addTodoItem(it) }
        }
    }
}
