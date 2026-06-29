package com.jeet.androidappdemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jeet.androidappdemo.presentation.navigation.AppNavGraph
import com.jeet.androidappdemo.presentation.theme.AndroidAppDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppDemoTheme {
                AppNavGraph()
            }
        }
    }
}
