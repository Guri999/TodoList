package com.jess.nbcamp.challnge2.assignment.todo.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.assignment.todo.TodoContentType
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel
import com.jess.nbcamp.challnge2.databinding.TodoCreateActivityBinding
import com.jess.nbcamp.challnge2.practice.signup.SignUpActivity
import com.jess.nbcamp.challnge2.practice.signup.SignUpUserEntity

class TodoContentActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_TODO_MODEL = "extra_todo_model"
        const val EXTRA_TODO_ENTRY = "extra_todo_entry"
        const val EXTRA_TODO_TYPE = "extra_todo_type"

        fun newIntent(
            context: Context,
            entryType: TodoContentType = TodoContentType.CREATE,
            todoModel: TodoModel? = null
        ): Intent {
            val intent = Intent(context, TodoContentActivity::class.java)
            intent.putExtra(EXTRA_TODO_MODEL, todoModel)
            intent.putExtra(EXTRA_TODO_ENTRY, entryType.ordinal)
            return intent
        }
    }

    private val binding: TodoCreateActivityBinding by lazy {
        TodoCreateActivityBinding.inflate(layoutInflater)
    }

    private val todoModel: TodoModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TODO_MODEL, TodoModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TODO_MODEL)
        }
    }

    private val todoType: TodoContentType? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TODO_TYPE, TodoContentType::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TODO_TYPE)
        }
    }

    private val entryType: TodoContentType by lazy {
        TodoContentType.getEntryType(intent.getIntExtra(EXTRA_TODO_ENTRY, TodoContentType.CREATE.ordinal))
    }

    private val viewModel: TodoContentViewModel by viewModels{
        TodoContentViewModelFactory(
            entryType,
            todoModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        toolBar.setNavigationOnClickListener {
            finish()
        }

        submit.setOnClickListener {
            val title = binding.todoTitle.text.toString()
            val content = binding.todoContent.text.toString()
            when (entryType) {
                TodoContentType.UPDATE -> {
                    val updatedTodoModel = todoModel?.copy(title = title, content = content)
                    val intent = Intent().apply {
                        putExtra(EXTRA_TODO_MODEL, updatedTodoModel)
                        putExtra(EXTRA_TODO_TYPE, TodoContentType.UPDATE)
                    }
                    setResult(Activity.RESULT_OK, intent)
                }
                else -> {
                    val newTodoModel = TodoModel(title = title, content = content)
                    val intent = Intent().apply {
                        putExtra(EXTRA_TODO_MODEL, newTodoModel)
                    }
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            finish()
        }

        delete.setOnClickListener {
            val deleteTodoModel = todoModel
            val intent = Intent().apply {
                putExtra(EXTRA_TODO_MODEL, deleteTodoModel)
                putExtra(EXTRA_TODO_TYPE, TodoContentType.DELETE)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


        viewModel.contentUiState.observe(this@TodoContentActivity) {
            todoTitle.setText(it.todoModel.title)
            todoContent.setText(it.todoModel.content)
            delete.isVisible = it.btnVisible
            submit.setText(R.string.todo_create_edit)
        }

    }

}