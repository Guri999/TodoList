package com.jess.nbcamp.challnge2.assignment.todo.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.databinding.TodoCreateActivityBinding

class TodoContentActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_TODO_ENTRY_TYPE = "extra_todo_entry_type"
        const val EXTRA_TODO_POSITION = "extra_todo_position"
        const val EXTRA_TODO_ENTITY = "extra_todo_entity"

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
        TodoContentViewModelFactory(
            entryType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(
                    EXTRA_TODO_ENTRY_TYPE,
                    TodoContentEntryType::class.java
                )
            } else {
                intent?.getSerializableExtra(EXTRA_TODO_ENTRY_TYPE) as? TodoContentEntryType
            } ?: TodoContentEntryType.CREATE,
            entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(
                    EXTRA_TODO_ENTITY,
                    TodoEntity::class.java
                )
            } else {
                intent?.getParcelableExtra(
                    EXTRA_TODO_ENTITY
                ) as? TodoEntity
            }
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
        }
    }

    private fun initView() = with(binding) {
        toolBar.setNavigationOnClickListener {
            finish()
        }

        btSubmit.setOnClickListener {
            val intent = Intent().apply {
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                putExtra(
                    EXTRA_TODO_ENTITY,
                    TodoEntity(
                        title = title,
                        content = content
                    )
                )
            }

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}