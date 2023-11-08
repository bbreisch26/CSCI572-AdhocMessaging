package com.csci572.adhocmessaging.ui.screens

import com.csci572.adhocmessaging.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.navigation.NavController
import com.csci572.adhocmessaging.ui.components.Login

// Outermost container for all of our components
@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(Gray80)
            .fillMaxSize()
    ) {
        Row {
            Login(navController = navController)
        }
    }
}