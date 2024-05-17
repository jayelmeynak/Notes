package com.example.note.domain



data class Note(
    val id: Int,
    var noteTitle: String,
    var noteDesc: String,
    var noteEditTime: String,
    var noteColor: Int
)