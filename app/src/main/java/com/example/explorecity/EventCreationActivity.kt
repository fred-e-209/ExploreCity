package com.example.explorecity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
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
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreationActivity(navController: NavController  ) {
    val eventName = remember { mutableStateOf("") }
    val eventLocation = remember { mutableStateOf("") }
    val eventDetails = remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    val eventDate = remember { mutableStateOf("") }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val context = LocalContext.current


    val eventTypes = listOf("Concert", "Sports", "Social", "Others")

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

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
                    // Some text thing
                    Text(
                        "   Tell us about your event....",
                        fontSize= 15.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top=24.dp)
                    )
                    Text("Event Name", Modifier.padding(top = 20.dp))
                    TextField(
                        value = eventName.value,
                        onValueChange = { eventName.value = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        singleLine = true,
                    )

                    // Event Date
                    Text("Event Date",  Modifier.padding(top = 8.dp))
                    TextField(
                        value = eventDate.value,
                        onValueChange = { /* Do nothing as this is read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        trailingIcon = {
                            IconButton(onClick = {showDatePicker(context, eventDate)}) {
                                Icon(painterResource(R.drawable.ic_calendar), contentDescription = "Select Date")
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        singleLine = true,
                    )

                    // Start Time
                    Text("Start Time", Modifier.padding(top = 8.dp))
                    TextField(
                        value = startTime.value,
                        onValueChange = { /* Do nothing as this is read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        trailingIcon = {
                            IconButton(onClick = {showTimePicker(context, startTime)}) {
                                Icon(painterResource(R.drawable.ic_time), contentDescription = "Select Date")}
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        singleLine = true,
                    )

                    // End Time
                    Text("End Time",  Modifier.padding(top = 8.dp))
                    TextField(
                        value = endTime.value,
                        onValueChange = { /* Do nothing as this is read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                showTimePicker(context, endTime)
                            },
                        trailingIcon = {
                            Icon(painterResource(R.drawable.ic_time), contentDescription = "Select End Time")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        singleLine = true,
                    )

                    Text("Event Type",  Modifier.padding(top = 8.dp))
                    Column(Modifier.padding(20.dp)) {

                        // Create an Outlined Text Field
                        // with icon and not expanded
                        OutlinedTextField(
                            readOnly = true,
                            value = eventType,
                            onValueChange = { eventType = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    // This value is used to assign to
                                    // the DropDown the same width
                                    mTextFieldSize = coordinates.size.toSize()
                                },
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

                    Text("Event Location",  Modifier.padding(top = 8.dp), textAlign = TextAlign.Start)
                    TextField(
                        value = eventLocation.value,
                        onValueChange = { eventLocation.value = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                    )

                    Text("Event Details",  Modifier.padding(top = 8.dp), textAlign = TextAlign.Start)
                    TextField(
                        value = eventDetails.value,
                        onValueChange = {
                            if (it.length <= 150){ eventDetails.value = it}
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(200.dp)
                        , // modify accordingly
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        )
                    )
                }
            }
            Spacer(Modifier.padding(10.dp))
        }
    }
}

private fun showDatePicker(context: Context, dateState: MutableState<String>) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, day ->
            dateState.value = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

private fun showTimePicker(context: Context, timeState: MutableState<String>) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            timeState.value = hour.toTwoDigitString() + ":" + minute.toTwoDigitString()
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // Use 24-hour time format
    ).show()
}

private fun Int.toTwoDigitString(): String {
    return this.toString().padStart(2, '0')
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreationActivity(navController: NavController) {
    val eventName = remember { mutableStateOf("") }
    val eventLocation = remember { mutableStateOf("") }
    val eventDetails = remember { mutableStateOf("") }
    var eventType = remember { mutableStateOf("") }
    val eventDate = remember { mutableStateOf("") }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val context = LocalContext.current


    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val eventTypes = mutableListOf("Concert", "Sports", "Social", "Others")

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Event Creation", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon", tint = Color.White)
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
            ) {
                Icon(Icons.Filled.Check, "checkIcon", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Tell us about your event...", style = MaterialTheme.typography.headlineSmall)

                    // Event Name
                    Text("Event Name", Modifier.padding(start = 20.dp, top = 8.dp))
                    TextField(
                        value = eventName.value,
                        onValueChange = { eventName.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        singleLine = true
                    )

                    // Event Date
                    Text("Event Date", Modifier.padding(start = 20.dp))
                    TextField(
                        value = eventDate.value,
                        onValueChange = { /* Do nothing as this is read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                showDatePicker(context, eventDate)
                            },
                        trailingIcon = {
                            Icon(Icons.Default.AddCircle, contentDescription = "Select Date")
                        }
                    )

                    // Start Time
                    Text("Start Time", Modifier.padding(start = 20.dp))
                    TextField(
                        value = startTime.value,
                        onValueChange = { /* Do nothing as this is read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                showTimePicker(context, startTime)
                            },
                        trailingIcon = {
                            Icon(Icons.Default.AddCircle, contentDescription = "Select Start Time")
                        }
                    )

                    // End Time
                    Text("End Time", Modifier.padding(start = 20.dp))
                    TextField(
                        value = endTime.value,
                        onValueChange = { /* Do nothing as this is read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                showTimePicker(context, endTime)
                            },
                        trailingIcon = {
                            Icon(Icons.Default.AddCircle, contentDescription = "Select End Time")
                        }
                    )

                    // Event Type
                    Text("Event Type", Modifier.padding(start = 20.dp))
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
                            label = Text("Event Type") ,
                            trailingIcon =
                                Icon(icon, "contentDescription",
                                    Modifier.clickable { mExpanded = !mExpanded })
                            ,
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
                        value = eventLocation.value,
                        onValueChange = { eventLocation.value = it },
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

                    // Event Location
                    Text("Event Location", Modifier.padding(start = 20.dp))
                    TextField(
                        value = eventLocation.value,
                        onValueChange = { eventLocation.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        singleLine = true
                    )

                    // Event Details
                    Text("Event Details", Modifier.padding(start = 20.dp))
                    TextField(
                        value = eventDetails.value,
                        onValueChange = { if (it.length <= 140) eventDetails.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(200.dp),
                        maxLines = 4,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
                    )
                }
            }
        }
    }
}

@Composable
fun EventTypeDropdown(eventType: MutableState<String>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val eventTypes = listOf("Conference", "Meeting", "Workshop", "Social", "Other") // Example event types

    Box(modifier = modifier) {
        TextField(
            value = eventType.value,
            onValueChange = { eventType.value = it },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            label = { Text("Event Type") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            eventTypes.forEach { label ->
                DropdownMenuItem(
                    text= { Text(text = label) },
                    onClick = {
                        eventType.value = label
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun showDatePicker(context: Context, dateState: MutableState<String>) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, day ->
            dateState.value = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

private fun showTimePicker(context: Context, timeState: MutableState<String>) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            timeState.value = hour.toTwoDigitString() + ":" + minute.toTwoDigitString()
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // Use 24-hour time format
    ).show()
}

private fun Int.toTwoDigitString(): String {
    return this.toString().padStart(2, '0')
}*/

