package com.csci572.adhocmessaging.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.csci572.adhocmessaging.ui.components.Message
import com.csci572.adhocmessaging.ui.components.MessageContent
import com.csci572.adhocmessaging.ui.components.NavBar

//Experimental tag needed for Scaffold (apparently)
@ExperimentalMaterial3Api
@Composable
fun ChatScreen(navController: NavController, userId: String) {
    Scaffold(
        topBar = {
            NavBar(userId)
        }
    ) {
        innerPadding ->
        Row(modifier = Modifier
            .padding(innerPadding)) {
            Message(MessageContent(true, "Lorem Ipsum"))
        }
    }
}