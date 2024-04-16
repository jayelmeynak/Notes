package com.example.note.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var noteTitle: String,
    var noteDesc: String,
    var noteEditTime: String,
    var noteColor: Int
): Parcelable

