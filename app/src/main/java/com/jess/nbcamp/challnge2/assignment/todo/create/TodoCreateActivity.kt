package com.jess.nbcamp.challnge2.assignment.todo.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jess.nbcamp.challnge2.databinding.TodoCreateActivityBinding

class TodoCreateActivity : AppCompatActivity() {

    companion object {
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

    }

}