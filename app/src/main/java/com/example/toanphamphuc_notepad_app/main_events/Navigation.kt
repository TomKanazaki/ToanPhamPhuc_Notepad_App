package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onDismissSearch: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "noteList",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("noteList") {
            NoteList(
                notes = viewModel.notesLiveData,
                onDoneChange = { viewModel.updateNote(it) },
                onDeleteClick = { viewModel.moveNoteToTrash(it) },
                onEditClick = { note ->
                    onEditingNoteChange(note)
                    navController.navigate("noteEdit")
                },
                currentView = currentView,
                sortOption = sortOption,
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
            )
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("search") {
            SearchPage(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearch = onSearch,
                onDismiss = onDismissSearch,
            )
        }
    }
}
