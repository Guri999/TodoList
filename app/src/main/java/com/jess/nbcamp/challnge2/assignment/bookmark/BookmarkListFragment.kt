package com.jess.nbcamp.challnge2.assignment.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jess.nbcamp.challnge2.assignment.main.TodoMainViewModel
import com.jess.nbcamp.challnge2.databinding.BookmarkListFragmentBinding

class BookmarkListFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarkListFragment()
    }

    private var _binding: BookmarkListFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: TodoMainViewModel by activityViewModels()

    private val listAdapter by lazy {
        BookmarkListAdapter(
            onClickItem = { position, item ->
                Unit
            },
            onBookmarkChecked = { position, item ->
                sharedViewModel.onBookmarkChecked(
                    position,
                    item
                )
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

    private fun initViewModel() {
        sharedViewModel.bookmarkList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.list)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}