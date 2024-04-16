package com.example.note.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.note.model.Note
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.reflect.KParameter

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class DataBaseNote: RoomDatabase() {
    abstract fun getNoteDao(): DaoNote
    companion object {
        @Volatile
        private var INSTANCE: DataBaseNote? = null
        private val LOCK = Any()

        @OptIn(InternalCoroutinesApi::class)
        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            createDataBase(context).also {
                INSTANCE = it
            }
        }
        fun getDatabase(context: Context):DataBaseNote{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseNote::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DataBaseNote::class.java,
                "note"
            ).build()

    }
}