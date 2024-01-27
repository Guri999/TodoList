package com.jess.nbcamp.challnge2.assignment.todo.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jess.nbcamp.challnge2.assignment.todo.TodoModel
import com.jess.nbcamp.challnge2.databinding.TodoListItemBinding
//람다로 클릭리스너 구현하기, 뷰타입여러개 만들 구조 미리 만들어두자.
class TodoListAdapter(private val itemClick: (View, Int, TodoModel) -> Unit) :
    ListAdapter<TodoModel, TodoListAdapter.Holder>(TodoModelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            TodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.setView(item)
    }

    class Holder(private val binding: TodoListItemBinding, private val itemClick: (View, Int, TodoModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun setView(item: TodoModel) = with(binding) {
            title.text = item.title
            description.text = item.content
            bookmark.isChecked = item.bookmark
            root.setOnClickListener { itemClick(it,position,item) }
        }
    }

    companion object {
        val TodoModelDiffCallback = object : DiffUtil.ItemCallback<TodoModel>() {
            override fun areItemsTheSame(oldItem: TodoModel, newItem: TodoModel): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: TodoModel, newItem: TodoModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}