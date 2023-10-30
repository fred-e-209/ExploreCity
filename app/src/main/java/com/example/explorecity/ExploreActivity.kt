package com.example.explorecity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explorecity.ui.theme.DarkBlue
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreActivity() {
    // Mutable state to keep track of the current view
    val viewMode = remember { mutableStateOf("map") } // "map" or "list"

    val darkBlue = Color(0xFF003366)
    val white = Color.White

    Scaffold(
        topBar = {
            Surface(shadowElevation = 10.dp, color = DarkBlue) {
                TopAppBar(
                    title = { Text("Explore") },
                    actions = {
                        // Centering the buttons by adding a Spacer before and after them
                        Spacer(modifier = Modifier.weight(1f))

                        // Map Button
                        TextButton(
                            onClick = { viewMode.value = "map" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viewMode.value == "map") darkBlue else white,
                                contentColor = if (viewMode.value == "map") white else darkBlue
                            )
                        ) {
                            Text("Map")
                        }

                        // List Button
                        TextButton(
                            onClick = { viewMode.value = "list" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viewMode.value == "list") darkBlue else white,
                                contentColor = if (viewMode.value == "list") white else darkBlue
                            )
                        ) {
                            Text("List")
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Filter Button
                        IconButton(onClick = { /* open drawer for filtering */ }) {
                            Icon(Icons.Default.Star, contentDescription = "Filter", tint = darkBlue)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (viewMode.value) {
                "map" -> GoogleMapsView() // Add GoogleMapsView here
                "list" -> EventsListView()
            }
        }
    }
}




@Composable
fun GoogleMapsView() {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                // Initialize the MapView
                onCreate(null)
                onResume()
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { mapView ->
        // This code block will be executed when the MapView is ready
        mapView.getMapAsync { googleMap ->
            // You can configure the GoogleMap here
        }
    }

}

@Composable
fun EventsListView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("List of Events Here (like My Events Page)")
    }
}
