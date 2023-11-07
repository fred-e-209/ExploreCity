package com.example.explorecity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.explorecity.ui.theme.DarkBlue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreActivity(navController: NavController) {
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
                "map" -> GoogleMapsView(navController) // Add GoogleMapsView here
                "list" -> EventsListView()
            }
        }
    }
}




@Composable
fun GoogleMapsView(navController: NavController) {
    val collegeStation = LatLng(30.627977, -96.334406)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(collegeStation, 14.5f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = rememberMarkerState(position = collegeStation),
            draggable = true,
            title = "Cstat",
            snippet = "Time: 8:00 pm"
        )
        MarkerInfoWindow(
            state = MarkerState(position = LatLng(30.610657, -96.340695)),
            onInfoWindowClick = {
                // Handle the click event
                Log.d("MAPS", "User clicked details")
                navController.navigate("details")
            }

        ) { marker ->
            Box(
                modifier = Modifier
                    .padding(16.dp) // Adjust padding for spacing
                    .background(Color.White, shape = RoundedCornerShape(8.dp)) // Adjust corner radius
                    .clickable {
                        navController.navigate("details")
                    }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = marker.title ?: "Aggie Football",
                        color = Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Add more vertical space
                    Text(
                        text = marker.snippet ?: "Time: 8:00PM",
                        color = Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Normal)
                    )
                }
            }
        }
    }
}








@Composable
fun EventsListView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("List of Events Here (like My Events Page)")
    }
}
