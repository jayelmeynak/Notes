package com.example.note.MainViewModel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.note.DataBase.DataBaseNote
import com.example.note.DataBase.RepositoryNote
import com.example.note.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Note>>
    private val repository: RepositoryNote

    init {
        val noteDao = DataBaseNote.getDatabase(application).getNoteDao()
        repository = RepositoryNote(noteDao)
        readAllData = repository.readAllNote
    }

    fun getNoteById(noteId: Int): LiveData<Note?> {
        return liveData(Dispatchers.IO) {
            val notes = repository.getNoteById(noteId)
            emitSource(notes.map { it }) // Преобразуем LiveData<List<Note>> в LiveData<List<Note>>
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun fetchAllNotes(): LiveData<List<Note>> {
        return liveData(Dispatchers.IO) {
            val notes = repository.getAllNotes()
            emitSource(notes.map { it }) // Преобразуем LiveData<List<Note>> в LiveData<List<Note>>
        }
    }

    fun filterNotesByColor(color: Int):  LiveData<List<Note>> {
        return liveData(Dispatchers.IO) {
            val notes = repository.filterNotesByColor(color)
            emitSource(notes.map { it })
        }
    }

    fun searchNotes(query: String?): LiveData<List<Note>> {
        return liveData(Dispatchers.IO) {
            val notes = repository.searchNotes(query)
            emitSource(notes.map { it }) // Преобразуем LiveData<List<Note>> в LiveData<List<Note>>
        }
    }
}