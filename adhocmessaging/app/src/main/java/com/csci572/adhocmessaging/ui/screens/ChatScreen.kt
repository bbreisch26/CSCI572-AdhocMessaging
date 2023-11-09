package com.csci572.adhocmessaging.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.csci572.adhocmessaging.ui.components.NavBar

//Experimental tag needed for Scaffold (apparently)
@ExperimentalMaterial3Api
@Composable
fun ChatScreen(navController: NavController) {
    Scaffold(
        topBar = {
            NavBar("placeholder")
        }
    ) {
       innerPadding ->
       Column(modifier = Modifier
           .padding(innerPadding)) {

       }
    }
}