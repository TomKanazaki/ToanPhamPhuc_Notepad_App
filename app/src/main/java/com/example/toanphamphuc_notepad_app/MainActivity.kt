package com.example.toanphamphuc_notepad_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           Note()
        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Note() {
    val viewModel: NoteViewModel = viewModel()
    val notes = viewModel.notesLiveData.observeAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<NoteData?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingNote = null //create new note
                showDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) {
        NoteList(
            notes = notes.value,
            onDoneChange = { note ->
                viewModel.updateNote(note.copy(isDone = !note.isDone))
            },
            onDeleteClick = { note ->
                viewModel.deleteNote(note.id)
            },
            onEditClick = { note ->
                editingNote = note
                showDialog = true
            }
        )
    }

    if (showDialog) {
        NoteManager(
            note = editingNote?:NoteData(0, "", "", false),
            onDismiss = { showDialog = false },
            onConfirm = { title, content ->
                if (editingNote == null) {
                    viewModel.addNote(title, content)
                } else {
                    editingNote?.let {
                        viewModel.updateNote(it.copy(title = title, content = content))
                    }
                }
                editingNote = null
                showDialog = false
            }
        )
    }
}


@Composable
fun NoteList(
    notes: List<NoteData>,
    onDoneChange: (NoteData) -> Unit ,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit
) {
    LazyColumn {
        items(notes) {note->
            NoteItem(note = note, onDoneChange = onDoneChange, onDeleteClick = onDeleteClick, onEditClick = onEditClick)}
    }

}

@Composable
fun NoteItem(
    note: NoteData,
    onDoneChange: (NoteData) -> Unit,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit
) {
    var showDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {onDoneChange(note)}) {
                Icon(
                    imageVector = if(note.isDone) Icons.Filled.CheckCircle else Icons.Default.AddCircle,
                    contentDescription = if (note.isDone) "Mark as Undone" else "Mark as Done",
                    tint = if (note.isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }

            IconButton(onClick = {onEditClick(note) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Note")
            }

            IconButton(onClick = {showDeleteConfirmation = true}) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
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

@Composable
fun NoteManager(
    note: NoteData,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text( if (note.id == 0) "Edit Note" else "Add Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {title = it},
                    label = {Text("Title")}
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, content) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Note()
}