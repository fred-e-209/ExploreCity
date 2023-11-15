package com.example.explorecity

import android.widget.Button
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import com.example.explorecity.ui.theme.DarkBlue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsActivity(navBarController: NavController) {
    // Scrollable content since we don't know how long the description will be
    var showConfirmDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }


    var event by remember {mutableStateOf(DetailedEvent(
        startDate = "11-8-2023",
        startTime = "2:00",
        endDate = "11-12-2023",
        endTime = "5:00",
        attendees = 123,
        name = "Some Event",
        host = "Some guy",
        location =  "Some Address,College Station,TX",
        eventType =  "Social",
        description =  "This is where the description of the event will be located. It will include" +
                " all additional information the host wants to add. We should probably add a character limit " +
                "or something. ",
        attending = false,
        hosting = false,
        ))}

    // The following state could come from a ViewModel or be passed down to this composable
    val isFollowing = event.attending // This should be a state that triggers recompositions when changed

    // Use animateFloatAsState to animate the weight changes
    val followButtonWeight by animateFloatAsState(if (isFollowing) 1f else 3f, label = "")
    val chatButtonWeight by animateFloatAsState(if (isFollowing) 3f else 1f, label = "")


    val toggleFollow = {
        event = event.copy(attending = !event.attending)
        dialogMessage = if (event.attending) {
            "You are now following ${event.name}"
        } else {
            "You are no longer following ${event.name}"
        }
        showConfirmDialog = true
    }

    Row (horizontalArrangement = Arrangement.SpaceBetween) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable(
                onClick = { navBarController.navigate("events") })
        )
        Icon(
            Icons.Default.MoreVert,
            contentDescription = "More Options",
            modifier = Modifier.clickable(
                onClick = { navBarController.navigate("events") })
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(title = {Text(text = "")},
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable(
                            onClick = { navBarController.popBackStack() })
                    )
                }, actions = {ReportEventOption()}
            )
        }
    ){paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 15.dp)
        ) {
            // Event time and title card
            Spacer(modifier = Modifier.height(5.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = DarkBlue, // Replace with your desired color
                        shape = RoundedCornerShape(size = 15.dp) // Match this with your Card's corner shape if any
                    ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = event.name, style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,
                            color= DarkBlue,
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Hosted by ${event.host} ⚬ ${event.attendees} attending", style = TextStyle(color = Color.Gray))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Follow Button
                Button(
                    onClick = toggleFollow,
                    modifier = Modifier
                        .weight(followButtonWeight)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    colors = if (isFollowing) ButtonDefaults.buttonColors(containerColor = Color(0xFFaf0104)) else ButtonDefaults.buttonColors(containerColor = DarkBlue)
                ) {
                    if (isFollowing) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Unfollow")
                    } else {
                        Text(text="Follow", maxLines = 1)
                    }
                }

                Spacer(modifier = Modifier.weight(0.1f)) // This will keep the buttons apart

                // Chat Button
                Button(
                    onClick = { navBarController.navigate("chat") },
                    modifier = Modifier
                        .weight(chatButtonWeight)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessHigh
                            )
                        ),
                    enabled = isFollowing,
                    colors = if (isFollowing) ButtonDefaults.buttonColors(containerColor = DarkBlue) else ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    if (isFollowing) {
                        Text("Chat", maxLines = 1)
                    } else {
                        Icon(painterResource(R.drawable.ic_chat), contentDescription = "Chat Disabled", tint = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))


            // Event details card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Location with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.Place, contentDescription = "Location", tint = DarkBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        val parsedLocation = event.location.split(',')

                        Column {
                            if (parsedLocation.size == 3) {
                                Text(parsedLocation[0])
                                Text("${parsedLocation[1]}, ${parsedLocation[2]}")
                            } else {
                                Text(event.location)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_calendar),
                            tint = DarkBlue,
                            contentDescription = "Event Duration"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            val startDate = formatDate(event.startDate)
                            val endDate = formatDate(event.endDate)
                            Text(text = if (startDate != endDate) "$startDate to $endDate" else startDate)
                            Text("${formatTime(event.startTime)} - ${formatTime(event.endTime)}")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Event type with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Replace with appropriate icon for event type
                        Icon(
                            when (event.eventType){
                               "Social" -> painterResource(R.drawable.ic_social)
                                "Business" -> painterResource(R.drawable.ic_business)
                                "Sports" -> painterResource(R.drawable.ic_sports)
                                "Tourism" -> painterResource(R.drawable.ic_tourism)
                                "Concert" -> painterResource(R.drawable.ic_concert)
                                else -> {
                                    painterResource(R.drawable.logo)
                                }
                            },
                            contentDescription = "Event Type",
                            tint = DarkBlue,
                            modifier = Modifier.width(25.dp).height(25.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(event.eventType)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Event description
                    Text("About this Event", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(event.description)

                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    }
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showConfirmDialog = false }) {
                    Text("Dismiss")
                }
            },
            containerColor = Color.White,
        )
    }
}


/*TODO: Pass event name (or ID?) to send the report*/
@Composable
fun ReportEventOption() {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    // Options for reporting the event
    val reportOptions = listOf("Fake Event", "Inappropriate Content", "Technical Issues", "Other")
    var response = "A Report has been sent to the ExploreCity Team."

    // More options icon
    IconButton(onClick = { expanded = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = "More options")
    }

    // Dropdown menu
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Report this event") },
            onClick = {
                expanded = false
                showDialog = true
            }
        )
    }

    // Show the dialog for reporting
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Report a problem") },
            text = {
                Column {
                    Text("Select the problem with the event:", modifier= Modifier.padding(bottom = 5.dp))
                    reportOptions.forEach { text ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { selectedOption = text }
                                )
                                .padding(horizontal = 16.dp)
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = { selectedOption = text }
                            )
                            Text(text = text, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        /*TODO: Send a report to the database
                            - The String selected will contain the report type
                            - Change condition to depend on whether the report is successful
                         */
                        if (true){
                            response = "There was an issue sending a response."
                        }
                        showConfirmDialog = true // Show confirmation after the report dialog
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Show confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Report Submitted") },
            text = { Text(response) },
            confirmButton = {
                Button(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    // Parse the input date
    val parsedDate = inputFormat.parse(inputDate)

    // Format the date into the new format
    return if (parsedDate != null) {
        outputFormat.format(parsedDate)
    } else {
        "Invalid date" // Handle this as you find suitable
    }
}

fun formatTime(inputTime: String): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    return try {
        val parsedDate = inputFormat.parse(inputTime)
        outputFormat.format(parsedDate)
    } catch (e: ParseException) {
        "Invalid time" // Handle invalid time format
    }
}




