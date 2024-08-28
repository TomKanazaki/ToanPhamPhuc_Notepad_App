package com.example.toanphamphuc_notepad_app

data class NoteData (
    val id: Int = 0,
    var title: String = "",
    var content: String = "",
    var isDone: Boolean = false
)