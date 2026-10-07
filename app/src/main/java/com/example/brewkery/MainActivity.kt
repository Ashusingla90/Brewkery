package com.example.brewkery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.brewkery.presentation.navigation.BrewNavGraph
import com.example.brewkery.ui.theme.BrewkeryTheme
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Color as AndroidColor
import androidx.activity.SystemBarStyle
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
        )
        setContent {
            BrewkeryTheme {
                BrewNavGraph()
            }
        }
    }
}