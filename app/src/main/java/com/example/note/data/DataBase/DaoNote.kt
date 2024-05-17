package com.example.note.data.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoNote {

    @Insert
    fun insertNote(note: NoteDbModel)

    @Update
    fun updateNote(note: NoteDbModel)

    @Delete
    fun deleteNote(note: NoteDbModel)

    @Query("SELECT * FROM NoteDbModel ORDER BY noteEditTime DESC")
    fun getAllNotes(): LiveData<List<NoteDbModel>>

    @Query("SELECT * FROM NoteDbModel WHERE noteTitle LIKE '%' || :query || '%' OR noteDesc LIKE '%' || :query || '%';\n")
    fun searchNotes(query: String?): LiveData<List<NoteDbModel>>

    @Query("SELECT * FROM NoteDbModel WHERE noteColor = :color")
    fun filterNotesByColor(color: Int): LiveData<List<NoteDbModel>>


    @Query("SELECT * FROM NoteDbModel WHERE id = :noteId")
    fun getNoteById(noteId: Int): LiveData<NoteDbModel?>

}