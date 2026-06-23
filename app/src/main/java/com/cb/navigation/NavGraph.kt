package com.cb.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Need : Screen("need")
    object Post : Screen("post")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
}
