package com.jess.nbcamp.challnge2.assignment.todo.content

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.jess.camp.util.SingleLiveEvent
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_ENTITY
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_ENTRY_TYPE
import java.util.UUID

class TodoContentViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val entryType get() = savedStateHandle.get<TodoContentEntryType>(EXTRA_TODO_ENTRY_TYPE)
    private val entity get() = savedStateHandle.get<TodoEntity>(EXTRA_TODO_ENTITY)

    private val _uiState: MutableLiveData<TodoContentUiState> =
        MutableLiveData(TodoContentUiState.init())
    val uiState: LiveData<TodoContentUiState> get() = _uiState

    private val _event: SingleLiveEvent<TodoContentEvent> = SingleLiveEvent()
    val event: LiveData<TodoContentEvent> get() = _event

    init {
        _uiState.value = uiState.value?.copy(
            title = entity?.title,
            content = entity?.content,
            button = if (entryType == TodoContentEntryType.UPDATE) {
                TodoContentButtonUiState.Update
            } else {
                TodoContentButtonUiState.Create
            }
        )
    }

    fun onClickCreate(
        title: String,
        content: String,
    ) {
        _event.value = TodoContentEvent.Create(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content
        )
    }

    fun onClickUpdate(
        title: String,
        content: String,
    ) {
        _event.value = TodoContentEvent.Update(
            id = entity?.id,
            title = title,
            content = content
        )
    }
}

class TodoContentViewModelFactory {
    fun create(
        savedStateHandle: SavedStateHandle
    ) = TodoContentViewModel(
        savedStateHandle
    )
}

class TodoContentSavedStateViewModelFactory(
    private val factory: TodoContentViewModelFactory,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = factory.create(handle) as T
}