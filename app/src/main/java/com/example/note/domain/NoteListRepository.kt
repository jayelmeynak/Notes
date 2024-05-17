package com.example.note.domain

import androidx.lifecycle.LiveData

interface NoteListRepository {

    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun editNote(note: Note)
    suspend fun getNote(noteId: Int): Note
    fun getNoteList(): LiveData<List<Note>>
}