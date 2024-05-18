package com.example.note.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteDbModel::class], version = 2, exportSchema = false)
abstract class DataBaseNote: RoomDatabase() {
    abstract fun noteListDao(): NoteListDao

    companion object{
        private var INSTANCE: DataBaseNote? = null
        private val LOCK = Any()
        private const val DB_NAME = "note.db"

        fun getInstance(application: Application): DataBaseNote{
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK){
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    DataBaseNote::class.java,
                    DB_NAME).build()
                INSTANCE = db
                return db
            }
        }
    }
}