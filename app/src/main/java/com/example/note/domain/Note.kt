package com.example.note.domain



data class Note(
    var noteTitle: String,
    var noteDesc: String,
    var noteEditTime: String,
    var noteColor: Int,
    val id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = 0
    }
}
