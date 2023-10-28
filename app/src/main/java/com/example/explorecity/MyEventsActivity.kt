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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEventsActivity(navController: NavController) {
    var isEditMode by remember { mutableStateOf(false) }
    var eventToDelete by remember { mutableStateOf<Event?>(null) } // This will hold the event to delete

    val events = mutableStateListOf(
        Event("Month, Year- Time", "Event 1", "Address, City, State"),
        Event("Month, Year- Time", "Event 2", "Address, City, State")
        // ... add more events
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Events") },
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }, content = {paddingValues ->
            LazyColumn (modifier = Modifier.padding(paddingValues)){
                items(events) { event ->
                    EventCard(event, isEditMode, onClick = {
                        // This is a placeholder for navigating to the event details
                        navController.navigate("eventDetails/${event.name}")
                    }) {
                        // Set the event to delete when trash icon is clicked
                        eventToDelete = it
                    }
                }
            }
        }
    )

    eventToDelete?.let { eventToDelete ->
        showDeleteDialog(
            event = eventToDelete,
            onDismiss = {
            },
            onDelete = {
                events.remove(eventToDelete) // Remove the event from the list
            }
        )
    }

}

@Composable
fun EventCard(event: Event, isEditMode: Boolean, onClick: () -> Unit, onDelete: (Event) -> Unit) {
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
                Text(event.date)
                Text(event.name, fontWeight = FontWeight.Bold)
                Text(event.location)
            }
            Spacer(modifier = Modifier.width(16.dp)) // Space between text and icon
            if (isEditMode) {
                // Show trash icon when in edit mode
                IconButton(onClick = { onDelete(event) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            } else {
                // Arrow icon when not in edit mode
                Icon(Icons.Default.ArrowForward, contentDescription = "More details")
            }
        }
    }
}

@Composable
fun showDeleteDialog(event: Event, onDismiss: () -> Unit, onDelete: (Event) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Event") },
        text = { Text("Are you sure you want to delete ${event.name}?") },
        confirmButton = {
            TextButton(onClick = {
                onDelete(event) // Close the dialog after confirming
                onDismiss()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class Event(val date: String, val name: String, val location: String)
