package com.jess.nbcamp.challnge2.assignment.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jess.nbcamp.challnge2.assignment.main.TodoMainViewModel
import com.jess.nbcamp.challnge2.assignment.todo.list.TodoListItem
import com.jess.nbcamp.challnge2.databinding.BookmarkListFragmentBinding

class BookmarkListFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarkListFragment()
    }

    private var _binding: BookmarkListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkListViewModel by viewModels()

    private val sharedViewModel: TodoMainViewModel by activityViewModels()

    private val listAdapter by lazy {
        BookmarkListAdapter(
            onClickItem = { position, item ->
                viewModel.onClickItem(
                    position,
                    item
                )
            },
            onBookmarkChecked = { position, item ->
                when(item){
                    is TodoListItem.Item -> {
                        if (item.isBookmark){
                            sharedViewModel.createBookmark(
                                item
                            )
                        } else {
                            sharedViewModel.deleteBookmark(
                                item
                            )
                        }
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookmarkListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        todoList.adapter = listAdapter
    }

    private fun initViewModel() = with(viewModel){
        sharedViewModel.bookmarkEvent.observe(viewLifecycleOwner) { event ->
            setBookmarkItem(
                event.entryType,
                event.item
            )
        }

        uiState.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.list)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}