package com.example.note.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.R
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.presentation.DialogManager
import com.example.note.presentation.MainViewModel
import com.example.note.presentation.NoteListAdapter
import kotlin.properties.Delegates

class HomeFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NoteListAdapter
    private var filterColor by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
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

        viewModel.searchResults.observe(viewLifecycleOwner) { noteList ->
            adapter.submitList(noteList)
        }
        viewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            adapter.submitList(noteList)
        }
        viewModel.filterResults.observe(viewLifecycleOwner) { noteList ->
            adapter.submitList(noteList)
        }
    }


    private fun searchNote(query: String?) {
        viewModel.searchNotes(query)
    }


    private fun rcInit() = with(binding) {
        adapter = NoteListAdapter()
        rcNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rcNote.setHasFixedSize(true)
        rcNote.adapter = adapter
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
                        viewModel.setFilterColor(name)
                    }
                })
            }
        }
        return false
    }

}