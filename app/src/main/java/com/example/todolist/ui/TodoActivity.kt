package com.example.todolist.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.Todo
import com.example.todolist.databinding.ActivityTodoBinding

class TodoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityTodoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        setBtn()
    }

    private fun setBtn() {
        binding.btTodoBtn.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("Todo", Todo(binding.etTodoTitle.text.toString(), binding.etTodoInfo.text.toString()))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}