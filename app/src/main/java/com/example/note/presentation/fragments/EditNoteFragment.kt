package com.example.note.presentation.fragments

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.note.R
import com.example.note.databinding.FragmentEditNoteBinding
import com.example.note.domain.Note
import com.example.note.presentation.DialogManager
import com.example.note.presentation.MainViewModel

class EditNoteFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var viewModel: MainViewModel
    private var noteColorArg: Int = 0
    private val args by navArgs<EditNoteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getNote(args.noteId)
        return binding.root
    }

    override fun onStop() {
        editNoteToDataBase()
        super.onStop()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.note.observe(viewLifecycleOwner) { note ->
            binding.edEditNoteTitle.setText(note.noteTitle)
            binding.edEditNoteDesc.setText(note.noteDesc)
            binding.tvEditTimeDate.setText(note.noteEditTime)
            noteColorArg = note.noteColor
            setColorImage()
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setColorImage()
        binding.bEditDoneEdit.setOnClickListener {
            editNoteToDataBase()
        }

        binding.bDelete.setOnClickListener {
            DialogManager.deleteNote(requireContext(), object : DialogManager.Listener {
                override fun onClick(name: Int) {
                    when (name) {
                        1 -> deleteNote()
                        0 -> return
                    }
                }
            })
        }

        binding.clEditNote.setOnClickListener {
            hideKeyboard()
        }
        binding.edEditNoteDesc.setSelection(binding.edEditNoteDesc.getText().length)
        binding.edEditNoteDesc.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setColorImage() {
        val imageView = binding.imEditNote
        val originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launch)
        originalDrawable?.let {
            it.colorFilter = PorterDuffColorFilter(noteColorArg, PorterDuff.Mode.SRC_IN)
            imageView.setImageDrawable(it)
        }
    }


    private fun deleteNote() {
        val noteTitle = binding.edEditNoteTitle.text.toString()
        val noteDesc = binding.edEditNoteDesc.text.toString()
        val noteTime = binding.tvEditTimeDate.text.toString()
        val note = Note(noteTitle, noteDesc, noteTime, noteColorArg, args.noteId)
        viewModel.deleteNote(note)
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    private fun editNoteToDataBase() {
        val noteTitle = binding.edEditNoteTitle.text.toString()
        val noteDesc = binding.edEditNoteDesc.text.toString()
        if (noteTitle.isNotEmpty()) {
            viewModel.editNote(noteTitle, noteDesc, noteColorArg)
            findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(requireContext(), R.string.enter_title, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.edit_color_change -> {
                DialogManager.changeNoteColor(requireContext(), object : DialogManager.Listener {
                    override fun onClick(name: Int) {
                        noteColorArg = name
                        setColorImage()
                    }
                })
                true
            }

            R.id.saveEditNote -> {
                editNoteToDataBase()
                true
            }

            R.id.deleteEditNote -> {
                DialogManager.deleteNote(requireContext(), object : DialogManager.Listener {
                    override fun onClick(name: Int) {
                        when (name) {
                            1 -> deleteNote()
                            0 -> return
                        }
                    }
                })
                true
            }

            else -> false
        }
    }
}