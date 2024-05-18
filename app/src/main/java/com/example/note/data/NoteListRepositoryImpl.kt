package com.example.note.data


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository

class NoteListRepositoryImpl(application: Application) : NoteListRepository {
    private val noteListDao = DataBaseNote.getInstance(application).noteListDao()
    private val mapper = Mapper()
    override suspend fun addNote(note: Note) {
        noteListDao.addNote(mapper.mapEntityToDbModel(note))
    }

    override suspend fun deleteNote(note: Note) {
        noteListDao.deleteNote(mapper.mapEntityToDbModel(note))
    }

    override suspend fun editNote(note: Note) {
        noteListDao.editNote(mapper.mapEntityToDbModel(note))
    }

    override suspend fun getNote(noteId: Int): Note {
        return mapper.mapDbModelToEntity(noteListDao.getNoteById(noteId))
    }

    override suspend fun serchNotes(query: String?): LiveData<List<Note>> {
        return noteListDao.searchNotes(query).map { list ->
            list.map { noteDbModel ->
                mapper.mapDbModelToEntity(noteDbModel)
            }
        }
    }

    override fun getNoteList(): LiveData<List<Note>> {
        return noteListDao.getAllNotes().map { list ->
            list.map { noteDbModel ->
                mapper.mapDbModelToEntity(noteDbModel)
            }
        }
    }


}