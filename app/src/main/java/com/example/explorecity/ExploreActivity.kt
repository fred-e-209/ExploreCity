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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState


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
    // Old Stuff
  /*  AndroidView(
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
            // Set the initial location to College Station, Texas
            val collegeStation = LatLng(30.627977, -96.334406) // College Station coordinates
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(collegeStation, 15f) // 14f is the zoom level
            googleMap.moveCamera(cameraUpdate)

            // Add a marker for Kyle Field
            val kyleField = LatLng(30.610657, -96.340695) // Kyle Field coordinates
            val kyleFieldMarker = MarkerOptions()
                .position(kyleField)
                .title("Aggie Football")
                .snippet("Time: 8:00 PM")
            googleMap.addMarker(kyleFieldMarker)

            // Add a marker for The Zachry Engineering Building
            val zachryBuilding = LatLng(30.618519, -96.337866) // Zachry Building coordinates
            val zachryBuildingMarker = MarkerOptions()
                .position(zachryBuilding)
                .title("Study")
                .snippet("Time: 8:00 PM")
            googleMap.addMarker(zachryBuildingMarker)
        }
    }*/

    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    )
}



@Composable
fun EventsListView() {
    Text("List View Coming Soon...")
}
