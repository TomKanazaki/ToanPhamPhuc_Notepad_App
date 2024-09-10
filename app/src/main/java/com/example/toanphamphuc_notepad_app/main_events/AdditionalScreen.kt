package com.example.toanphamphuc_notepad_app.main_events

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserDataScreen() {
    Scaffold {
        //TODO()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrashScreen(
    deletedNotes: List<NoteData>,
    onRestoreClick: (NoteData) -> Unit,
) {
    Scaffold {
        //TODO()
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    Scaffold {
        //TODO()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen() {
    Scaffold {
        //TODO()
    }
}

