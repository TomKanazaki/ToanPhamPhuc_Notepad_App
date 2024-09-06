package com.example.toanphamphuc_notepad_app.main_events

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteViewModel

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
    var editingNote by remember { mutableStateOf<NoteData?>(null) }

    var showSearchPage by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var currentView by remember { mutableStateOf("list") } //or grid
    var sortOption by remember { mutableStateOf("name_asc") } //or name_des

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Super Note") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            navController.navigate("userData")
                        }) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "User Data")
                        }
                        IconButton(onClick = {
                            navController.navigate("trash")
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Trash")
                        }
                        IconButton(onClick = {
                            navController.navigate("profile")
                        }) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile")
                        }
                        IconButton(onClick = { showSearchPage = true }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentRoute == "noteList") { //only show the + (add button) when at the notes' list page
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
        }
    ) {paddingValues ->
        if (showSearchPage) {
            SearchPage(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearch = {
                    showSearchPage = false
                },
                onDismiss = { showSearchPage = false }
            )
        } else {
            NavHost(navController = navController, startDestination = "noteList", modifier = Modifier.padding(paddingValues)) {
                composable("noteList") {
                    NoteList(
                        notes = viewModel.notesLiveData,
                        onDoneChange = { note -> viewModel.updateNote(note.copy(isDone = !note.isDone)) },
                        onDeleteClick = { note -> viewModel.deleteNote(note.id) },
                        onEditClick = { note ->
                            editingNote = note
                            navController.navigate("noteEdit")
                        },
                        currentView = currentView,
                        onCurrentViewChange = { currentView = it },
                        sortOption = sortOption,
                        onSortOptionChange = { sortOption = it }

                    )
                }

                composable("noteEdit") {
                    NoteEditScreen(
                        note = editingNote,
                        onConfirm = { updatedNote ->
                            if (editingNote == null) {
                                viewModel.addNote(updatedNote.title, updatedNote.content, updatedNote.backgroundColor)
                            }
                            else {
                                viewModel.updateNote(updatedNote)
                            }
                            navController.popBackStack()
                        },
                        onNavigateBack = {navController.popBackStack()} //go back to the list
                    )
                }

                composable("userData") {
                    UserDataScreen()

                }

                composable("trash") {
//                    TrashScreen(deletedNotes = viewModel.deleteNotesLiveData())
//                    onRestoreClick = { note -> viewModel.restoreNote(note) }
                }

                composable("profile") {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun UserDataScreen() {
    Text("User Data Screen")
}

@Composable
fun TrashScreen(deletedNotes: List<NoteData>, onRestoreClick: (NoteData) -> Unit) {
    // Display deleted notes and a restore button for each
    // You'll likely want to use a LazyColumn here to display the deleted notes
    Text("Trash Screen")
}

@Composable
fun ProfileScreen() {
    Text("Profile Screen")
}









