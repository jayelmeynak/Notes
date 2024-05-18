package com.example.note.domain

import androidx.lifecycle.LiveData

class GetNoteListUseCase(private val repository: NoteListRepository) {

    fun getNoteList(): LiveData<List<Note>>{
        return repository.getNoteList()
    }
}