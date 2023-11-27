package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.api.classes.event.EventDetailBody
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.EventStorage
import com.example.explorecity.ui.theme.DarkBlue
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEventsActivity(navController: NavController, viewModel: ApiViewModel) {

//    val events = mutableStateListOf(
//        Event("Month, Year- Time", "Event 1", "Address, City, State"),
//        Event("Month, Year- Time", "Event 2", "Address, City, State")
//        // ... add more events
//    )
    val eventStorageInstance = EventStorage.instance

    val userEvents by viewModel.userEvents.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchUserEvents()
        delay(1000)
        viewModel.updateEventsToday(userEvents)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "My Events",
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            fontSize = 30.sp,
                        )
                    }
                },
                navigationIcon = {
                    Row {
                        Spacer(Modifier.width(10.dp))
                        Surface(
                            shadowElevation = 2.dp,
                            shape = CircleShape,

                        ) {
                            Icon(
                                painterResource(R.drawable.logo),
                                contentDescription = "logo",
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .padding(8.dp),
                                tint = DarkBlue,
                            )
                        }
                    }
                },
                actions = {
                    Spacer(Modifier.width(61.dp))
                }

            )
        }, content = {paddingValues ->
            LazyColumn (modifier = Modifier.padding(paddingValues)){
                items(userEvents) { event ->
                    EventCard(event) {
                        // This is a placeholder for navigating to the event details
                        eventStorageInstance.setEventID(event.id)
                        navController.navigate("details")
                    }
                }
            }
        }
    )

}

@Composable
fun EventCard(event: EventDetailBody, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
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
                Spacer(modifier = Modifier.height(15.dp),)
                Text(formatDate(event.start) + " ⚬ " + formatTime(event.start), maxLines = 1, fontSize = 13.sp)
                Spacer(modifier = Modifier.weight(0.5f))
                Text(event.displayname,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = DarkBlue,
                    fontSize = 28.sp,
                    )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(event.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(15.dp))
            }
            Spacer(modifier = Modifier.width(16.dp)) // Space between text and icon

            Icon(Icons.Default.ArrowForward, contentDescription = "More details")
        }
    }
}
