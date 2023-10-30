package com.example.explorecity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HostProfileActivity(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // List of clickable text items
        val items = mapOf(
            "User Preferences" to "prefs",
            "App Settings" to "settings",
            "Common Questions" to "questions",
            "Log Out" to "login"
        )
        items.forEach { (text, route) ->
            ClickableTextItem(text = text) {
                navController.navigate(route) // Navigate based on the route
            }
            Divider() // Add a divider after each item
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Action for "Host your Own Events!"
            navController.navigate("home")
        }) {
            Text("View Other Events!")
        }
    }
}

