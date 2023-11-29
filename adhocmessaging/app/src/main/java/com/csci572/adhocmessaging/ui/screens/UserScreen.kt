package com.csci572.adhocmessaging.ui.screens

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.csci572.adhocmessaging.MainActivity
import com.csci572.adhocmessaging.MyWifiP2PApp
import com.csci572.adhocmessaging.ui.components.NavBar
import com.csci572.adhocmessaging.ui.components.User
import com.csci572.adhocmessaging.ui.components.UserCard
import com.csci572.adhocmessaging.ui.theme.Gray80

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterial3Api
@Composable
fun UserScreen(navController: NavController, activity: MainActivity, userName: String) {
    if(activity.p2papp == null) {
        activity.p2papp = MyWifiP2PApp()
        activity.p2papp!!.setup(activity.applicationContext, userName)
    }

    val peerList = remember { activity.p2papp!!.peerList }

    Scaffold(
        topBar = {
            NavBar(navController, "Nearby Users", false)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
            innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)) {

            peerList?.deviceList?.forEach {
                val mac = it.deviceAddress
                if (activity.p2papp!!.servicePeerList.containsKey(mac)) {
                    val peerName = activity.p2papp!!.servicePeerList[mac]
                    UserCard(navController, User(peerName!!, mac), activity.p2papp!!)
                }
            }
        }
    }
}