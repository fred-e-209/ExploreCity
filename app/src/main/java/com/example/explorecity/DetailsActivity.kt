package com.example.explorecity

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import com.example.explorecity.api.classes.event.emptySingleEventResponse
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.EventStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsActivity(navBarController: NavController, viewModel: ApiViewModel) {
    // Scrollable content since we don't know how long the description will be
    val eventID = EventStorage.instance.getEventID()

    val event by viewModel.singleEvent.observeAsState(emptySingleEventResponse())

    LaunchedEffect(Unit) {
        try {
            viewModel.fetchEvent(eventID)
        } catch (e: Exception) {
            Log.e("DETAILEDEVENTPAGE", e.toString())
        }
    }

//    val event = DetailedEvent(
//        date = "October 6, 2023",
//        name = "Some Event",
//        location =  Location("Some Address", "College Station", "TX"),
//        eventType =  "Event Type",
//        description =  "This is where the description of the event will be located. It will include" +
//                " all additional information the host wants to add. We should probably add a character limit " +
//                "or something. "
//        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable(onClick = { navBarController.navigate("events") }))

        Spacer(modifier = Modifier.height(16.dp))

        // Event time and title card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
//                Text(text = event., style = TextStyle(color = Color.Gray))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = event.displayname, style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp
                )
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Follow and Chat buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* Follow logic */ }, modifier = Modifier.weight(20f)) {
                Text("Follow")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { /* Navigate to chat room */ }, modifier = Modifier.weight(20f)) {
                Icon(Icons.Default.Search, contentDescription = "Chat")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Chat")
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Event details card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation =  CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Location with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(Icons.Default.Place, contentDescription = "Location")
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Lat: ${event.location.lat}")
                        Text("Lon: ${event.location.lon}")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Event type with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Replace with appropriate icon for event type
                    Icon(Icons.Default.CheckCircle, contentDescription = "Event Type")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(event.description)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Event description
                Text("About this Event", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(event.description)
            }
        }
    }
}

//data class DetailedEvent(
//    val date: String,
//    val name: String,
//    val location: Location,
//    val eventType: String,
//    val description: String
//)
//
//data class Location(
//    val address: String,
//    val city: String,
//    val state: String
//)
