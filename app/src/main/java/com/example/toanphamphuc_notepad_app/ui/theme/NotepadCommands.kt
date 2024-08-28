package com.example.toanphamphuc_notepad_app.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.toanphamphuc_notepad_app.NoteData

class NotepadCommands {
    private val notes = mutableListOf<NoteData>()
    private var nextId = 1
    private val _notesLiveData = MutableLiveData<List<NoteData>>(notes)
    val notesLiveData: LiveData<List<NoteData>> = _notesLiveData

    fun addNote(title: String, content: String) {
        val newNote = NoteData(id = nextId++, title = title, content = content)
        notes.add(newNote)
        _notesLiveData.value = notes
    }

    fun deleteNote(noteId: Int) {
        notes.removeAll { it.id == noteId }
        _notesLiveData.value = notes
    }

    fun updateNote(updateNote: NoteData) {
        val index = notes.indexOfFirst { it.id == updateNote.id }
        if (index != -1) {
            notes[index] = updateNote
            _notesLiveData.value = notes
        }
    }

    fun getNoteById(noteId: Int): NoteData? {
        return notes.find { it.id == noteId }
    }
}