package com.example.explorecity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import com.example.explorecity.ui.theme.DarkBlue
import kotlin.random.Random


val standardColors = listOf(
    Color(0xFFF9AA33),
    Color(0xFF4D8FDA),
    Color(0xFFE91E63),
    Color(0xFF00ACC1),
    Color(0xFF5E35B1),
    Color(0xFF3949AB),
    Color(0xFF43A047),
    Color(0xFF1E88E5),
    Color(0xFFF06292),
    Color(0xFFFFB74D),
    Color(0xFFBA68C8),
    Color(0xFFAED581),
    Color(0xFF7986CB),
    Color(0xFFFF8A65),
    Color(0xFFA1887F)
)
data class Message(val userName: String, val content: String, val timestamp: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatActivity(navBarController: NavController) {
    // TODO: Get messages from database and update periodically
    // - Use Message.add() to add a message. It should appear instantly.
    // - Get display name
    val displayName = "Freddy V"
    val messages = remember {
        mutableStateListOf(
            Message("David L", "This is my message.", "OCT 30, 11:20 AM"),
            Message("Lucas H", "This is my message.", "OCT 30, 2:51 PM"),
            Message("Lucas H", "This is my message.", "OCT 30, 2:52 PM"),
            Message("Freddy V", "This is my message.This is my message.This is my message." +
                    "This is my message.This is my message. This is my message.", "OCT 30, 2:53 PM"),
            Message("Lucas H", "This is my message.", "OCT 30, 2:54 PM"),
            Message("David L", "This is my message.", "OCT 31, 11:20 AM"),
            Message("Lucas H", "This is my message.", "OCT 31, 2:51 PM"),
            Message("Lucas H", "This is my message.", "OCT 31, 2:52 PM"),
            Message("Sam P", "This is my message.", "OCT 31, 3:10 PM"),
        )
    }
    var currentMessage by remember { mutableStateOf("") }
    val userColors = remember { mutableMapOf<String, Color>() }

    Scaffold(
        modifier = Modifier.padding(bottom = 80.dp),
        topBar = {
            Surface (shadowElevation = 10.dp){
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Event Chat",
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue,
                                fontSize = 24.sp,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navBarController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {Spacer(modifier = Modifier.width(30.dp))}
                )
            }
        },
        bottomBar = {
            Surface(shadowElevation = 2.dp, color = Color.White) {
                OutlinedTextField(
                    value = currentMessage,
                    onValueChange = {
                        if (currentMessage.length < 100) {
                            currentMessage = it
                        }
                    },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp).padding(bottom = 10.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        addMessage(displayName, currentMessage, messages)
                        currentMessage = ""
                    }),
                    trailingIcon = {
                        IconButton(onClick = {
                            addMessage(displayName, currentMessage, messages)
                            currentMessage = ""
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send Message")
                        }
                    },
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            reverseLayout = true, // This makes the list start from the bottom
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(messages.reversed()) { index, message -> // Reverse the messages list to start from the bottom
                if (index != 0) {
                    Divider()
                }
                MessageRow(message = message, userColor = userColors.getOrPut(message.userName) { getUserColor(message.userName) })
            }
        }
    }
}

fun addMessage(userName: String, message: String, messages: SnapshotStateList<Message>){
    // TODO: Send message to database
    val currentDateTime = getCurrentDateTime()
    messages.add(
        Message(
            userName,
            message,
            currentDateTime
        )
    )
}

@Composable
fun MessageRow(message: Message, userColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Spacer(modifier = Modifier.width(2.dp))
        Column (verticalArrangement = Arrangement.Top){
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(userColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = message.userName.take(1), color = Color.White)
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Row {
                Text(text = message.userName, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = message.timestamp, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = message.content)
            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}


fun getUserColor(userName: String): Color {
    val index = Random.Default.nextInt(standardColors.size)
    return standardColors[index]
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
    return dateFormat.format(Date())
}