package com.example.note.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.note.MainViewModel.MainViewModel
import com.example.note.R
import com.example.note.databinding.FragmentEditNoteBinding
import com.example.note.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.note.dialogmanager.DialogManager

class EditNoteFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var viewModel: MainViewModel
    private var noteColor: Int = R.color.orange
    private val args by navArgs<EditNoteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        return binding.root
    }

    override fun onStop() {
        editNoteToDataBase()
        super.onStop()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edEditNoteTitle.setText(args.note.noteTitle)
        binding.edEditNoteDesc.setText(args.note.noteDesc)
        binding.tvEditTimeDate.setText(args.note.noteEditTime)
        noteColor = args.note.noteColor
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setColorImage()
        binding.bEditDoneEdit.setOnClickListener {
            editNoteToDataBase()
        }

        binding.bDelete.setOnClickListener {
            DialogManager.deleteNote(requireContext(),object : DialogManager.Listener{
                override fun onClick(name: Int) {
                    when(name){
                        1 -> deleteNote()
                        0 -> return
                    }
                }
            })
        }

        binding.clEditNote.setOnClickListener{
            hideKeyboard()
        }
        binding.edEditNoteDesc.setSelection(binding.edEditNoteDesc.getText().length)
        binding.edEditNoteDesc.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)

//        val rootView = binding.root
//        val listener = ViewTreeObserver.OnGlobalLayoutListener {
//            val rect = Rect()
//            rootView.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = rootView.height
//            val keypadHeight = screenHeight - rect.bottom
//
//            if (keypadHeight > screenHeight * 0.1) {
//                binding.edEditNoteDesc.maxLines = 13
//                Log.d("MyLog", binding.edEditNoteDesc.maxLines.toString()
//                )
//            } else {
//                binding.edEditNoteDesc.maxLines = 30
//                Log.d("MyLog", binding.edEditNoteDesc.maxLines.toString())
//            }
//
//        }
//        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)

    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setColorImage(){
        val imageView = binding.imEditNote
        val originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launch)
        val color = Color.parseColor("#${Integer.toHexString(noteColor)}")
        originalDrawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        imageView.setImageDrawable(originalDrawable)
    }

    private fun deleteNote(){
        val noteTitle = binding.edEditNoteTitle.text.toString()
        val noteDesc = binding.edEditNoteDesc.text.toString()
        val noteTime = binding.tvEditTimeDate.text.toString()
        val note = Note(args.note.id, noteTitle, noteDesc, noteTime, args.note.noteColor)
        viewModel.deleteNote(note)
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    private fun editNoteToDataBase() {
        val noteTitle = binding.edEditNoteTitle.text.toString()
        val noteDesc = binding.edEditNoteDesc.text.toString()
        val noteEditTime = getCurrentDateTime()
        if (noteTitle.isNotEmpty()) {
            val note = Note(args.note.id, noteTitle, noteDesc, noteEditTime, noteColor)
            viewModel.updateNote(note)
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

    companion object {

        @JvmStatic
        fun newInstance() = EditNoteFragment()
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
                        noteColor = name
                        setColorImage()
                    }
                })
                true
            }

            R.id.saveEditNote ->{
                editNoteToDataBase()
                true
            }

            R.id.deleteEditNote -> {
                DialogManager.deleteNote(requireContext(),object : DialogManager.Listener{
                    override fun onClick(name: Int) {
                        when(name){
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