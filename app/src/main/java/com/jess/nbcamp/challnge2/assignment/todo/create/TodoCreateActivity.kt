package com.jess.nbcamp.challnge2.assignment.todo.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jess.nbcamp.challnge2.databinding.TodoCreateActivityBinding

class TodoCreateActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_TODO_MODEL = "extra_todo_model"

        fun newIntent(
            context: Context
        ) = Intent(context, TodoCreateActivity::class.java)
    }

    private val binding: TodoCreateActivityBinding by lazy {
        TodoCreateActivityBinding.inflate(layoutInflater)
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
            val intent = Intent().apply {
                val title = todoTitle.text.toString()
                val content = todoContent.text.toString()
                putExtra(
                    EXTRA_TODO_MODEL,
                    TodoCreateModel(
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