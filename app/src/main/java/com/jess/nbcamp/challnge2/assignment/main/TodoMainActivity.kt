package com.jess.nbcamp.challnge2.assignment.main

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jess.nbcamp.challnge2.assignment.todo.content.TodoContentActivity
import com.jess.nbcamp.challnge2.assignment.todo.TodoEntity
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListFragment
import com.jess.nbcamp.challnge2.databinding.TodoMainActivityBinding

class TodoMainActivity : AppCompatActivity() {

    private val binding: TodoMainActivityBinding by lazy {
        TodoMainActivityBinding.inflate(layoutInflater)
    }

    private val viewPagerAdapter by lazy {
        TodoMainViewPagerAdapter(this)
    }

    private val createTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val todoModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(
                        TodoContentActivity.EXTRA_TODO_ENTITY,
                        TodoEntity::class.java
                    )
                } else {
                    result.data?.getParcelableExtra(
                        TodoContentActivity.EXTRA_TODO_ENTITY
                    )
                }

                val fragment = viewPagerAdapter.getTodoListFragment() as? TodoListFragment
                fragment?.addTodoItem(todoModel)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {
        viewPager.adapter = viewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (viewPagerAdapter.getFragment(position) is TodoListFragment) {
                    fabCreateTodo.show()
                } else {
                    fabCreateTodo.hide()
                }
            }
        })

        // TabLayout x ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        // fab
        fabCreateTodo.setOnClickListener {
            createTodoLauncher.launch(
                TodoContentActivity.newIntentCreate(this@TodoMainActivity)
            )
        }
    }
}