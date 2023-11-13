package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.ui.theme.DarkBlue

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostActivity (navController: NavController, viewModel: ApiViewModel) {
//    val events = mutableStateListOf(
//        Event("Month, Year- Time", "Event 1", "Address, City, State"),
//        Event("Month, Year- Time", "Event 2", "Address, City, State")
//        // ... add more events
//    )


    val hostEvents by viewModel.userEvents.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchHostEvents()
    }

    Scaffold(topBar = {
        Surface(shadowElevation = 10.dp, color = DarkBlue) {
            TopAppBar(
                title = { Text(
                    "My Events",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("host_prof") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                },
                actions = {
                    Spacer(Modifier.weight(1f)) // This pushes the following icons to the right
                    IconButton(onClick = { navController.navigate("create_event") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Event")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = DarkBlue,
                    titleContentColor = Color.White,
                ),

                )
        }
    }
        , content = {paddingValues ->
        LazyColumn (modifier = androidx.compose.ui.Modifier.padding(paddingValues)){
            items(hostEvents) { event ->
                EventCard(event) {
                    // This is a placeholder for navigating to the event details
                    navController.navigate("details")
                }
            }
        }
    })
}
