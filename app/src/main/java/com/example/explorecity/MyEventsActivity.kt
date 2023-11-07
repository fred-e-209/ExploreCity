package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorecity.api.classes.event.Hosting
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.EventStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEventsActivity(navController: NavController, viewModel: ApiViewModel) {

//    val events = mutableStateListOf(
//        Event("Month, Year- Time", "Event 1", "Address, City, State"),
//        Event("Month, Year- Time", "Event 2", "Address, City, State")
//        // ... add more events
//    )
    val eventStorageInstance = EventStorage.instance

    val userEvents by viewModel.userEvents.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchUserEvents()
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 10.dp) {
                TopAppBar(title = {
                    Text(
                        text = "My Events"
                    )
                })
            }
        }, content = {paddingValues ->
            LazyColumn (modifier = Modifier.padding(paddingValues)){
                items(userEvents) { event ->
                    EventCard(event, onClick = {
                        // This is a placeholder for navigating to the event details
                        eventStorageInstance.setEventID(event.id)
                        navController.navigate("details")
                    })
                }
            }
        }
    )

}

@Composable
fun EventCard(event: Hosting, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column (
                modifier = Modifier.weight(1f) // This ensures that the column takes up as much space as possible
            ){
                Text(event.displayname, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp)) // Space between text and icon

            Icon(Icons.Default.ArrowForward, contentDescription = "More details")
        }
    }
}
