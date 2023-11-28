package com.csci572.adhocmessaging.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.csci572.adhocmessaging.MainActivity
import com.csci572.adhocmessaging.Message
import com.csci572.adhocmessaging.ui.components.ChatBar
import com.csci572.adhocmessaging.ui.components.Message
import com.csci572.adhocmessaging.ui.components.MessageContent
import com.csci572.adhocmessaging.ui.components.NavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//Experimental tag needed for Scaffold (apparently)
@ExperimentalMaterial3Api
@Composable
fun ChatScreen(navController: NavController, userId: String, activity: MainActivity) {
    var messageList = remember { mutableStateListOf<Message>() }
    val chats : LiveData<List<Message>>? = activity.p2papp?.messageDatabase?.messageDao()?.getMessagesForContact(userId)
    var scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    chats?.observe(activity, Observer { messages ->
        // Update your UI or perform any action with the list of messages
        messageList.clear()
        for (message in messages) {
            messageList.add(message)
        }
        coroutineScope.launch {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    })
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
        //messageDatabase?.messageDao()?.insert(Message(contactName=inputList[0], sourceIsMe = false, contents = inputList[1]))
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .verticalScroll(scrollState)
        ) {
            messageList.forEach {
                Row(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = (if(it.sourceIsMe) Arrangement.End else Arrangement.Start)
                    ) {
                    Message(MessageContent(it.sourceIsMe, it.contents))
                    }
            }


        }
    }
}