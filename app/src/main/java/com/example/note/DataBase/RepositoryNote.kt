package com.example.note.DataBase


import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.note.model.Note

class RepositoryNote(private val noteDao: DaoNote) {
    val readAllNote:  LiveData<List<Note>> = noteDao.getAllNotes()


    suspend fun insertNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    suspend fun getAllNotes(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.getAllNotes()
        }
    }

    suspend fun searchNotes(query: String?): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.searchNotes(query)
        }
    }

    suspend fun filterNotesByColor(color: Int): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.filterNotesByColor(color)
        }
    }

    suspend fun getNoteById(noteId: Int): LiveData<Note?>{
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.getNoteById(noteId)
        }
    }
}