package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.csci572.adhocmessaging.ui.theme.Blue80
import com.csci572.adhocmessaging.ui.theme.Gray80

@ExperimentalMaterial3Api
@Composable
fun Login(navController: NavController) {
    Card(
        modifier = Modifier
            .padding(10.dp)
    ) {
        //TODO: Insert logo here
        //Image(bitmap = , contentDescription="Logo")
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Name: ",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight(),
                fontSize = 18.sp
            )
            nameTextField()

        }
        Row(horizontalArrangement = Arrangement.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignInButton(navigateUserScreen = { navController.navigate("UserScreen") })
            }
        }
    }
}

@Composable
fun SignInButton(navigateUserScreen: () -> Unit) {
    Button(onClick = navigateUserScreen, colors = ButtonDefaults.buttonColors(
        containerColor = Blue80,
        contentColor = Gray80)) {
        Text("Sign In")
    }
}

@ExperimentalMaterial3Api
@Composable
fun nameTextField() {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            value = newText
        },
        modifier = Modifier.background(Color(0xFFFFFFFF))
            .border(2.dp, Blue80)

    )
}