package com.example.note.domain

import androidx.lifecycle.LiveData

class FilterNotesUseCase(private val repository: NoteListRepository) {

    suspend fun filterNotes(query: Int): LiveData<List<Note>>{
        return repository.filterNotes(query)
    }
}