package com.example.toanphamphuc_notepad_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Note()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Note() {
    val viewModel: NoteViewModel = viewModel()
    val navController = rememberNavController()
    //var showDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<NoteData?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notepad") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingNote = null
                    navController.navigate("noteEdit")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add Note")
            }
        }
    ) {paddingValues ->
        NavHost(navController = navController, startDestination = "noteList", modifier = Modifier.padding(paddingValues)) {
            composable("noteList") {
                NoteList(
                    notes = viewModel.notesLiveData,
                    onDoneChange = { note -> viewModel.updateNote(note.copy(isDone = !note.isDone)) },
                    onDeleteClick = { note -> viewModel.deleteNote(note.id) },
                    onEditClick = { note ->
                        editingNote = note
                        navController.navigate("noteEdit")
                    }
                )
            }

            composable("noteEdit") {
                NoteEditScreen(
                    note = editingNote,
                    onConfirm = { updatedNote ->
                        if (editingNote == null) {viewModel.addNote(updatedNote.title, updatedNote.content)}
                        else {
                            viewModel.updateNote(updatedNote)
                        }
                    },
                    onNavigateBack = {navController.popBackStack()} //go back to the list
                )
            }
        }
    }

}


@Composable
fun NoteList(
    notes: List<NoteData>,
    onDoneChange: (NoteData) -> Unit ,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
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
                Text(text = note.title.ifBlank { "No title" }, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = note.content.ifBlank { "No content" }, fontWeight = FontWeight.Light, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {onDoneChange(note)}) {
                Icon(
                    imageVector = if(note.isDone) Icons.Filled.CheckCircle else Icons.Default.AddCircle,
                    contentDescription = if (note.isDone) "Mark as Undone" else "Mark as Done",
                    tint = if (note.isDone) MaterialTheme.colorScheme.primary else LocalContentColor.current.copy(alpha = 0.3f)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    note: NoteData?,
    onConfirm: (NoteData) -> Unit,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var selectedColor by remember { mutableStateOf(note?.backgroundColor ?: Color.White) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (note == null) "Add Note" else "Edit Note") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(selectedColor)
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                ColorCircle(color = Color.White, isSelected = selectedColor == Color.White) {
                    selectedColor = Color.White
                }
                ColorCircle(color = Color.Yellow, isSelected = selectedColor == Color.Yellow) {
                    selectedColor = Color.Yellow
                }
                ColorCircle(color = Color.Green, isSelected = selectedColor == Color.Green) {
                    selectedColor = Color.Green
                }
                ColorCircle(color = Color.Cyan, isSelected = selectedColor == Color.Cyan) {
                    selectedColor = Color.Cyan
                }
                ColorCircle(color = Color.Magenta, isSelected = selectedColor == Color.Magenta) {
                    selectedColor = Color.Magenta
                }
            }
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) //allow content to take up remaining space
                    .background(selectedColor)
            ) {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        val updatedNote = note?.copy(
                            title = title,
                            content = content,
                            backgroundColor = selectedColor
                        ) ?: NoteData(
                            title = title,
                            content = content,
                            isDone = false,
                            backgroundColor = selectedColor
                        )
                        onConfirm(updatedNote)
                        onNavigateBack()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Save")
                }
                //Cancel button
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun ColorCircle(color: Color, isSelected: Boolean, onColorSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color, shape = CircleShape)
            .clickable { onColorSelected() }
            .padding(4.dp)
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Black, shape = CircleShape)
            )
        }
    }
}

