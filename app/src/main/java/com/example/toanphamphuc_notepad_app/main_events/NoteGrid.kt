package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData

//This composable is responsible for displaying a list of notes in a grid view.
@Composable
fun NoteGrid() {

}
@Composable
fun NoteGridItem(
    note: NoteData,
    onDoneChange: (NoteData) -> Unit,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        color = note.backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title.ifBlank { "No title" },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = note.content.ifBlank { "No content" },
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                }
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


