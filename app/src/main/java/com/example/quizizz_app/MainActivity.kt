package com.example.quizizz_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quizizz_app.navigation.ScreenNavigation
import com.example.quizizz_app.ui.theme.Quizizz_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Quizizz_AppTheme {
                ScreenNavigation()
            }
        }
    }
}