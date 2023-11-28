package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.csci572.adhocmessaging.MainActivity
import com.csci572.adhocmessaging.MyWifiP2PApp
import com.csci572.adhocmessaging.ui.theme.Blue80

data class User(val username: String, val MAC: String)
@ExperimentalMaterial3Api
@Composable
fun UserCard(navController : NavController, user: User, p2papp : MyWifiP2PApp) {
    Card(//elevation = CardDefaults.cardElevation(
        //defaultElevation = 6.dp, hoveredElevation = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blue80,
        ),
        modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        onClick = {
            p2papp.connectToPeer(user.MAC)
            navController.navigate("ChatScreen/{user}".replace(oldValue="{user}", newValue = user.username)) }
        ) {
            Text(
                text = user.username,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
    }
}