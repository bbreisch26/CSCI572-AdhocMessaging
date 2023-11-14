package com.csci572.adhocmessaging.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.csci572.adhocmessaging.R
import com.csci572.adhocmessaging.ui.theme.Blue80
import com.csci572.adhocmessaging.ui.theme.BlueGray80
import com.csci572.adhocmessaging.ui.theme.Gray80
import java.io.File

@ExperimentalMaterial3Api
@Composable
fun Login(navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Blue80),
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = "Login",
            color = Gray80,
            modifier = Modifier
                .padding(10.dp),
            textAlign = TextAlign.Center,
        )

        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)) {

            Text(
                text = "Name: ",
                textAlign = TextAlign.Center,
                color = Gray80,
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
        containerColor = Gray80,
        contentColor = Blue80)) {
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
        colors = TextFieldDefaults.textFieldColors(containerColor = Gray80, textColor = Blue80),
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color.DarkGray)

    )
}