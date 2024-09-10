package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//This composable is responsible for displaying a search page with a search bar and search icon.
@Composable
fun SearchPage(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onDismiss: () -> Unit,
) {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ){
        //search bar
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

        //search result:
        Text(text = "Search results for: $searchQuery", modifier = Modifier.padding(8.dp))

        //search icon
        IconButton(onClick = onDismiss) {
            Icon(Icons.Filled.Search, contentDescription = "search")
        }
    }
}