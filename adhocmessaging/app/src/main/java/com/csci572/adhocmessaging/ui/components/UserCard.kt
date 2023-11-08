package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class User(val username: String, val MAC: String)
@Composable
fun UserCard(user: User) {
    Card(modifier = Modifier
        .size(width=240.dp, 100.dp)
        .padding(10.dp)) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = user.username,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(6.dp).wrapContentHeight()
            )
            Text(
                text = user.MAC,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(6.dp).wrapContentHeight()
            )
        }
    }
}