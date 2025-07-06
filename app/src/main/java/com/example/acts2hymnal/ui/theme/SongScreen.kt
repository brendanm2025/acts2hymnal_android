package com.example.acts2hymnal.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.acts2hymnal.Song

@Preview
@Composable
fun SongView() {
    Song(1, navController = rememberNavController())
}