package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.csci572.adhocmessaging.ui.theme.Blue80

data class MessageContent(val isFromMe : Boolean, val text : String)
@Composable
fun Message(message : MessageContent) {
    Box(
        modifier = Modifier
            //.align(if (message.isFromMe) Alignment.End else Alignment.Start)
            .clip(
                RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 48f,
                    bottomStart = if (message.isFromMe) 48f else 0f,
                    bottomEnd = if (message.isFromMe) 0f else 48f
                )
            )
            .background(Blue80)
            .padding(16.dp)
    ) {
        Text(text = message.text, color= Color.White)
    }
}