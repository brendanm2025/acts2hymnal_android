package com.example.acts2hymnal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.acts2hymnal.ui.theme.Acts2HymnalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val songList = readAllSongs(this@MainActivity)
        setContent {
            Acts2HymnalTheme {
                HymnalApp(songList=songList, context=this@MainActivity)
            }
        }
    }
}