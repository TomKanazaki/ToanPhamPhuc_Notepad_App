package com.example.toanphamphuc_notepad_app.viewModel_Data

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteViewModel : ViewModel() {
    private val _notesLiveData = MutableStateFlow<List<NoteData>>(emptyList()) //holds the list of active note
    val notesLiveData: StateFlow<List<NoteData>> = _notesLiveData.asStateFlow() //exposes the active notes list

    private val _deleteNotesLiveData = MutableStateFlow<List<NoteData>>(emptyList()) //holds the list of deleted notes
    val deleteNotesLiveData: StateFlow<List<NoteData>> = _deleteNotesLiveData.asStateFlow() //exposes the deleted notes list

    private var nextId = 1 //keeps track of the next available note ID

    private val _currentView = MutableStateFlow("list") // Default view
    val currentView: StateFlow<String> = _currentView.asStateFlow()

    fun setView(view: String) {
        _currentView.value = view
    }

    //METHODS
    //adds a new note to the list of active notes
    fun addNote(title: String, content: String, backgroundColor: Color) {
        val newNote = NoteData(id = nextId++, title = title, content = content, backgroundColor = backgroundColor)
        _notesLiveData.value += newNote
    }

    //updates an existing note in the list of active notes
    fun updateNote(updatedNote: NoteData) {
        _notesLiveData.value = _notesLiveData.value.map { if (it.id == updatedNote.id) updatedNote else it }
    }

    //marks a note as done or undone
    fun toggleDone(note: NoteData) {
        _notesLiveData.value = _notesLiveData.value.map { if (it.id == note.id) { it.copy(isDone = !it.isDone) } else it }
    }

    fun moveNoteToTrash(note: NoteData) {
        deleteNote(note.id)
    }

    //restore deleted note from the list of deleted notes back to the list of active notes
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

    fun restoreAllNotes() {
        _notesLiveData.value += _deleteNotesLiveData.value
        _deleteNotesLiveData.value = emptyList()
    }

    fun toggleStar(note: NoteData) {
        _notesLiveData.value = _notesLiveData.value.map { if (it.id == note.id) { it.copy(isStarred = !it.isStarred) } else it }
    }
}
