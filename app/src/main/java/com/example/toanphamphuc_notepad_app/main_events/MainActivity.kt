package com.example.toanphamphuc_notepad_app.main_events

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person //profile icon
import androidx.compose.material.icons.filled.Search //search icon
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BasicAlertDialog

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
    var currentView by remember { mutableStateOf("list") }
    //control sort option view: name_asc or name_desc
    var sortOption by remember { mutableStateOf("name_asc") }

    //navigation variables
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //menu variables
    val showMainOptionsMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                currentRoute = currentRoute ?: "noteList",
                navController = navController,
                showOptionsMenu = showMainOptionsMenu,
                currentView = currentView,
                sortOption = sortOption,
                onSortChange = { sortOption = it },
                onViewChange = { currentView = it }
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
            onSearch = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    currentRoute: String,
    navController: NavController,
    showOptionsMenu: Boolean,
    currentView: String,
    sortOption: String,
    onSortChange: (String) -> Unit,
    onViewChange: (String) -> Unit
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
    var showMoreOptionsMenu by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var showViewDialog by remember { mutableStateOf(false) }

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
            } else {
                //more account options
                IconButton(onClick = { showMoreOptionsMenu = !showMoreOptionsMenu }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = showMoreOptionsMenu,
                    onDismissRequest = { showMoreOptionsMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            navController.navigate("profile")
                            showMoreOptionsMenu = false
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
                            navController.navigate("trash")
                            showMoreOptionsMenu = false
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Delete, contentDescription = "Trash")
                                Spacer(Modifier.width(8.dp))
                                Text("Trash")
                            }
                        }
                    )

                    DropdownMenuItem(
                        onClick = {
                            navController.navigate("settings")
                            showMoreOptionsMenu = false
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
                            navController.navigate("userData")
                            showMoreOptionsMenu = false
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.AccountCircle, contentDescription = "User Data")
                                Spacer(Modifier.width(8.dp))
                                Text("User Data")
                            }
                        }
                    )
                }
            }
        },
        actions = {
            if (currentRoute != "search") {
                //search icon
                IconButton(onClick = {
                    navController.navigate("search")
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }

            //more options menu icon
            IconButton(onClick = { showMainOptionsMenu = !showMainOptionsMenu }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options")
            }

            //main options menu dropdown
            DropdownMenu(
                expanded = showMainOptionsMenu,
                onDismissRequest = { showMainOptionsMenu = false }
            ) {
                //view options button
                DropdownMenuItem(
                    onClick = { showViewDialog = true },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (currentView == "grid") Icons.Filled.GridView else Icons.AutoMirrored.Filled.ViewList,
                                contentDescription = if (currentView == "grid") "View by List" else "View by Grid"
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(text = if (currentView == "grid") "Grid View" else "List View")
                        }
                    }
                )
                //show the view dialog to change the view option
                if (showViewDialog) {
                    ViewDialog(
                        currentView = currentView,
                        onViewChange = { newView ->
                            onViewChange(newView)
                        },
                        onDismiss = { showViewDialog = false }
                    )
                }

                //sort options button
                DropdownMenuItem(
                    onClick = { showSortDialog = true },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                            Spacer(Modifier.width(4.dp))
                            Text(text = "Sort Method")
                        }
                    }
                )
                //show the sort dialog to change the sort option
                if (showSortDialog) {
                    SortDialog(
                        currentSortOption = sortOption,
                        onSortChange = { newSortOption ->
                            onSortChange(newSortOption)
                            showSortDialog = false
                        },
                        onDismiss = { showSortDialog = false }
                    )
                }

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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewDialog(
    currentView: String,
    onViewChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedView by remember { mutableStateOf(currentView) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth(),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            //view dialog title
            Text(
                text = "Choose View Method",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            //view options
            val viewOptions = mapOf(
                "list" to "List View",
                "grid" to "Grid View"
            )

            viewOptions.forEach { (key, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedView = key
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (key == selectedView),
                        onClick = {
                            selectedView = key
                        }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(label)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //dialog Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    onViewChange(selectedView)
                    onDismiss()
                }) {
                    Text("Done")
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
    currentSortOption: String,
    onSortChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSortOption by remember { mutableStateOf(currentSortOption) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth(),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            //sort dialog title
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            //sort options
            val sortOptions = mapOf(
                "name_asc" to "Name A-Z",
                "name_desc" to "Name Z-A"
            )

            sortOptions.forEach { (key, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSortOption = key }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (key == selectedSortOption),
                        onClick = { selectedSortOption = key }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(label)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //dialog buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("Cancel") //leave and remain the old setting
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    onSortChange(selectedSortOption)
                    onDismiss()
                }) {
                    Text("Done")
                }
            }
        }
    }
}





