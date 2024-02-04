package com.jess.nbcamp.challnge2.assignment.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem
import com.jess.nbcamp.challnge2.databinding.BookmarkListItemBinding
import com.jess.nbcamp.challnge2.databinding.UnknownItemBinding

class BookmarkListAdapter(
    private val onClickItem: (Int, TodoListItem) -> Unit,
    private val onBookmarkChecked: (Int, TodoListItem) -> Unit
) : ListAdapter<TodoListItem, BookmarkListAdapter.BookmarkViewHolder>(
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
    abstract class BookmarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: TodoListItem)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TodoListItem.Item -> BookmarkListViewType.ITEM
        else -> BookmarkListViewType.UNKNOWN
    }.ordinal

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder =
        when (BookmarkListViewType.from(viewType)) {
            BookmarkListViewType.ITEM -> BookmarkItemViewHolder(
                BookmarkListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem,
                onBookmarkChecked
            )

            BookmarkListViewType.UNKNOWN -> BookmarkUnknownViewHolder(
                UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    class BookmarkItemViewHolder(
        private val binding: BookmarkListItemBinding,
        private val onClickItem: (Int, TodoListItem) -> Unit,
        private val onBookmarkChecked: (Int, TodoListItem) -> Unit
    ) : BookmarkViewHolder(binding.root) {
        override fun onBind(item: TodoListItem) = with(binding) {
            if (item !is TodoListItem.Item) {
                return@with
            }

            title.text = item.title
            description.text = item.content
            bookmark.isChecked = item.isBookmark

            container.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    TodoListItem.Item(
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        isBookmark = bookmark.isChecked
                    )
                )
            }

            bookmark.setOnClickListener {
                onBookmarkChecked(
                    adapterPosition,
                    item.copy(
                        isBookmark = bookmark.isChecked
                    )
                )
            }
        }

    }

    class BookmarkUnknownViewHolder(
        binding: UnknownItemBinding
    ) : BookmarkViewHolder(binding.root) {
        override fun onBind(item: TodoListItem) = Unit
    }
}