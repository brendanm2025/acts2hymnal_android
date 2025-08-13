package com.acts2.acts2hymnal

sealed class Screen(val route: String) {
    data object HymnList : Screen("hymn_list")
    data object Song : Screen("song/{id}") {
        fun createRoute(id: Int) = "song/$id"
    }
    data object Settings : Screen("settings")
}