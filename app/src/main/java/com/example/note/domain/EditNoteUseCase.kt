package com.example.note.domain

class EditNoteUseCase(private val repository: NoteListRepository) {

    suspend fun editNote(note: Note){
        repository.editNote(note)
    }
}