package com.example.explorecity

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun CommonQuestions(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderQuestion("Why use ExploreCity?", "This application allows people and communities" +
                "to host local events around their cities and keep up to date with their followed events.")

        Spacer(modifier = Modifier.height(16.dp))
        HeaderQuestion("How do I use ExploreCity?", "Users begin at the login page in which they are able to login " +
                "or create an account. Once users log in, they can view the navigation bar at the bottom to select between their followed events, search for events" +
                ", a map and list view of events, and profile section to logout and view other user settings. To follow an event, simply click" +
                " the event you have your eyes on and click 'Follow'. You can now stay up to date with the events of your choosing! To " +
                "host an event, click on the profile section in the navigation bar, and click 'Host your own event'. Fill out the information for your event and your event" +
                "will be posted for other users to view and follow.")

        Spacer(modifier = Modifier.height(32.dp))

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
fun HeaderQuestion(question: String, answer: String) {
    Column {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold

        )
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}