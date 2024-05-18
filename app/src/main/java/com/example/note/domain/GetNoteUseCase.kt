package com.example.note.domain

class GetNoteUseCase(private val repository: NoteListRepository) {

    suspend fun getNote(noteId: Int): Note{
        return repository.getNote(noteId)
    }
}