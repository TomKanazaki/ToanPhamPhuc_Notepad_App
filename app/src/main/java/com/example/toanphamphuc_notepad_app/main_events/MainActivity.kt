package com.example.toanphamphuc_notepad_app.main_events

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

//importing represented icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort //sort icon
import androidx.compose.material.icons.automirrored.filled.ViewList //list icon
import androidx.compose.material.icons.filled.AccountCircle //circled account icon
import androidx.compose.material.icons.filled.AddCircle //circled add icon
import androidx.compose.material.icons.filled.Delete //delete icon
import androidx.compose.material.icons.filled.GridView //grid icon
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person //profile icon
import androidx.compose.material.icons.filled.Search //search icon
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteListScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteListScreen() {
    val viewModel: NoteViewModel = viewModel()
    val navController = rememberNavController()

    //control edit page
    var editingNote by remember { mutableStateOf<NoteData?>(null) }
    //control view by list or grid
    val currentView by remember { mutableStateOf("list") }
    //control sort option view: name_asc or name_desc
    val sortOption by remember { mutableStateOf("name_asc") }

    //navigation variables
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //menu variables
    val showMainOptionsMenu by remember { mutableStateOf(false) }
    val showSortMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CustomTopAppBar(
                currentRoute = currentRoute ?: "noteList",
                navController = navController,
                showOptionsMenu = showMainOptionsMenu,
                showSortMenuParam = showSortMenu
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
            currentView = currentView,
            sortOption = sortOption,
            editingNote = editingNote,
            onEditingNoteChange = { editingNote = it },
            searchQuery = "",
            onSearchQueryChange = {},
            onSearch = {},
            onDismissSearch = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    currentRoute: String,
    navController: NavController,
    showOptionsMenu: Boolean,
    showSortMenuParam: Boolean
) {
    val title = when (currentRoute) {
        "userData" -> "User Data"
        "profile" -> "Profile"
        "settings" -> "Settings"
        "trash" -> "Trash"
        else -> "SNote"
    }

    // Control menu states
    var showMainOptionsMenu by remember { mutableStateOf(showOptionsMenu) }
    var showSortMenu by remember { mutableStateOf(showSortMenuParam) }
    var showViewMenu by remember { mutableStateOf(false) }
    var currentView by remember { mutableStateOf("list") }
    var sortOption by remember { mutableStateOf("name_asc") }

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (currentRoute != "noteList") {
                IconButton(onClick = {
                    navController.navigate("noteList") {
                        popUpTo("noteList") { inclusive = true }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (currentRoute != "search") {
                // Search icon
                IconButton(onClick = {
                    navController.navigate("search")
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }

            // More options menu
            IconButton(onClick = { showMainOptionsMenu = !showMainOptionsMenu }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options")
            }

            // Main options menu
            DropdownMenu(
                expanded = showMainOptionsMenu,
                onDismissRequest = { showMainOptionsMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        navController.navigate("userData")
                        showMainOptionsMenu = false
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "User Data")
                            Spacer(Modifier.width(8.dp))
                            Text("User Data")
                        }
                    }
                )

                DropdownMenuItem(
                    onClick = {
                        navController.navigate("profile")
                        showMainOptionsMenu = false
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile")
                            Spacer(Modifier.width(8.dp))
                            Text("Profile")
                        }
                    }
                )

                DropdownMenuItem(
                    onClick = {
                        navController.navigate("settings")
                        showMainOptionsMenu = false
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings")
                            Spacer(Modifier.width(8.dp))
                            Text("Settings")
                        }
                    }
                )

                DropdownMenuItem(
                    onClick = {
                        navController.navigate("trash")
                        showMainOptionsMenu = false
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Delete, contentDescription = "Trash")
                            Spacer(Modifier.width(8.dp))
                            Text("Trash")
                        }
                    }
                )
            }

            // View options button
            IconButton(onClick = { showViewMenu = !showViewMenu }) {
                Icon(
                    imageVector = if (currentView == "grid") Icons.Filled.GridView else Icons.AutoMirrored.Filled.ViewList,
                    contentDescription = if (currentView == "grid") "View by List" else "View by Grid"
                )
            }

            // View options menu
            DropdownMenu(
                expanded = showViewMenu,
                onDismissRequest = { showViewMenu = false },
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                DropdownMenuItem(
                    onClick = {
                        currentView = "list"
                        showViewMenu = false
                    },
                    text = { Text("List") }
                )
                DropdownMenuItem(
                    onClick = {
                        currentView = "grid"
                        showViewMenu = false
                    },
                    text = { Text("Grid") }
                )
            }

            // Sort options button
            IconButton(onClick = { showSortMenu = !showSortMenu }) {
                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
            }

            // Sort options menu
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
    )
}









