package com.example.toanphamphuc_notepad_app.viewModel_Data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteViewModel : ViewModel() {
    private val _notesLiveData = MutableStateFlow<List<NoteData>>(emptyList())
    val notesLiveData: StateFlow<List<NoteData>> = _notesLiveData.asStateFlow()

    private val _deleteNotesLiveData = MutableStateFlow<List<NoteData>>(emptyList())
    val deleteNotesLiveData: StateFlow<List<NoteData>> = _deleteNotesLiveData.asStateFlow()

    private var nextId = 1

    fun addNote(title: String, content: String, backgroundColor: Color) {
        val newNote = NoteData(id = nextId++, title = title, content = content, backgroundColor = backgroundColor)
        _notesLiveData.value += newNote
    }

    fun updateNote(updatedNote: NoteData) {
        _notesLiveData.value = _notesLiveData.value.map { if (it.id == updatedNote.id) updatedNote else it }
    }

    fun moveNoteToTrash(note: NoteData) {
        deleteNote(note.id)
    }

    fun restoreNote(note: NoteData) {
        _deleteNotesLiveData.value = _deleteNotesLiveData.value.filter { it.id != note.id }
        _notesLiveData.value += note
    }

    private fun deleteNote(noteId: Int) {
        val noteToDelete = _notesLiveData.value.find { it.id == noteId }
        _notesLiveData.value = _notesLiveData.value.filter { it.id != noteId }
        if (noteToDelete != null) {
            _deleteNotesLiveData.value += noteToDelete
        }
    }

    fun permanentlyDeleteNote(noteId: Int) {
        _deleteNotesLiveData.value = _deleteNotesLiveData.value.filter { it.id != noteId }
    }

    fun deleteAllTrash() {
        _deleteNotesLiveData.value = emptyList()
    }
}
