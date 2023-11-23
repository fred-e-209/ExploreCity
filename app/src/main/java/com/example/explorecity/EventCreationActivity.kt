package com.example.explorecity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.explorecity.api.classes.event.DateTimeBody
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.models.ApiViewModel
import kotlinx.coroutines.launch

import com.example.explorecity.ui.theme.DarkBlue
import java.util.Calendar
import java.util.Locale
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import com.example.explorecity.api.classes.event.stringToDateTimeBody
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.delay
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreationActivity(navController: NavController  ) {
    var toastMessage by remember { mutableStateOf<String?>(null) }

    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDetails = remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    var startDate = remember { mutableStateOf("") }
    var startDateComplete by remember { mutableStateOf(DateTimeBody(0, 0, 0, 0, 0)) }
    var endDate = remember { mutableStateOf("") }
    var startTime = remember { mutableStateOf("") }
    var endTime = remember { mutableStateOf("") }
    var placeID by remember { mutableStateOf("") }
    var location by remember {mutableStateOf("")}
    val context = LocalContext.current

    val eventTypes = listOf("Social", "Business", "Concert", "Sports", "Art", "Academic", "Tourism", "Special Event")

    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            placeID = (place.id ?: "")
            location = (place.name ?:"")
        }
    }

    val launchAutocomplete = {
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyDPFDQebw-Qwis6bU68K_-pmylM4pJc88k")
        }
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
            listOf(Place.Field.NAME, Place.Field.ID))
            .build(context)
        autocompleteLauncher.launch(intent)
    }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    // Scope for calling, and API handler
    val scope = rememberCoroutineScope()
    val apiVM = ApiViewModel()

    val eventBody = EventBody(
        displayname = eventName,
        description = eventDetails.value,
        location = placeID,
        start = stringToDateTimeBody(startDate.value, startTime.value),
        end = stringToDateTimeBody(endDate.value, endTime.value),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Create an Event",
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        fontSize = 30.sp,
                    )
                }},  // Set this to an empty composable
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable(
                            onClick = { navController.navigate("host_home") }))
                },
                actions = {Spacer(Modifier.width(25.dp))},
                modifier = Modifier.fillMaxWidth(),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick =
                /*TODO: Pass event into database*/
                {
                    // Handle event submission logic here
                    Log.d("EVENT_CREATION", eventBody.toString())
                    scope.launch {
                        val eventID = apiVM.createEvent(eventBody)
                        if (eventID > 0) {
                            toastMessage = "Event Created!"
                            delay(500)
                            navController.navigate("host_home")
                        } else {
                            toastMessage = "Event not created"
                        }
                    }
                },
                containerColor = Color(0xFF00a53c),
                contentColor = Color.White,
                modifier = Modifier
                    .width(75.dp)
                    .height(75.dp),
            ) {
                Icon(Icons.Default.Check, contentDescription = "Submit Event", modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                )
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
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top=24.dp)
                )
                Text("Event Name", Modifier.padding(top = 20.dp))
                TextField(
                    value = eventName,
                    onValueChange = {
                        if (it.length <= 40){ eventName = it}
                    },
                    placeholder = { Text("Limit 40 characters...")},
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
                Text("Start Date",  Modifier.padding(top = 8.dp))
                TextField(
                    value = startDate.value,
                    onValueChange = { /* Do nothing as this is read-only */ },
                    readOnly = true,
                    placeholder = { Text("Select Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    trailingIcon = {
                        IconButton(onClick = {showDatePicker(context, startDate)}) {
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
                    placeholder = { Text("Select Time") },
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

                Text("End Date",  Modifier.padding(top = 8.dp))
                TextField(
                    value =endDate.value,
                    onValueChange = { /* Do nothing as this is read-only */ },
                    readOnly = true,
                    placeholder = { Text("Select Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    trailingIcon = {
                        IconButton(onClick = {showDatePicker(context, endDate)}) {
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

                // End Time
                Text("End Time",  Modifier.padding(top = 8.dp))
                TextField(
                    value = endTime.value,
                    onValueChange = { /* Do nothing as this is read-only */ },
                    placeholder = { Text("Select Time") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            showTimePicker(context, endTime)
                        },
                    trailingIcon = {
                        IconButton(onClick = {showTimePicker(context, endTime)}) {
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

                Text("Event Location", Modifier.padding(top = 8.dp), textAlign = TextAlign.Start)
                Row {
                    TextField(
                        value = location,
                        readOnly = true,
                        onValueChange = { location = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        placeholder = { Text("Search for Address...") },
                        // TODO: Implement Places Autocomplete
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .clickable { launchAutocomplete() },
                        trailingIcon = {
                            IconButton(
                                onClick = { launchAutocomplete() },
                                content = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                },
                            )
                        },
                    )
                    Button(onClick = { launchAutocomplete() }) {
                        Text("Search")
                    }
                }

                Text("Event Type",  Modifier.padding(top = 8.dp))
                Column(Modifier.padding(20.dp)) {

                    // Create an Outlined Text Field
                    // with icon and not expanded
                    OutlinedTextField(
                        readOnly = true,
                        value = eventType,
                        onValueChange = { eventType = it },
                        placeholder = { Text("Select Event Type") },
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
                    ,
                    placeholder = { Text("Describe your Event... (150 character limit)") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                Spacer(Modifier.padding(15.dp))
            }
        }
            Spacer(Modifier.padding(20.dp))
        }
    }
    toastMessage?.let { message ->
        DisplayToast(message = message)
        // Clear the toast message to avoid displaying it again on recomposition
        toastMessage = null
    }
}

fun showDatePicker(context: Context, dateState: MutableState<String>) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, day, month, year ->
            dateState.value = String.format(Locale.getDefault(), "%02d-%02d-%04d", year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePicker(context: Context, timeState: MutableState<String>) {
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

