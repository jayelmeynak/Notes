package com.example.note.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.note.model.Note
@Dao
interface DaoNote {

    @Insert
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM NOTE ORDER BY noteEditTime DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTE WHERE noteTitle LIKE '%' || :query || '%' OR noteDesc LIKE '%' || :query || '%';\n")
    fun searchNotes(query: String?): LiveData<List<Note>>

    @Query("SELECT * FROM NOTE WHERE noteColor = :color")
    fun filterNotesByColor(color: Int): LiveData<List<Note>>


    @Query("SELECT * FROM NOTE WHERE id = :noteId")
    fun getNoteById(noteId: Int): LiveData<Note?>

}