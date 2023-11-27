package com.csci572.adhocmessaging

import android.Manifest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.csci572.adhocmessaging.ui.screens.ChatScreen
import com.csci572.adhocmessaging.ui.screens.HomeScreen
import com.csci572.adhocmessaging.ui.screens.PermissionScreen
import com.csci572.adhocmessaging.ui.screens.UserScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(activity: MainActivity) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "PermissionScreen") {
        composable(
           route = "PermissionScreen"
        ) {
            PermissionScreen(navController = navController)
        }
        composable(
            route = "HomeScreen") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "UserScreen/{name}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("name")
            if (userName != null) {
                UserScreen(navController = navController, activity, userName)

            }
        }
        composable(
            route = "ChatScreen/{userId}"
        ) {
            backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                ChatScreen(navController = navController, userId = userId, activity = activity)
            }
        }
    }
}