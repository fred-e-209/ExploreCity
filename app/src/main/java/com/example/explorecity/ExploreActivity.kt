package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.api.classes.event.EventDetailBody
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.EventStorage
import com.example.explorecity.api.models.UserInformation
import com.example.explorecity.ui.theme.DarkBlue
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreActivity(navController: NavController, viewModel: ApiViewModel) {
    // Mutable state to keep track of the current view
    val viewMode = remember { mutableStateOf("map") } // "map" or "list"

    val darkBlue = Color(0xFF003366)
    val white = Color.White

    val userEvents by viewModel.userEvents.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchUserEvents()
        delay(1000)
        viewModel.updateEventsToday(userEvents)
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 10.dp, color = DarkBlue) {
                TopAppBar(
                    title = {
                        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
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
                        }
                    },
                    navigationIcon = {Spacer(modifier = Modifier.width(30.dp))},
                    actions = {
                        IconButton(onClick = { /* open drawer for filtering */ }) {
                            Icon(painterResource(R.drawable.ic_filter), contentDescription = "Filter", tint = darkBlue)
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
                .padding(bottom = 100.dp)
        ) {
            when (viewMode.value) {
                "map" -> GoogleMapsView(viewMode, userEvents)
                "list" -> EventsListView(navController, userEvents)
                "details" -> DetailsActivity(navController, viewModel)
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GoogleMapsView(viewMode: MutableState<String>, events: List<EventDetailBody>) {
    val userLocation = UserInformation.instance.getUserLocation()
    var currentUserLat by remember { mutableDoubleStateOf(0.0) }
    var currentUserLon by remember { mutableDoubleStateOf(0.0) }
    currentUserLat = userLocation.lat
    currentUserLon = userLocation.lon
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(currentUserLat, currentUserLon), 14.5f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = rememberMarkerState(position = LatLng(currentUserLat, currentUserLon)),
            draggable = true,
            title = "Your Location",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
        )
        for (event in events){
            MarkerInfoWindow(
                state = MarkerState(position = LatLng(event.coords.lat, event.coords.lon)),
                onInfoWindowClick = {
                    EventStorage.instance.setEventID(event.id)
                    viewMode.value = "details"
                },
                title = event.displayname,
                snippet = formatDate(event.start)
            )
            { marker ->
                Box(
                    modifier = Modifier
                        .padding(16.dp) // Adjust padding for spacing
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) // Adjust corner radius
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = marker.title ?: "Unknown",
                            color = Color.Black,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Add more vertical space
                        Text(
                            text = marker.snippet ?: "Time: Unknown",
                            color = Color.Black,
                            style = TextStyle(fontWeight = FontWeight.Normal)
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun EventsListView(navController: NavController, events: List<EventDetailBody>) {
    val eventStorageInstance = EventStorage.instance
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nearby Events",
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                fontSize = 20.sp,
            )
        }
        LazyColumn (modifier = Modifier.padding(vertical=5.dp)){
            items(events) { event ->
                EventCard(event, onClick = {
                    // This is a placeholder for navigating to the event details
                    eventStorageInstance.setEventID(event.id)
                    navController.navigate("details")
                })
            }
        }
    }
}


