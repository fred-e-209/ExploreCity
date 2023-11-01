package com.example.explorecity

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchActivity(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val searchQuery = remember { mutableStateOf("") }

    val events = mutableStateListOf(
        Event("Month, Year- Time", "Event 1", "Address, City, State"),
        Event("Month, Year- Time", "Event 2", "Address, City, State")
        // ... add more events
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }) {
                    Text("Close")
                }
                // Add more filtering options here as needed.
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            OutlinedTextField(
                                value = searchQuery.value,
                                onValueChange = { searchQuery.value = it },
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {
                                    // Logic for searching the events goes here.
                                }),
                                shape = RoundedCornerShape(8.dp), // Rounded corners for the outline
                                singleLine = true, // Makes sure it's a single line text field
                                placeholder = {Text("Search using Keywords...")},
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Filter Icon",
                                        modifier = Modifier.clickable {
                                            coroutineScope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().padding(5.dp)
                            )
                        },
                    /*    actions = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Filter Icon",
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        }*/
                    )
                }
            ) {paddingValues ->
                // This is where the cards for events will appear after a search.
                // Use a LazyColumn for efficiency if expecting many cards.

                // Example card:
                LazyColumn (modifier = Modifier.padding(paddingValues)){
                    items(events) { event ->
                        EventCard(event, onClick = {
                            // This is a placeholder for navigating to the event details
                            navController.navigate("details")
                        })
                    }
                }
            }
        }
    )
}
