package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteViewModel

@Composable
fun NavigationGraph(
    viewModel: NoteViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues,
    currentView: String,
    sortOption: String,
    editingNote: NoteData?,
    onEditingNoteChange: (NoteData?) -> Unit,
    onSearch: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "noteList",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("noteList") {
            NoteList(
                notes = viewModel.notesLiveData,
                onDoneChange = {  note -> viewModel.toggleDone(note) },
                onDeleteClick = { note -> viewModel.moveNoteToTrash(note) },
                onEditClick = { note ->
                    onEditingNoteChange(note)
                    navController.navigate("noteEdit")
                },
                onToggleStar = { note -> viewModel.toggleStar(note) },
                currentView = currentView,
                sortOption = sortOption
            )
        }
        composable("noteEdit") {
            NoteEditScreen(
                note = editingNote,
                onConfirm = { updatedNote ->
                    if (editingNote == null) {
                        viewModel.addNote(updatedNote.title, updatedNote.content, updatedNote.backgroundColor)
                    } else {
                        viewModel.updateNote(updatedNote)
                    }
                    navController.popBackStack()
                    onEditingNoteChange(null)
                },
                onNavigateBack = {
                    navController.popBackStack()
                    onEditingNoteChange(null)
                }
            )
        }
        composable("userData") {
            UserDataScreen()
        }
        composable("profile") {
            ProfileScreen()
        }
        composable("trash") {
            TrashScreen(
                deletedNotes = viewModel.deleteNotesLiveData.collectAsState().value,
                onRestoreClick = { note -> viewModel.restoreNote(note) },
                onRestoreAll = { viewModel.restoreAllNotes() },
                onDeleteAll = { viewModel.deleteAllTrash() },
                onViewChange = { currentView -> viewModel.setView(currentView) },
                currentView = viewModel.currentView.collectAsState().value
            )
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("search") {
            SearchPage(
                notesFlow = viewModel.notesLiveData,
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery -> searchQuery = newQuery },
                onSearch = onSearch,
                onDoneChange = { note -> viewModel.toggleDone(note) },
                onDeleteClick = { note -> viewModel.moveNoteToTrash(note) },
                onEditClick = { note ->
                    onEditingNoteChange(note)
                    navController.navigate("noteEdit")
                },
                onToggleStar = { note -> viewModel.toggleStar(note) }
            )
        }
    }
}
