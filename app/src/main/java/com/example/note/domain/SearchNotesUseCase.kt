package com.example.note.domain

import androidx.lifecycle.LiveData

class SearchNotesUseCase(private val repository: NoteListRepository) {

    suspend fun searchNotes(query: String?): LiveData<List<Note>>{
        return repository.serchNotes(query)
    }
}