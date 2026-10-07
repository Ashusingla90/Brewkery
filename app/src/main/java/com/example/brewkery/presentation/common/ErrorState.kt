package com.example.brewkery.presentation.common


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.brewkery.ui.theme.BrewAccent
import com.example.brewkery.ui.theme.BrewInk

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(message, color = BrewInk, textAlign = TextAlign.Center)
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = BrewAccent)
        ) { Text("Retry") }
    }
}