package com.example.toanphamphuc_notepad_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toanphamphuc_notepad_app.ui.theme.NotepadCommands
import kotlinx.coroutines.launch

class NoteViewModel(private val command: NotepadCommands = NotepadCommands()) : ViewModel() {
    val notesLiveData = command.notesLiveData

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            command.addNote(title, content)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            command.deleteNote(noteId)
        }
    }

    fun updateNote(updateNote: NoteData) {
        viewModelScope.launch {
            command.updateNote(updateNote)
        }
    }

    fun getNoteById(noteId: Int): NoteData? {
        return command.getNoteById(noteId)
    }
}