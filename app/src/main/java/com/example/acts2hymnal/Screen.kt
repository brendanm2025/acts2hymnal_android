package com.example.acts2hymnal

sealed class Screen(val route: String) {
    data object HymnList : Screen("hymn_list")
    data object Song : Screen("song/{id}") {
        fun createRoute(id: Int) = "song/$id"
    }
}