package com.csci572.adhocmessaging.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.csci572.adhocmessaging.MainActivity
import com.csci572.adhocmessaging.ui.theme.Blue80
import com.csci572.adhocmessaging.ui.theme.Gray80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBar(address: String, activity: MainActivity) {
    var inputValue by remember { mutableStateOf("") }
    fun sendMessage() {

        //messageInputViewModel.sendMessage(inputValue)
        Log.v("ChatScreen", "String to send : $inputValue")
        activity.sendMessage(address, inputValue)
        inputValue = ""

    }
    BottomAppBar(containerColor = Blue80) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = { newText ->
                inputValue = newText
            },
            colors = TextFieldDefaults.textFieldColors(containerColor = Gray80, textColor = Blue80),
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .border(1.dp, Color.DarkGray)

        )
        Button( // 5
            modifier = Modifier.height(56.dp),
            onClick = { sendMessage() },
            enabled = inputValue.isNotBlank(),
        ) {

            Icon( // 6
                imageVector = Icons.Default.Send,
                contentDescription = "Send Button"

            )

        }
    }
}