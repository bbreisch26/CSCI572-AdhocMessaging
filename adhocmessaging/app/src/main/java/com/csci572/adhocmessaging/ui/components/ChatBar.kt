package com.csci572.adhocmessaging.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.csci572.adhocmessaging.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBar(userId: String, activity: MainActivity) {
    var inputValue by remember { mutableStateOf("") }
    fun sendMessage() {
        //messageInputViewModel.sendMessage(inputValue)
        val temp = inputValue
        Log.v("ChatScreen", "String to send : $inputValue")
        activity.p2papp?.sendMessage(inputValue)
        inputValue = ""
        CoroutineScope(IO).launch {
            activity.p2papp?.messageDatabase?.messageDao()?.insert(Message(contactName = userId, sourceIsMe = true, contents = temp))
        }

    }
    BottomAppBar(containerColor = Blue80) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = { newText ->
                inputValue = newText
            },
            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background, textColor = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .border(1.dp, Color.DarkGray)

        )
        Button( // 5
            modifier = Modifier.height(56.dp).offset(x = (2).dp),
            onClick = { sendMessage() },
            enabled = inputValue.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, disabledContainerColor = MaterialTheme.colorScheme.secondary)
        ) {

            Icon( // 6
                imageVector = Icons.Default.Send,
                contentDescription = "Send Button",
                tint = MaterialTheme.colorScheme.primary,

            )

        }
    }
}