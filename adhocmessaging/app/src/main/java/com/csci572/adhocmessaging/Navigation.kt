package com.csci572.adhocmessaging

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.csci572.adhocmessaging.ui.screens.ChatScreen
import com.csci572.adhocmessaging.ui.screens.HomeScreen
import com.csci572.adhocmessaging.ui.screens.UserScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable(
            route = "HomeScreen") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "UserScreen") {
            UserScreen(navController = navController)
        }
        composable(
            route = "ChatScreen"
            //TODO: add args here to fetch user chat history
        ) {
            ChatScreen(navController = navController)
        }
    }
}