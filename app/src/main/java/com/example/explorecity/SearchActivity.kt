package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.ui.theme.DarkBlue
import kotlinx.coroutines.launch
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchActivity(navController: NavController, viewModel: ApiViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val searchQuery = remember { mutableStateOf("") }

//    val events = mutableStateListOf(
//        Event("Month, Year- Time", "Event 1", "Address, City, State"),
//        Event("Month, Year- Time", "Event 2", "Address, City, State")
//        // ... add more events
//    )

    val coroutineScope = rememberCoroutineScope()
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }
    val selectedEventTypes = mutableStateListOf<String>()
    var selectedDistance by remember { mutableStateOf("") }
    val context = LocalContext.current

    val userEvents by viewModel.userEvents.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchUserEvents()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Surface (modifier = Modifier.width(300.dp)){ /*TODO: make filter page size dynamic*/
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = "Event Filters",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue,
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Start Date:")
                                OutlinedTextField(
                                    value =startDate.value,
                                    onValueChange = { /* Do nothing as this is read-only */ },
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .width(150.dp)
                                    ,
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
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Row (verticalAlignment = Alignment.CenterVertically){
                                Text(text = "End Date:  ")
                                OutlinedTextField(
                                    value =endDate.value,
                                    onValueChange = { /* Do nothing as this is read-only */ },
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .width(150.dp)
                                    ,
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
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Type", modifier = Modifier.padding(bottom= 5.dp), color= DarkBlue)
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                            ) {
                                EventTypeButton("Music", selectedEventTypes)
                                EventTypeButton("Sports", selectedEventTypes)
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                            ) {
                                EventTypeButton("Religious", selectedEventTypes)
                                EventTypeButton("Tourism", selectedEventTypes)
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                            ) {
                                EventTypeButton("Social", selectedEventTypes)
                                EventTypeButton("Business", selectedEventTypes)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Max Distance Away",
                                modifier = Modifier.padding(bottom = 5.dp), color= DarkBlue
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SelectableButton("1 m", selectedDistance) {
                                    selectedDistance = it
                                }
                                SelectableButton("2.5 m", selectedDistance) {
                                    selectedDistance = it
                                }
                                SelectableButton("5 m", selectedDistance) {
                                    selectedDistance = it
                                }
                            }
                            SelectableButton("10m", selectedDistance) {
                                selectedDistance = it
                            }
                            // ... Add more distance buttons ...

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            }) {
                                Text(text = "Apply Filters")
                            }
                        }
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                    Column {
                        OutlinedTextField(
                            value = searchQuery.value,
                            onValueChange = { searchQuery.value = it },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                // Logic for searching the events goes here.
                            }),
                            shape = RoundedCornerShape(8.dp), // Rounded corners for the outline
                            singleLine = true, // Makes sure it's a single line text field
                            placeholder = { Text("Search ExploreCity Events...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_filter),
                                    contentDescription = "Filter Icon",
                                    tint = DarkBlue,
                                    modifier = Modifier.clickable {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        LazyColumn(modifier = Modifier.padding(5.dp)) {
                            items(events) { event ->
                                EventCard(event, onClick = {
                                    // This is a placeholder for navigating to the event details
                                    navController.navigate("details")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SelectableButton(label: String, selectedValue: String, onSelected: (String) -> Unit) {
    Button(
        onClick = { onSelected(label) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedValue == label) DarkBlue else Color.Transparent,
            contentColor = if (selectedValue == label) Color.White else Color.Black
        ),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.width(75.dp)
    ) {
        Text(text = label, maxLines = 1)
    }
}

@Composable
fun EventTypeButton(eventType: String, selectedEventTypes: MutableList<String>) {
    val isSelected = selectedEventTypes.contains(eventType)

    Button(
        onClick = {
            if (isSelected) {
                selectedEventTypes.remove(eventType)
            } else {
                selectedEventTypes.add(eventType)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) DarkBlue else Color.Transparent),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.width(120.dp)
    ) {
        Text(text = eventType, color = if (isSelected) Color.White else Color.Black, overflow = TextOverflow.Ellipsis)
    }
}