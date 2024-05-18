package com.example.note.data

import com.example.note.domain.Note

class Mapper {

    fun mapDbModelToEntity(noteDbModel: NoteDbModel): Note {
        return Note(
            noteDbModel.noteTitle,
            noteDbModel.noteDesc,
            noteDbModel.noteEditTime,
            noteDbModel.noteColor,
            noteDbModel.id
        )
    }


    fun mapEntityToDbModel(note: Note): NoteDbModel {
        return NoteDbModel(
            note.id,
            note.noteTitle,
            note.noteDesc,
            note.noteEditTime,
            note.noteColor
        )
    }
}