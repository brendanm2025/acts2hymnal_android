package com.acts2.acts2hymnal

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.res.painterResource
import com.acts2.acts2hymnal.R

// this defines the main view of the app, i.e. the scrollable list screen
@Composable
fun HymnalApp(
    navController: NavHostController = rememberNavController(),
    songList: List<SongData>,
    context: Context
) {
    NavHost(navController = navController, startDestination = Screen.HymnList.route) {
        composable(Screen.HymnList.route) {
            HymnScreen(songList = songList, navController = navController)
        }
        composable(Screen.Song.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                val song = songList.find {it.id == id}
                if (song != null) {
                    SongScreen(song = song, navController = navController, context = context)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnScreen(
    songList: List<SongData>,
    navController: NavHostController = rememberNavController()
) {

    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(searchQuery, songList) {
        if (searchQuery.isEmpty()) {
            songList
        } else {
            songList.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                                }
                            }
                        }
                    )
                },
                actions = {
                    IconButton(onClick = { /* settings or menu */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Scrollable List
            HymnScroll(songList = filteredList, navController = navController)
        }
    }
}

// Defines a single row of the scrollable, clickable list
@Composable
fun HymnRow(item: SongData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(24.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // First item: name
        Text(text = item.name,
             modifier = Modifier.weight(1f),
             style = MaterialTheme.typography.bodyLarge,
             color = MaterialTheme.colorScheme.onBackground)

        if (item.christmas) {
            Icon(
                painter = painterResource(id = R.drawable.snow),
                contentDescription = "Christmas",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        if (item.audio_file != "") {
            Icon(
                painter = painterResource(id = R.drawable.music_note),
                contentDescription = "Audio File",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Last item: side arrow
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

// Function: Hymn Scroll
// Description: generates the scrollable, clickable list of hymn titles
//
@Composable
fun HymnScroll(songList: List<SongData>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(songList) { song ->
            HymnRow(song) {
                navController.navigate(Screen.Song.createRoute(song.id))
            }
        }
    }
}

