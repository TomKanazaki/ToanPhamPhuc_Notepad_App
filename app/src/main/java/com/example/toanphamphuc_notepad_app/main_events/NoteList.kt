package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun sortNotes(notes: List<NoteData>, sortOption: String): List<NoteData> {
    return when (sortOption) {
        "name_asc" -> notes
            .sortedWith(compareByDescending<NoteData> { it.isStarred }
                .thenBy { it.title })

        "name_desc" -> notes
            .sortedWith(compareByDescending<NoteData> { it.isStarred }
                .thenByDescending { it.title })

        else -> notes.sortedByDescending { it.isStarred }
    }
}

// Displays a list of notes, allowing users to interact with them (mark as done, delete, edit)
@Composable
fun NoteList(
    notes: StateFlow<List<NoteData>>,
    onDoneChange: (NoteData) -> Unit,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit,
    onToggleStar: (NoteData) -> Unit,
    currentView: String,
    sortOption: String
) {
    val currentNotes by notes.collectAsState()

    val sortedNotes = sortNotes(currentNotes, sortOption)

    Column(modifier = Modifier.fillMaxSize()) {
        // View toggle
        if (currentView == "list") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(sortedNotes) { note ->
                    NoteItem(
                        note = note,
                        onDoneChange = onDoneChange,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick,
                        onToggleStar = onToggleStar
                    )
                }
            }
        } else {
            // Grid view
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp)
            ) {
                items(sortedNotes) { note ->
                    NoteGridItem(
                        note = note,
                        onDoneChange = onDoneChange,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick,
                        onToggleStar = onToggleStar
                    )
                }
            }
        }

    }
}


@Composable
fun NoteItem(
    note: NoteData,
    onDoneChange: (NoteData) -> Unit,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit,
    onToggleStar: (NoteData) -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = note.backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = note.title.ifBlank { "No title" },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = note.content.ifBlank { "No content" }.take(15) + if(note.content.length>15) "..." else "",
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(onClick = { onDoneChange(note) }) {
                    Icon(
                        imageVector = if (note.isDone) Icons.Filled.CheckCircle else Icons.Filled.AddCircle,
                        contentDescription = if (note.isDone) "Mark as Undone" else "Mark as Done",
                        tint = if (note.isDone) MaterialTheme.colorScheme.primary else LocalContentColor.current.copy(alpha = 0.3f)
                    )
                }

                IconButton(onClick = { onEditClick(note) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Note")
                }

                IconButton(onClick = { showDeleteConfirmation = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                }

                IconButton(onClick = { onToggleStar(note) }) {
                    Icon(
                        imageVector = if (note.isStarred) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = if (note.isStarred) "Unstar" else "Star"
                    )
                }
            }
        }

        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete '${note.title}'?") },
                dismissButton = {
                    Button(onClick = { showDeleteConfirmation = false }) {
                        Text("No")
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        onDeleteClick(note)
                        showDeleteConfirmation = false
                    }) {
                        Text("Yes")
                    }
                }
            )
        }
    }
}