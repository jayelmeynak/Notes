package com.example.note.presentation.fragments

import android.content.Context
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
import com.example.note.R
import com.example.note.databinding.FragmentAddNoteBinding
import com.example.note.presentation.DialogManager
import com.example.note.presentation.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates


class AddNoteFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: MainViewModel
    private var noteColor by Delegates.notNull<Int>()
    private var noteSaved = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onStop() {
        if (!noteSaved) {
            addNoteToDataBase()
        }
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        noteColor = binding.tvAdd.textColors.defaultColor
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.bAddDoneEdit.setOnClickListener {
            onStop()
        }

        binding.clAddNote.setOnClickListener {
            hideKeyboard()
        }
        binding.tvAddEditTimeDate.text = getCurrentDateTime()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun addNoteToDataBase() {
        val noteTitle = binding.edAddNoteTitle.text.toString()
        val noteDesc = binding.edAddNoteDesc.text.toString()
        if (noteTitle.isNotEmpty()) {
            viewModel.addNote(noteTitle, noteDesc, noteColor)
            noteSaved = true
            findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(requireContext(), R.string.enter_title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.add_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add_color_change -> {
                DialogManager.changeNoteColor(requireContext(), object : DialogManager.Listener {
                    override fun onClick(name: Int) {
                        noteColor = name
                        val imageView = binding.imageView
                        val drawable =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launch)
                        drawable?.setTint(noteColor)
                        imageView.setImageDrawable(drawable)

                    }
                })
                true
            }

            R.id.saveAddNote -> {
                addNoteToDataBase()
                true
            }

            else -> false
        }
    }
}