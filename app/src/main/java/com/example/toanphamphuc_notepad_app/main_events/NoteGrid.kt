package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.runtime.Composable
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData

@Composable
fun NoteGrid() {

}
@Composable
fun NoteGridItem(note: NoteData, onDoneChange: (NoteData) -> Unit, onDeleteClick: (NoteData) -> Unit, onEditClick: (NoteData) -> Unit) {
    //...
}