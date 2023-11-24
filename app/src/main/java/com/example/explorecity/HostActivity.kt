package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.EventStorage
import com.example.explorecity.ui.theme.DarkBlue
import kotlinx.coroutines.launch

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
    val eventStorage = EventStorage()

    LaunchedEffect(Unit) {
        viewModel.fetchHostEvents()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface (modifier = Modifier.width(300.dp), color = Color.White){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // List of clickable text items
                    val items = mapOf(
                        "User Preferences" to "prefs",
                        "App Settings" to "settings",
                        "Common Questions" to "questions",
                        "Log Out" to "login"
                    )
                    items.forEach { (text, route) ->
                        ClickableTextItem(text = text) {
                            navController.navigate(route) // Navigate based on the route
                        }
                        Divider() // Add a divider after each item
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        // Action for "Host your Own Events!"
                        navController.navigate("home")
                    }) {
                        Text("View Other Events!")
                    }
                }
            }
        },
        content=  {
            Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Box (modifier = Modifier.fillMaxWidth()){
                                Text(
                                    "My Events",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigate("host_prof") }) {
                                Icon(Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.clickable {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    })
                            }
                        },
                        actions = {
                            Spacer(Modifier.weight(1f)) // This pushes the following icons to the right
                            IconButton(onClick = { navController.navigate("create_event") }) {
                                Icon(Icons.Default.Add,
                                    contentDescription = "Add Event",
                                    tint = Color.White,
                                )
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = DarkBlue,
                        ),
                    )
            }, content = {paddingValues ->
                LazyColumn (modifier = Modifier.padding(paddingValues)){
                    items(hostEvents) { event ->
                        EventCard(event) {
                            // This is a placeholder for navigating to the event details
                            eventStorage.setEventID(event.id)
                            navController.navigate("details")
                        }
                    }
                }
            })
        }
    )
}