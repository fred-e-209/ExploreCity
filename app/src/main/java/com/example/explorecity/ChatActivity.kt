package com.example.explorecity

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.api.classes.chat.Message
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.EventStorage
import com.example.explorecity.api.models.UserInformation
import com.example.explorecity.ui.theme.DarkBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatActivity(navBarController: NavController) {
    // TODO: Get messages from database and update periodically
    // - Use Message.add() to add a message. It should appear instantly.
    // - Get display name
    val userInfo = UserInformation.instance
    val displayName = userInfo.getUserDisplayName()
    var errorMsg by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }
    var currentMessage by remember { mutableStateOf("") }
    val userColors = remember { mutableMapOf<String, Color>() }
    var errorMsgToggle by remember { mutableStateOf(true) }

    // Display error as toast
    var toastMessage by remember { mutableStateOf<String?>(null) }

    // API callers
    val apiVM = ApiViewModel()
    val eventID = EventStorage.instance.getEventID()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            errorMsg = apiVM.fetchChatMessages(eventID, messages)
            delay(10000L)
            Log.d("CHAT_ERROR", errorMsg)
            if (messages.isEmpty() && errorMsgToggle) {
                toastMessage = errorMsg
                errorMsgToggle = false
            }
        }
    }

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
                                fontSize = 24.sp
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
                        .padding(horizontal = 5.dp)
                        .padding(bottom = 10.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        scope.launch {
                            errorMsg = addMessage(api = apiVM, displayName = displayName, message = currentMessage, eventID = eventID, messages = messages)
                            if (errorMsg.isNotBlank()) {
                                toastMessage = "Post error: $errorMsg"
                            }
                            currentMessage = ""
                        }
                    }),
                    trailingIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                addMessage(api = apiVM, displayName = displayName, message = currentMessage, eventID = eventID, messages = messages)
                                delay(200)
                                currentMessage = ""
                            }
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
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(messages) { index, message -> // Reverse the messages list to start from the bottom
                if (index != 0) {
                    Divider()
                }
                MessageRow(message = message, userColor = userColors.getOrPut(message.userName) { getUserColor() })
            }
        }
    }
    toastMessage?.let { message ->
        DisplayToast(message = message)
        // Clear the toast message to avoid displaying it again on recomposition
        toastMessage = null
    }
}

suspend fun addMessage(api: ApiViewModel, displayName: String, message: String, eventID: Int, messages: MutableList<Message>): String {
    var errorMsg = ""
    val response: Pair<Boolean, String> = api.postChatMessage(eventID = eventID, message = message)
    delay(500)
    if (!response.first) {
        messages.add(index = 0, element = Message(userName = displayName, content = message, timestamp = response.second))
    } else {
        errorMsg = response.second
    }
    return errorMsg
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


fun getUserColor(): Color {
    val index = Random.Default.nextInt(standardColors.size)
    return standardColors[index]
}

//fun getCurrentDateTime(): String {
//    val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
//    return dateFormat.format(Date())
//}