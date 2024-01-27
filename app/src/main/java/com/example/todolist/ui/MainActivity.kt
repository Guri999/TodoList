package com.example.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.Todo
import com.example.todolist.TodoList
import com.example.todolist.ViewPagerAdapter
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.ui.bookmark.BookmarkFragment
import com.example.todolist.ui.todo.TodoFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(this)
    }
    private val tabList by lazy {
        arrayListOf(
            "TODO", "BookMark"
        )
    }
    private lateinit var forResult: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        forResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val todo = result.data?.getParcelableExtra<Todo>("Todo")
                todo?.let { TodoList.totalTodo.add(it) }
            }
        }
        initView()
    }

    private fun initView(){
        setFragment()
    }

    private fun setFragment() {
        viewPagerAdapter.removeFragment()

        viewPagerAdapter.addFragment(TodoFragment())
        viewPagerAdapter.addFragment(BookmarkFragment())
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if (position == 0) binding.fbMainFab.visibility = View.VISIBLE
                else binding.fbMainFab.visibility = View.GONE
            }
        })
        setBtn()
    }

    private fun setBtn(){
        binding.fbMainFab.setOnClickListener {
            val intent = Intent(this, TodoActivity::class.java).apply { putExtra("todo",0) }
            forResult.launch(intent)
        }
    }
}