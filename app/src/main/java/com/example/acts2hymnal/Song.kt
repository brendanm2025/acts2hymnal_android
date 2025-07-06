@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.acts2hymnal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.compose.material3.Icon
import androidx.navigation.compose.rememberNavController


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Song(id: Int,
         modifier: Modifier = Modifier,
         navController: NavHostController
) {
    Scaffold(
        topBar = {
            SongAppBar(id, navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Lyrics for hymn go here")
        }
    }
}


@Composable
fun SongAppBar(
    id: Int,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    TopAppBar(
        title = { Text(text= "Hymn $id Title", fontSize = 24.sp)},
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
        }
    )
}

