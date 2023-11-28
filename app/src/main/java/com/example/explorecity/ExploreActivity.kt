package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.api.classes.RecommendationResponseBody
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
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreActivity(navController: NavController, viewModel: ApiViewModel) {
    // Mutable state to keep track of the current view
    val viewMode = remember { mutableStateOf("map") } // "map" or "list"

    val recTypes = remember { mutableStateListOf<String>() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val darkBlue = Color(0xFF003366)
    val white = Color.White

    // API Callers
    val coroutineScope = rememberCoroutineScope()
    var nearByEvents by remember { mutableStateOf(emptyList<EventDetailBody>()) }
    val eventFilter = viewModel.explorePageFilter()

    // Getting location recommendations
    val recommendationList = remember { mutableStateListOf<RecommendationResponseBody>() }
    val recFilter = viewModel.recommendationsFilter(query = recTypes.joinToString(separator = " "), location = null)

    LaunchedEffect(Unit) {
        nearByEvents = viewModel.fetchEventsByFilter(eventFilter = eventFilter)
    }



    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Surface(modifier = Modifier.width(300.dp), color = Color.White) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Recommendation Filters",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Type", modifier = Modifier.padding(bottom= 5.dp), color= DarkBlue)
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                            ) {
                                EventTypeButton("Restaurant", recTypes)
                                EventTypeButton("Gas Station", recTypes)
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                            ) {
                                EventTypeButton("Entertainment", recTypes)
                                EventTypeButton("Hotel", recTypes)
                            }
                            Button(onClick = { // TODO: Applies filter button
                                coroutineScope.launch {
                                    recommendationList.clear()
                                    recommendationList.addAll(viewModel.getListOfRecommendations(recFilter))
                                }
                            }) {
                                Text(text = "Apply Filters")
                            }
                        }
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            Surface(shadowElevation = 10.dp, color = DarkBlue) {
                                TopAppBar(
                                    title = {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
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
                                                Text("Recs")
                                            }
                                        }
                                    },
                                    navigationIcon = { Spacer(modifier = Modifier.width(30.dp)) },
                                    actions = {
                                        Icon(
                                            painterResource(R.drawable.ic_filter),
                                            contentDescription = "Filter",
                                            tint = darkBlue,
                                            modifier = Modifier.clickable {
                                                coroutineScope.launch {
                                                    drawerState.open()
                                                }
                                            }
                                        )
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
                                "map" -> GoogleMapsView(viewMode, nearByEvents)
                                "list" -> EventsListView(recommendationList)
                                "details" -> DetailsActivity(navController, viewModel)
                            }
                        }
                    }
                }
            }
        )
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
fun EventsListView(recommendations: List<RecommendationResponseBody>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nearby Recommendations",
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                fontSize = 20.sp,
            )
        }
        LazyColumn (modifier = Modifier.padding(vertical=5.dp)){
            items(recommendations) { recommendation ->
                RecommendationCard(recommendation = recommendation)
            }
        }
    }
}

@Composable
fun RecommendationCard(recommendation: RecommendationResponseBody) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(135.dp)
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
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceAround
            ){
                Spacer(modifier = Modifier.height(15.dp))
                Spacer(modifier = Modifier.weight(0.5f))
                Text(recommendation.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = DarkBlue,
                    fontSize = 28.sp,
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(recommendation.address, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(15.dp))
                Text("Approximately ${BigDecimal(recommendation.distance).setScale(10, RoundingMode.HALF_UP).toDouble()} miles away", maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(15.dp))
            }
            Spacer(modifier = Modifier.width(16.dp)) // Space between text and icon

            Icon(Icons.Default.ArrowForward, contentDescription = "More details")
        }
    }
}


