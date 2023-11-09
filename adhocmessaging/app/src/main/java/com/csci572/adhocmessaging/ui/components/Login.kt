package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@ExperimentalMaterial3Api
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
                nameTextField()
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
@ExperimentalMaterial3Api
@Composable
fun nameTextField() {
    var value by remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = { newText ->
            value = newText
        }
    )
}