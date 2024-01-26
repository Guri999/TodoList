package com.jess.nbcamp.challnge2.assignment.todo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jess.nbcamp.challnge2.databinding.TodoListItemBinding
import com.jess.nbcamp.challnge2.databinding.UnknownItemBinding

class TodoListAdapter(
    private val onClickItem: (Int, TodoListItem) -> Unit,
    private val onBookmarkChecked: (Int, TodoListItem) -> Unit
) : ListAdapter<TodoListItem, TodoListAdapter.TodoViewHolder>(

    object : DiffUtil.ItemCallback<TodoListItem>() {
        override fun areItemsTheSame(
            oldItem: TodoListItem,
            newItem: TodoListItem
        ): Boolean = if (oldItem is TodoListItem.Item && newItem is TodoListItem.Item) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }


        override fun areContentsTheSame(
            oldItem: TodoListItem,
            newItem: TodoListItem
        ): Boolean = oldItem == newItem

    }
) {

    abstract class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun onBind(item: TodoListItem)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TodoListItem.Item -> TodoListViewType.ITEM
        else -> TodoListViewType.UNKNOWN
    }.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
        when (TodoListViewType.from(viewType)) {
            TodoListViewType.ITEM -> TodoItemViewHolder(
                TodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem,
                onBookmarkChecked
            )

            else -> TodoUnknownViewHolder(
                UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class TodoItemViewHolder(
        private val binding: TodoListItemBinding,
        private val onClickItem: (Int, TodoListItem) -> Unit,
        private val onBookmarkChecked: (Int, TodoListItem) -> Unit
    ) : TodoViewHolder(binding.root) {

        override fun onBind(item: TodoListItem) = with(binding) {
            if (item !is TodoListItem.Item) {
                return@with
            }

            title.text = item.title
            description.text = item.content
            bookmark.isChecked = item.isBookmark

            // 아이템 클릭
            container.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }

            // 북마크 클릭
            bookmark.setOnClickListener {
                if (item.isBookmark != bookmark.isChecked) {
                    onBookmarkChecked(
                        adapterPosition,
                        item.copy(
                            isBookmark = bookmark.isChecked
                        )
                    )
                }
            }
        }
    }

    class TodoUnknownViewHolder(
        binding: UnknownItemBinding
    ) : TodoViewHolder(binding.root) {
        override fun onBind(item: TodoListItem) = Unit
    }
}