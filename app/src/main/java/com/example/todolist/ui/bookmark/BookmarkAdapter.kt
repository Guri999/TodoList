package com.example.todolist.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Todo
import com.example.todolist.databinding.ItemTodoListBinding

class BookmarkAdapter(private var mItem: List<Todo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun setData(newItems: List<Todo>) {
        mItem = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as Holder) {
            title.text = mItem[position].title
            description.text = mItem[position].info
        }
    }

    inner class Holder(binding: ItemTodoListBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val description = binding.description
        val switch = binding.switch1
    }

}