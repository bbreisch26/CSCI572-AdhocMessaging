package com.csci572.adhocmessaging.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.csci572.adhocmessaging.R
import com.csci572.adhocmessaging.ui.theme.Blue80
import com.csci572.adhocmessaging.ui.theme.BlueGray80
import com.csci572.adhocmessaging.ui.theme.Gray80
import java.io.File

private var nameField : String = ""
@ExperimentalMaterial3Api
@Composable
fun Login(navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
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
            nameTextField(navController)

        }
        Row(horizontalArrangement = Arrangement.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignInButton(navigateUserScreen = { navController.navigate("UserScreen/{name}".replace(oldValue="{name}", newValue = nameField))})
            }
        }
    }
}

@Composable
fun SignInButton(navigateUserScreen: () -> Unit) {
    Button(onClick = navigateUserScreen, colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground)) {
        Text("Sign In")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun nameTextField(navController : NavController) {
    var value by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            value = newText
            nameField = value
        },
        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background, textColor = MaterialTheme.colorScheme.onBackground),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            navController.navigate("UserScreen/{name}".replace(oldValue="{name}", newValue = nameField))
        }),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .border(1.dp, Color.DarkGray)


    )
}