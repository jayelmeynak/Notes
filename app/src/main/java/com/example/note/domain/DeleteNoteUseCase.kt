package com.example.note.domain

class DeleteNoteUseCase(private val repository: NoteListRepository) {

    suspend fun deleteNote(note: Note){
        repository.deleteNote(note)
    }
}