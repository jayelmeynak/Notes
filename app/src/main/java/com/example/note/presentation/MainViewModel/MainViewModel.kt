package com.example.note.presentation.MainViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.note.data.DataBase.DataBaseNote
import com.example.note.data.DataBase.NoteDbModel
import com.example.note.data.DataBase.RepositoryNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<NoteDbModel>>
    private val repository: RepositoryNote

    init {
        val noteDao = DataBaseNote.getDatabase(application).getNoteDao()
        repository = RepositoryNote(noteDao)
        readAllData = repository.readAllNote
    }

    fun getNoteById(noteId: Int): LiveData<NoteDbModel?> {
        return liveData(Dispatchers.IO) {
            val notes = repository.getNoteById(noteId)
            emitSource(notes.map { it }) // Преобразуем LiveData<List<Note>> в LiveData<List<Note>>
        }
    }

    fun insertNote(note: NoteDbModel) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: NoteDbModel) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: NoteDbModel) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun fetchAllNotes(): LiveData<List<NoteDbModel>> {
        return liveData(Dispatchers.IO) {
            val notes = repository.getAllNotes()
            emitSource(notes.map { it }) // Преобразуем LiveData<List<Note>> в LiveData<List<Note>>
        }
    }

    fun filterNotesByColor(color: Int):  LiveData<List<NoteDbModel>> {
        return liveData(Dispatchers.IO) {
            val notes = repository.filterNotesByColor(color)
            emitSource(notes.map { it })
        }
    }

    fun searchNotes(query: String?): LiveData<List<NoteDbModel>> {
        return liveData(Dispatchers.IO) {
            val notes = repository.searchNotes(query)
            emitSource(notes.map { it }) // Преобразуем LiveData<List<Note>> в LiveData<List<Note>>
        }
    }
}