package com.csci572.adhocmessaging.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.csci572.adhocmessaging.MainActivity
import com.csci572.adhocmessaging.ui.components.NavBar
import com.csci572.adhocmessaging.ui.components.User
import com.csci572.adhocmessaging.ui.components.UserCard
import com.csci572.adhocmessaging.ui.theme.Gray80

@ExperimentalMaterial3Api
@Composable
fun UserScreen(navController: NavController, activity: MainActivity) {
    Scaffold(
        topBar = {
            NavBar(navController, "Nearby Users")
        },
        containerColor = Gray80
    ) {
            innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)) {
            //for each nearby user

//            activity.peerList?.deviceList?.forEach {
//                UserCard(navController, User(it.deviceName, it.deviceAddress))
//            }

            activity.servicePeers?.forEach {
                UserCard(navController, User(it.value, it.key))
            }
            UserCard(navController, User("beep", "boop"))
        }
    }
}