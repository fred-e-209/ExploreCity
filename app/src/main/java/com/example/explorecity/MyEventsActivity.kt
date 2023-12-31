package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.ui.theme.DarkBlue

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEventsActivity(navController: NavController) {

    val events = mutableStateListOf(
        Event("Month, Year- Time", "Event 1", "Address, City, State"),
        Event("Month, Year- Time", "Event 2", "Address, City, State")
        // ... add more events
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "My Events",
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            fontSize = 30.sp,
                        )
                    }
                },
                navigationIcon = {
                    Row {
                        Spacer(Modifier.width(10.dp))
                        Surface(
                            shadowElevation = 2.dp,
                            shape = CircleShape,

                        ) {
                            Icon(
                                painterResource(R.drawable.logo),
                                contentDescription = "logo",
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .padding(8.dp),
                                tint = DarkBlue,
                            )
                        }
                    }
                },
                actions = {
                    Spacer(Modifier.width(61.dp))
                }

            )
        }, content = {paddingValues ->
            LazyColumn (modifier = Modifier.padding(paddingValues)){
                items(events) { event ->
                    EventCard(event, onClick = {
                        // This is a placeholder for navigating to the event details
                        navController.navigate("details")
                    })
                }
            }
        }
    )

}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = DarkBlue, // Replace with your desired color
                shape = RoundedCornerShape(size = 15.dp) // Match this with your Card's corner shape if any
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column (
                modifier = Modifier.weight(1f) // This ensures that the column takes up as much space as possible
            ){
                Text(event.date, color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 3.dp))
                Text(event.name, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text(event.location, color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(top = 3.dp))
            }
            Spacer(modifier = Modifier.width(16.dp)) // Space between text and icon

            Icon(Icons.Default.ArrowForward, contentDescription = "More details")
        }
    }
}


data class Event(val date: String, val name: String, val location: String)
