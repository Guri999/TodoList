package com.jess.nbcamp.challnge2.assignment.todo.content

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_ENTITY
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_ENTRY_TYPE
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentConstant.EXTRA_TODO_POSITION
import com.jess.nbcamp.challnge2.databinding.TodoCreateActivityBinding

class TodoContentActivity : AppCompatActivity() {

    companion object {

        fun newIntentCreate(
            context: Context
        ) = Intent(context, TodoContentActivity::class.java).apply {
            putExtra(EXTRA_TODO_ENTRY_TYPE, TodoContentEntryType.CREATE)
        }

        fun newIntentUpdate(
            context: Context,
            position: Int,
            entity: TodoEntity
        ) = Intent(context, TodoContentActivity::class.java).apply {
            putExtra(EXTRA_TODO_ENTRY_TYPE, TodoContentEntryType.UPDATE)
            putExtra(EXTRA_TODO_POSITION, position)
            putExtra(EXTRA_TODO_ENTITY, entity)
        }
    }

    private val binding: TodoCreateActivityBinding by lazy {
        TodoCreateActivityBinding.inflate(layoutInflater)
    }

    private val viewModel: TodoContentViewModel by viewModels {
        TodoContentSavedStateViewModelFactory(
            TodoContentViewModelFactory(),
            this,
            intent.extras
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        uiState.observe(this@TodoContentActivity) {
            binding.etTitle.setText(it.title)
            binding.etContent.setText(it.content)

            // 버튼 처리
            when (it.button) {
                TodoContentButtonUiState.Create -> {
                    binding.btCreate.isVisible = true
                }

                TodoContentButtonUiState.Update -> {
                    binding.btUpdate.isVisible = true
                    binding.btDelete.isVisible = true
                }

                else -> Unit
            }
        }

        event.observe(this@TodoContentActivity) {
            when (it) {
                is TodoContentEvent.Create -> {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(
                            EXTRA_TODO_ENTRY_TYPE,
                            TodoContentEntryType.CREATE
                        )
                        putExtra(
                            EXTRA_TODO_ENTITY,
                            TodoEntity(
                                id = it.id,
                                title = it.title,
                                content = it.content
                            )
                        )
                    })
                    finish()
                }

                is TodoContentEvent.Update -> {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(
                            EXTRA_TODO_ENTRY_TYPE,
                            TodoContentEntryType.UPDATE
                        )
                        putExtra(
                            EXTRA_TODO_ENTITY,
                            TodoEntity(
                                id = it.id,
                                title = it.title,
                                content = it.content
                            )
                        )
                    })
                    finish()
                }

                is TodoContentEvent.Delete -> {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(
                            EXTRA_TODO_ENTRY_TYPE,
                            TodoContentEntryType.DELETE
                        )
                        putExtra(
                            EXTRA_TODO_ENTITY,
                            TodoEntity(
                                id = it.id,
                            )
                        )
                    })
                    finish()
                }
            }
        }
    }

    private fun initView() = with(binding) {
        toolBar.setNavigationOnClickListener {
            finish()
        }

        btCreate.setOnClickListener {
            viewModel.onClickCreate(
                etTitle.text.toString(),
                etContent.text.toString()
            )
        }

        btUpdate.setOnClickListener {
            viewModel.onClickUpdate(
                etTitle.text.toString(),
                etContent.text.toString()
            )
        }

        btDelete.setOnClickListener {
            viewModel.onClickDelete()
        }
    }

}