package com.example.explorecity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight


@Composable
fun ProfileDetails(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Account Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))
        HeaderQuestion2("Username:", "!!!USERNAME!!!.")
        Spacer(modifier = Modifier.height(16.dp))
        HeaderQuestion2("First Name:", "!!!FirstName!!!")
        Spacer(modifier = Modifier.height(16.dp))
        HeaderQuestion2("Last Name:", "!!!LastName!!!")
        Spacer(modifier = Modifier.height(16.dp))
        HeaderQuestion2("Account Email:", "!!!EMAIL!!!")

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {
                // Handle button click if needed
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Back to Previous Page")
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}




@Composable
fun HeaderQuestion2(question: String, answer: String) {
    Column {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 2.dp),
            fontWeight = FontWeight.Bold

        )
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}