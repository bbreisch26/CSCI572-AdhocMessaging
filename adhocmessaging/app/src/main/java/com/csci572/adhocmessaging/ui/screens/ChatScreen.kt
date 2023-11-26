package com.csci572.adhocmessaging.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.csci572.adhocmessaging.MainActivity
import com.csci572.adhocmessaging.ui.components.ChatBar
import com.csci572.adhocmessaging.ui.components.Message
import com.csci572.adhocmessaging.ui.components.MessageContent
import com.csci572.adhocmessaging.ui.components.NavBar

//Experimental tag needed for Scaffold (apparently)
@ExperimentalMaterial3Api
@Composable
fun ChatScreen(navController: NavController, userId: String, activity: MainActivity) {

    Scaffold(
        topBar = {
            NavBar(navController, userId)
        },
        bottomBar = {
            ChatBar(userId, activity)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        innerPadding ->
        Row(modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp)) {
            Message(MessageContent(false, "Lorem Ipsum"))
        }
    }
}