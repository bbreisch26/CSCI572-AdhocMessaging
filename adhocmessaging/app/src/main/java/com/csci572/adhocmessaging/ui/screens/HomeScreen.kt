package com.csci572.adhocmessaging.ui.screens

import com.csci572.adhocmessaging.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.csci572.adhocmessaging.ui.components.Login

// Outermost container for all of our components
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(Gray80)
            .fillMaxSize(),
        contentAlignment = Alignment.CenterStart,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                //Logo
                Text("LOGO HERE")
            }
            Row(modifier = Modifier
                .weight(1f, false)) {
                Login(navController = navController)
            }
        }

    }
}