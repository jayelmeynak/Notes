package com.example.note.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.note.data.NoteListRepositoryImpl
import com.example.note.domain.AddNoteUseCase
import com.example.note.domain.DeleteNoteUseCase
import com.example.note.domain.EditNoteUseCase
import com.example.note.domain.FilterNotesUseCase
import com.example.note.domain.GetNoteListUseCase
import com.example.note.domain.GetNoteUseCase
import com.example.note.domain.Note
import com.example.note.domain.SearchNotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteListRepositoryImpl(application)
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val getNoteListUseCase = GetNoteListUseCase(repository)
    private val searchNotesUseCase = SearchNotesUseCase(repository)
    private val filterNotesUseCase = FilterNotesUseCase(repository)
    val noteList = getNoteListUseCase.getNoteList()

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note>
        get() = _note

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _searchQuery = MutableLiveData<String?>()

    val searchResults: LiveData<List<Note>> = _searchQuery.switchMap { query ->
        liveData(Dispatchers.IO) {
            emitSource(searchNotesUseCase.searchNotes(query))
        }
    }

    private val _filterQuery = MutableLiveData<Int>()

    val filterResults: LiveData<List<Note>> = _filterQuery.switchMap { query ->
        liveData(Dispatchers.IO) {
            filterNotesUseCase.filterNotes(query)
        }
    }

    fun addNote(noteTitle: String, noteDesc: String, noteColor: Int) {
        if (noteTitle.isNotEmpty()) {
            val noteEditTime = getCurrentDateTime()
            val note = Note(noteTitle, noteDesc, noteEditTime, noteColor)
            viewModelScope.launch {
                addNoteUseCase.addNote(note)
                finishWork()
            }
        }

    }

    fun editNote(noteTitle: String, noteDesc: String, noteColor: Int) {
        if (noteTitle.isNotEmpty()) {
            val noteEditTime = getCurrentDateTime()
            _note.value?.let {
                val note =
                    it.copy(
                        noteTitle = noteTitle,
                        noteDesc = noteDesc,
                        noteEditTime = noteEditTime,
                        noteColor = noteColor
                    )
                viewModelScope.launch {
                    editNoteUseCase.editNote(note)
                    finishWork()
                }
            }
        }

    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.deleteNote(note)
        }
    }

    fun searchNotes(query: String?){
        _searchQuery.value = query
    }

    fun setFilterColor(query: Int){
        _filterQuery.value = query
    }

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            val note = getNoteUseCase.getNote(noteId)
            _note.value = note
        }

    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

}