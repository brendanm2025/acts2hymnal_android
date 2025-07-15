package com.example.acts2hymnal.ui.theme


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.acts2hymnal.HymnScreen
import com.example.acts2hymnal.SongData
import androidx.navigation.compose.rememberNavController

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HymnScreenPreview() {
    val mockSongs = listOf(
        SongData("Amazing Grace"),
        SongData("How Great Thou Art"),
        SongData("Be Thou My Vision")
    )
    val navController = rememberNavController()
    HymnScreen(songList = mockSongs, navController = navController)
}
