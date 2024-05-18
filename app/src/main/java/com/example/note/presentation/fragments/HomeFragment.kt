package com.example.note.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.presentation.MainViewModel
import com.example.note.R
import com.example.note.presentation.NoteAdapter
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.presentation.DialogManager
import com.example.note.domain.Note
import kotlin.properties.Delegates

class HomeFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NoteAdapter
    private var filterColor by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.readAllData.observe(viewLifecycleOwner, Observer { note ->
            adapter.setData(note)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcInit()
        filterColor = binding.tv.textColors.defaultColor
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.bAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }


    private fun searchNote(query: String?) {
        val searchQuery = "%$query"

        viewModel.searchNotes(searchQuery).observe(this) { list ->
            adapter.setData(list)
        }
    }


    private fun rcInit() = with(binding) {
        adapter = NoteAdapter()
        rcNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rcNote.setHasFixedSize(true)
        rcNote.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.filterColor -> {
                DialogManager.filterNoteColor(requireContext(), object : DialogManager.Listener {
                    override fun onClick(name: Int) {
                        var isWhite = false
                        val liveDataList: LiveData<List<Note>>
                        if (filterColor == name) {
                            isWhite = true
                        } else {
                            liveDataList = viewModel.filterNotesByColor(name)
                            liveDataList.observe(viewLifecycleOwner, Observer { list ->
                                if (list.isEmpty()) {
                                    Toast.makeText(
                                        requireContext(),
                                        R.string.not_found_filter,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.readAllData.observe(
                                        viewLifecycleOwner,
                                        Observer { notes ->
                                            adapter.setData(notes)
                                        })
                                } else {
                                    adapter.setData(list)
                                }
                            })
                        }
                        if (isWhite) {
                            viewModel.readAllData.observe(viewLifecycleOwner, Observer { notes ->
                                adapter.setData(notes)
                            })
                        }
                    }
                })
            }
        }
        return false
    }

}