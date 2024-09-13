package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//This composable is responsible for displaying a color circle with an optional selection indicator.
//
@Composable
fun ColorCircle(color: Color, isSelected: Boolean, onColorSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color, shape = CircleShape)
            .clickable { onColorSelected() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}