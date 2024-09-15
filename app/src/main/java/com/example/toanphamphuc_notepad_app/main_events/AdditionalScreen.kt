package com.example.toanphamphuc_notepad_app.main_events

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData
import androidx.compose.ui.Modifier


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserDataScreen() {
    Scaffold {
        //TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrashScreen(
    deletedNotes: List<NoteData>,
    onRestoreClick: (NoteData) -> Unit,
    onRestoreAll: () -> Unit,
    onDeleteAll: () -> Unit,
    onViewChange: (String) -> Unit,
    currentView: String
) {
    var showViewMenu by remember { mutableStateOf(false) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trash") },
                actions = {
                    // More options menu icon
                    IconButton(onClick = { showViewMenu = !showViewMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                    // More options menu dropdown
                    DropdownMenu(
                        expanded = showViewMenu,
                        onDismissRequest = { showViewMenu = false }
                    ) {
                        DropdownMenuItem(
                            onClick = { onViewChange("list") },
                            text = { Text("List View") }
                        )
                        DropdownMenuItem(
                            onClick = { onViewChange("grid") },
                            text = { Text("Grid View") }
                        )
                        DropdownMenuItem(
                            onClick = { onRestoreAll() },
                            text = { Text("Restore All") }
                        )
                        DropdownMenuItem(
                            onClick = { onDeleteAll() },
                            text = { Text("Delete All") }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Content of Trash Screen
        if (currentView == "list") {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {
                items(deletedNotes) { note ->
                    TrashNoteItem(
                        note = note,
                        onRestoreClick = { onRestoreClick(note) }
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {
                items(deletedNotes) { note ->
                    TrashNoteItem(
                        note = note,
                        onRestoreClick = { onRestoreClick(note) }
                    )
                }
            }
        }

        // Show delete all dialog
        if (showDeleteAllDialog) { //TODO
            AlertDialog(
                onDismissRequest = { showDeleteAllDialog = false },
                title = { Text("Delete All Notes Permanently") },
                text = { Text("Are you sure you want to delete all notes permanently?") },
                confirmButton = {
                    Button(onClick = {
                        onDeleteAll()
                        showDeleteAllDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteAllDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun TrashNoteItem(
    note: NoteData,
    onRestoreClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = note.title.ifBlank { "No title" },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.content.ifBlank { "No content" }.take(100) + if(note.content.length > 100) "..." else "",
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRestoreClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Restore")
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    Scaffold {
        //TODO()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen() {
    Scaffold {
        //TODO()
    }
}

