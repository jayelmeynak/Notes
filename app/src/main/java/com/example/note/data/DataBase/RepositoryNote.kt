package com.example.note.data.DataBase


import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryNote(private val noteDao: DaoNote) {
    val readAllNote:  LiveData<List<NoteDbModel>> = noteDao.getAllNotes()


    suspend fun insertNote(note: NoteDbModel) {
        withContext(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    suspend fun updateNote(note: NoteDbModel) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    suspend fun deleteNote(note: NoteDbModel) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    suspend fun getAllNotes(): LiveData<List<NoteDbModel>> {
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.getAllNotes()
        }
    }

    suspend fun searchNotes(query: String?): LiveData<List<NoteDbModel>> {
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.searchNotes(query)
        }
    }

    suspend fun filterNotesByColor(color: Int): LiveData<List<NoteDbModel>> {
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.filterNotesByColor(color)
        }
    }

    suspend fun getNoteById(noteId: Int): LiveData<NoteDbModel?>{
        return withContext(Dispatchers.IO) {
            return@withContext noteDao.getNoteById(noteId)
        }
    }
}