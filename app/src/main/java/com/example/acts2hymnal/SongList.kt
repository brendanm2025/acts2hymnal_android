package com.example.acts2hymnal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HymnalApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.HymnList.route) {
        composable(Screen.HymnList.route) {
            HymnScreen(navController)
        }
        composable(Screen.Song.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                Song(id = id, navController = navController)
            }
        }
    }
}

@Composable
fun HymnScreen(
    navController: NavHostController = rememberNavController()
)  {
    Column {

        // Menu Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            IconButton(
                onClick = {/* Handle click */ },
                modifier = Modifier.align(Alignment.CenterEnd).padding(start = 16.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }

        // Search Bar
        SearchBar()

        // Scrollable List
        HymnScroll(navController)

    }
}

@Composable
fun HymnRow(itemIdx: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(24.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // First item: name
        Text(text = "Item #$itemIdx", modifier = Modifier.weight(1f))

        // TODO: implement Christmas and melody icons

        // Last item: side arrow
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun SearchBar() {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { query = it },
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = Color.White,
//            textColor = Color.Black
//        )
        placeholder = {
            Text(
                text = "Search",
                style = TextStyle(fontSize = 16.sp)
            )
        },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = ""} ) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(48.dp)
    )
}

// Function: Hymn Scroll
// Description: generates the scrollable, clickable list of hymn titles
//
@Composable
fun HymnScroll(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(100) { index ->
            HymnRow(index) {
                navController.navigate(Screen.Song.createRoute(index))
            }
        }
    }
}