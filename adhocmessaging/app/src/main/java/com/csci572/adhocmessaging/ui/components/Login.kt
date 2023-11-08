package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Login(navController: NavController) {
    Card(
        modifier = Modifier
            .padding(10.dp)
    ) {
        //TODO: Insert logo here
        //Image(bitmap = , contentDescription="Logo")
        Row() {
            Column {
                Text(
                    text = "Name: ",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .wrapContentHeight()
                )
                Text(
                    text = "MAC ADDRESS",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(10.dp)
                        .wrapContentHeight()
                )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom) {
            SignInButton(navigateUserScreen = { navController.navigate("UserScreen") })
        }
    }
}

@Composable
fun SignInButton(navigateUserScreen: () -> Unit) {
    Button(onClick = navigateUserScreen) {
        Text("Sign In")
    }
}