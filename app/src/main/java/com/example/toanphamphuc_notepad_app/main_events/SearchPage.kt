package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData
import kotlinx.coroutines.flow.StateFlow

//This composable is responsible for displaying a search page with a search bar and search icon.
@Composable
fun SearchPage(
    notesFlow: StateFlow<List<NoteData>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onDoneChange: (NoteData) -> Unit,
    onDeleteClick: (NoteData) -> Unit,
    onEditClick: (NoteData) -> Unit,
    onToggleStar: (NoteData) -> Unit,
) {

    val notes = notesFlow.collectAsState().value
    val filteredNotes = notes.filter { it.title.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search for a note") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = onSearch) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search result label
        if (searchQuery.isNotEmpty()) {
            if (filteredNotes.isNotEmpty()) {
                Text(
                    text = "Search results for: $searchQuery",
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                Text(
                    text = "No note was found, check your title.",
                    modifier = Modifier.padding(8.dp),
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search results list
        if (filteredNotes.isNotEmpty()) {
            LazyColumn {
                items(filteredNotes) { note ->
                    // Reuse the NoteItem composable used on the main screen
                    NoteItem(
                        note = note,
                        onDoneChange = onDoneChange,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick,
                        onToggleStar = onToggleStar
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }
}
