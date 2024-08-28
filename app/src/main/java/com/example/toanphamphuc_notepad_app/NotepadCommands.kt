package com.example.toanphamphuc_notepad_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NotepadCommands {
    //private val notes = mutableListOf<NoteData>()
    //private var nextId = 1
    //private val _notesLiveData = MutableLiveData<List<NoteData>>(notes)
    private val _notesLiveData = MutableLiveData<List<NoteData>>(emptyList())
    val notesLiveData: LiveData<List<NoteData>> = _notesLiveData

    fun addNote(title: String, content: String) {
//        val newNote = NoteData(id = nextId++, title = title, content = content)
//        notes.add(newNote)
//        _notesLiveData.value = notes
        val newNote = NoteData(_notesLiveData.value!!.size + 1, title, content, false)
        _notesLiveData.value = _notesLiveData.value!! + newNote
    }

    fun deleteNote(noteId: Int) {
//        notes.removeAll { it.id == noteId }
//        _notesLiveData.value = notes
        _notesLiveData.value = _notesLiveData.value?.toMutableList()?.also {
            it.removeAll{ note -> note.id == noteId}
        }
    }

    fun updateNote(updateNote: NoteData) {
//        val index = notes.indexOfFirst { it.id == updateNote.id }
//        if (index != -1) {
//            notes[index] = updateNote
//            _notesLiveData.value = notes
//        }
        _notesLiveData.value = _notesLiveData.value?.map {
            if(it.id == updateNote.id) updateNote else it
        }
    }

    fun getNoteById(noteId: Int): NoteData? {
//        return notes.find { it.id == noteId }
        return _notesLiveData.value?.find { it.id == noteId }
    }
}