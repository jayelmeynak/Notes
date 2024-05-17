package com.example.note.domain

class GetNoteListUseCase(private val repository: NoteListRepository) {

    fun getNoteList(){
        repository.getNoteList()
    }
}