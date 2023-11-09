package com.csci572.adhocmessaging.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.csci572.adhocmessaging.ui.theme.Blue80

@Composable
fun NavBar(headerText: String) {
    Row(modifier = Modifier
        .background(Blue80)
        .fillMaxWidth(1.0f),
        horizontalArrangement = Arrangement.Center

    ) {
        //Back button
        //
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = headerText,
                fontSize = 30.sp,
                color = Color(0xFFFFFFFF)
            )
        }

    }
}