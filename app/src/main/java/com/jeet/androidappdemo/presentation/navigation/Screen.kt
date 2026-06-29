package com.jeet.androidappdemo.presentation.navigation

sealed class Screen(val route: String) {
    data object UserList : Screen("user_list")
    data object UserDetails : Screen("user_detail/{userId}") {
        fun createRoute(userId: Int) = "user_detail/$userId"
    }
}