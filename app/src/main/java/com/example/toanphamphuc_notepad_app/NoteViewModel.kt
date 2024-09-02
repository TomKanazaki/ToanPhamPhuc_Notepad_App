package com.example.toanphamphuc_notepad_app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {
    private val _notesLiveData = mutableStateListOf<NoteData>()
    val notesLiveData: List<NoteData> = _notesLiveData
    private var nextId = 1

    fun addNote(title: String, content: String) {
        val newNote = NoteData(id = nextId++, title = title, content = content)
        _notesLiveData.add(newNote)
    }

    fun deleteNote(noteId: Int) {
        _notesLiveData.removeAll { it.id == noteId }
    }

    fun updateNote(updatedNote: NoteData) {
        val index = _notesLiveData.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            _notesLiveData[index] = updatedNote
        }
    }

//    fun getNoteById(noteId: Int): NoteData? {
//        return _notesLiveData.find { it.id == noteId }
//    }
}