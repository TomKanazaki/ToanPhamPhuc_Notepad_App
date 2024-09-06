package com.example.toanphamphuc_notepad_app.viewModel_Data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteViewModel : ViewModel() {
//    private val _notesLiveData = mutableStateListOf<NoteData>()
    private val _notesLiveData = MutableStateFlow<List<NoteData>>(emptyList())
    //val notesLiveData: List<NoteData> = _notesLiveData
    val notesLiveData: StateFlow<List<NoteData>> = _notesLiveData.asStateFlow()

    private val _deleteNotesLiveData = MutableStateFlow<List<NoteData>>(emptyList())
    val deleteNotesLiveData: StateFlow<List<NoteData>> = _deleteNotesLiveData.asStateFlow()

    private var nextId = 1

    fun addNote(title: String, content: String, backgroundColor: Color) {
        val newNote = NoteData(id = nextId++, title = title, content = content, backgroundColor = backgroundColor)
        _notesLiveData.value += newNote
    }

    fun deleteNote(noteId: Int) {
        _notesLiveData.value = _notesLiveData.value.filter { it.id != noteId }
    }

    fun restoreNote(note: NoteData) {
        _deleteNotesLiveData.value = _deleteNotesLiveData.value.filter { it.id != note.id   }
        _notesLiveData.value += note
    }

    fun permanentlyDeleteNote(noteId: Int) {
        _deleteNotesLiveData.value = _deleteNotesLiveData.value.filter { it.id != noteId }
    }

    fun deleteAllTrash() {
        _deleteNotesLiveData.value = emptyList()
    }

    fun updateNote(updatedNote: NoteData) {
//        val index = _notesLiveData.indexOfFirst { it.id == updatedNote.id }
//        if (index != -1) {
//            _notesLiveData[index] = updatedNote
//        }
        _notesLiveData.value = _notesLiveData.value.map { if (it.id == updatedNote.id) updatedNote else it }
    }
}