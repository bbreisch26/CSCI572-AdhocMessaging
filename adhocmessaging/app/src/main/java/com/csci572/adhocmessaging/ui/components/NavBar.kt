package com.csci572.adhocmessaging.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csci572.adhocmessaging.ui.theme.Blue80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navigationController: NavController, headerText: String, backArrowOn: Boolean) {
    CenterAlignedTopAppBar(title={ Text(text = headerText, color= Color.White) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Blue80, navigationIconContentColor = Color.White),
        navigationIcon = {
            if(navigationController.previousBackStackEntry != null && backArrowOn) {
                IconButton(onClick = { navigationController.navigateUp()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
            else {
                null
            }
        }
    )
}
