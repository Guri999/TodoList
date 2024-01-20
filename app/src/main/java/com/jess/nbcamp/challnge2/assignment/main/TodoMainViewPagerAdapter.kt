package com.jess.nbcamp.challnge2.assignment.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.assignment.bookmark.BookmarkListFragment
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListFragment

class TodoMainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        TodoMainTab(TodoListFragment.newInstance(), R.string.main_tab_todo_title),
        TodoMainTab(BookmarkListFragment.newInstance(), R.string.main_tab_bookmark_title),
    )

    fun getFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

    fun getTitle(position: Int): Int {
        return fragments[position].title
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
}