package com.example.note.domain

class AddNoteUseCase(private val repository: NoteListRepository) {

    suspend fun addNote(note: Note){
        repository.addNote(note)
    }
}