package com.example.toanphamphuc_notepad_app

import androidx.compose.ui.graphics.Color

data class NoteData(
    val id: Int = 0,
    var title: String = "",
    var content: String = "",
    var isDone: Boolean = false,
    var backgroundColor: Color = Color.White
)