package com.example.toanphamphuc_notepad_app.viewModel_Data

import androidx.compose.ui.graphics.Color

//this class represents the model for each note
data class NoteData(
    val id: Int = 0, //note's id
    var title: String = "", //note's title
    var content: String = "", //note's content
    var isDone: Boolean = false, //whether the note is done or not (with default value false)
    var backgroundColor: Color = Color.White //note's background color, defaulting to White
)