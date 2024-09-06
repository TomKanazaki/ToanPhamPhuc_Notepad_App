package com.example.toanphamphuc_notepad_app.main_events

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

//importing represented icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack //to go back
import androidx.compose.material.icons.automirrored.filled.Sort //sort icon
import androidx.compose.material.icons.automirrored.filled.ViewList //list icon
import androidx.compose.material.icons.filled.AccountCircle //circled account icon
import androidx.compose.material.icons.filled.AddCircle //circled add icon
import androidx.compose.material.icons.filled.Delete //delete icon
import androidx.compose.material.icons.filled.GridView //grid icon
import androidx.compose.material.icons.filled.Person //profile icon
import androidx.compose.material.icons.filled.Search //search icon

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
    //var searchQuery by remember { mutableStateOf("") }
    var currentView by remember { mutableStateOf("list") } //or grid
    var sortOption by remember { mutableStateOf("name_asc") } //or name_des

    //navigation variables
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //menu variables
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

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

                        IconButton(onClick = { showOptionsMenu = !showOptionsMenu }) {
                            Icon(
                                imageVector = if (currentView == "grid") Icons.AutoMirrored.Filled.ViewList else Icons.Filled.GridView,
                                contentDescription = if (currentView == "grid") "View by List" else "View by Grid"
                            )
                        }

                        //view options button
                        if (showOptionsMenu) {
                            DropdownMenu(
                                expanded = showOptionsMenu,
                                onDismissRequest = { showOptionsMenu = false },
                                modifier = Modifier.width(IntrinsicSize.Min)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        currentView = "list"
                                        showOptionsMenu = false // Hide menu after selection
                                    },
                                    text = { Text("List") }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        currentView = "grid"
                                        showOptionsMenu = false // Hide menu after selection
                                    },
                                    text = { Text("Grid") }
                                )
                            }
                        }

                        IconButton(onClick = { showSortMenu = !showSortMenu }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                        }

                        //sort button
                        if (showSortMenu) {
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false },
                                modifier = Modifier.width(IntrinsicSize.Min)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        sortOption = "name_asc"
                                        showSortMenu = false
                                    },
                                    text = { Text("Name A-Z") }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        sortOption = "name_desc"
                                        showSortMenu = false
                                    },
                                    text = { Text("Name Z-A") }
                                )
                            }
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
        NavigationGraph(
            viewModel = viewModel,
            navController = navController,
            paddingValues = paddingValues,
//            onSearchQueryChanged = { searchQuery = it },
//            showSearchPage = showSearchPage,
//            onHideSearchPage = { showSearchPage = false },
            currentView = currentView,
            onCurrentViewChange = { currentView = it },
            sortOption = sortOption,
            onSortOptionChange = { sortOption = it },
            editingNote = editingNote,
            onEditingNoteChange = { editingNote = it }
        )
    }
}

@Composable
fun NavigationGraph(
    viewModel: NoteViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues,
//    onSearchQueryChanged: (String) -> Unit,
//    showSearchPage: Boolean,
//    onHideSearchPage: () -> Unit,
    currentView: String,
    onCurrentViewChange: (String) -> Unit,
    sortOption: String,
    onSortOptionChange: (String) -> Unit,
    editingNote: NoteData?,
    onEditingNoteChange: (NoteData?) -> Unit
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
                onCurrentViewChange = onCurrentViewChange,
                sortOption = sortOption,
                onSortOptionChange = onSortOptionChange
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
            UserDataScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("trash") {
            TrashScreen(
                deletedNotes = viewModel.deleteNotesLiveData.collectAsState().value,
                onRestoreClick = { note -> viewModel.restoreNote(note) },
                navController = navController
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Data") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        // Content of the UserDataScreen
        Text("User Data Screen", modifier = Modifier.padding(16.dp))
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    deletedNotes: List<NoteData>,
    onRestoreClick: (NoteData) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trash") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        // Content of the TrashScreen
        Text("Trash Screen", modifier = Modifier.padding(16.dp))
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        // Content of the ProfileScreen
        Text("Profile Screen", modifier = Modifier.padding(16.dp))
    }
}









