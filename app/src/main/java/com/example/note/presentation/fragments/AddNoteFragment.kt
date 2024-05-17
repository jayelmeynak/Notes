package com.example.note.presentation.fragments

import android.content.Context
import android.graphics.Color
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
import com.example.note.presentation.MainViewModel.MainViewModel
import com.example.note.R
import com.example.note.databinding.FragmentAddNoteBinding
import com.example.note.presentation.dialogmanager.DialogManager
import com.example.note.domain.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates


class AddNoteFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: MainViewModel
    private var noteColor by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onStop() {
        addNoteToDataBase()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        noteColor = binding.tvAdd.textColors.defaultColor
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.bAddDoneEdit.setOnClickListener {
            addNoteToDataBase()
        }

        binding.clAddNote.setOnClickListener{
            hideKeyboard()
        }
        binding.tvAddEditTimeDate.text = getCurrentDateTime()

//        val rootView = binding.root
//        val listener = ViewTreeObserver.OnGlobalLayoutListener {
//            val rect = Rect()
//            rootView.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = rootView.height
//            val keypadHeight = screenHeight - rect.bottom
//            val paramsDone = binding.bAddDoneEdit.layoutParams as ConstraintLayout.LayoutParams
//            if (keypadHeight > screenHeight * 0.15) {
//                paramsDone.bottomMargin = keypadHeight + (rect.bottom * 0.15).toInt()
//            } else {
//                paramsDone.bottomMargin = (rect.bottom * 0.05).toInt()
//            }
//
//            binding.bAddDoneEdit.layoutParams = paramsDone
//        }
//        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)


    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun addNoteToDataBase() {
        val noteTitle = binding.edAddNoteTitle.text.toString()
        val noteDesc = binding.edAddNoteDesc.text.toString()
        val noteEditTime = getCurrentDateTime()
        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDesc, noteEditTime, noteColor)
            viewModel.insertNote(note)
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
        fun newInstance() = AddNoteFragment()
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
                        val originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launch)
                        val color = Color.parseColor("#${Integer.toHexString(noteColor)}")
                        originalDrawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                        imageView.setImageDrawable(originalDrawable)

                    }
                })
                true
            }

            R.id.saveAddNote ->{
                addNoteToDataBase()
                true
            }

            else -> false
        }
    }
}