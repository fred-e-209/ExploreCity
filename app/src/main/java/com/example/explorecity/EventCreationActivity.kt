package com.example.explorecity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.explorecity.api.classes.event.DateTimeBody
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.classes.event.Location
import com.example.explorecity.api.models.ApiViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreationActivity(navController: NavController  ) {
    var eventName: String by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDetails: String by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val eventTypes = listOf("Concert", "Sports", "Social", "Others")

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    // Toast for messages
    var toastMessage by remember { mutableStateOf<String?>(null) }

    // Event body for POST request
    val eventBody = EventBody(
        displayname = eventName,
        description = eventDetails,
        location = "ChIJmb50i5SDRoYR_Q9QjI96P6c",
        start = DateTimeBody(16, 8, 0, 11, 2023),
        end = DateTimeBody(16, 10, 0, 11, 2023)
    )

    // Scope for calling, and API handler
    val scope = rememberCoroutineScope()
    val apiVM = ApiViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Event Creation")
                }},  // Set this to an empty composable
                navigationIcon = {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable(onClick = { navController.navigate("host_home") }))
                },
                modifier = Modifier.fillMaxWidth(), // Ensure it fills the width
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Handle event submission logic here
                scope.launch {
                    val eventID = apiVM.createEvent(eventBody)
                    toastMessage = if (eventID > 0) {
                        "Event Created!"
                    } else {
                        "Event not created"
                    }
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "Submit Event")
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {           Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        "   Tell us about your event....",
                        fontSize= 15.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top=24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = eventName,
                        onValueChange = { eventName = it },
                        label = { Text("Event Name") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(Modifier.padding(20.dp)) {

                        // Create an Outlined Text Field
                        // with icon and not expanded
                        OutlinedTextField(
                            value = eventType,
                            onValueChange = { eventType = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    // This value is used to assign to
                                    // the DropDown the same width
                                    mTextFieldSize = coordinates.size.toSize()
                                },
                            label = { Text("Event Type") },
                            trailingIcon = {
                                Icon(icon, "contentDescription",
                                    Modifier.clickable { mExpanded = !mExpanded })
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                            )
                        )

                        // Create a drop-down menu with list of cities,
                        // when clicked, set the Text Field text as the city selected
                        DropdownMenu(
                            expanded = mExpanded,
                            onDismissRequest = { mExpanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                        ) {
                            eventTypes.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    eventType = label
                                    mExpanded = false
                                }, text = { Text(text = label) })
                            }
                        }
                    }
                    //DropdownMenuForEventType(eventType)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = eventLocation,
                        onValueChange = { eventLocation = it },
                        label = { Text("Event Location") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = eventDetails,
                        onValueChange = { eventDetails = it },
                        label = { Text("Event Details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(200.dp), // modify accordingly
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        )
                    )
                }
            }
        }
    }
    toastMessage?.let { message ->
        DisplayToast(message = message)
        // Clear the toast message to avoid displaying it again on recomposition
        toastMessage = null
    }
}
