package com.jeet.androidappdemo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jeet.androidappdemo.presentation.userdetails.UserDetailScreen
import com.jeet.androidappdemo.presentation.users.UserScreen
import com.jeet.androidappdemo.presentation.users.UserViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.UserList.route
    ) {
        composable(Screen.UserList.route) {
            val userViewModel: UserViewModel = hiltViewModel()
            UserScreen(
                viewModel = userViewModel,
                onItemClick = { userId ->
                    navController.navigate(Screen.UserDetails.createRoute(userId))
                }
            )
        }

        composable(
            route = Screen.UserDetails.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) {

            val userViewModel: UserViewModel = hiltViewModel(
                viewModelStoreOwner = navController.getBackStackEntry(Screen.UserList.route)
            )
            UserDetailScreen(
                onBack = { navController.popBackStack() },
                userViewModel = userViewModel
            )
        }
    }
}