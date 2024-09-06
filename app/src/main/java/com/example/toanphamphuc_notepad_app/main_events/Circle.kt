package com.example.toanphamphuc_notepad_app.main_events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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