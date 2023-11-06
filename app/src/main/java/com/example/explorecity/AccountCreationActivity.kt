package com.example.explorecity


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.ui.theme.DarkBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccountCreationActivity(navController: NavController) {
    // mutable states for inputs
    var newUsername by remember{ mutableStateOf("")}
    var newPassword by remember{ mutableStateOf("")}
    var reenteredPassword by remember{ mutableStateOf("")}
    var newDisplayName by remember{ mutableStateOf("")}
    var newEmail by remember{ mutableStateOf("")}

    // toast
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val newAccount = RegistrationBody(
        username = newUsername,
        password = newPassword,
        displayname = newDisplayName,
        email = newEmail
    )

    // API Helpers
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize() // This will make the Box fill the entire screen
    ) {
        // Back Button at the top left corner
        IconButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .align(Alignment.TopStart) // This will align the button to the top-left corner
                .padding(
                    top = 8.dp,
                    start = 8.dp
                ) // Added top and start padding for visual spacing from edges
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "back"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // App name
            Text(
                text = "ExploreCity",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Display Name TextField
            OutlinedTextField(
                value = newDisplayName, // bind to a state
                label = { Text("Display Name") },
                onValueChange = {
                    newDisplayName = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Email TextField
            OutlinedTextField(
                value = newEmail,
                label = { Text("Enter your email") },
                onValueChange = { newEmail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Username TextField
            OutlinedTextField(
                value = newUsername,
                label = { Text("Enter your username") },
                onValueChange = { newUsername = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Password TextField
            OutlinedTextField(
                value = newPassword,
                label = { Text("Enter your password") },
                onValueChange = { newPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            // Re-Password TextField
            OutlinedTextField(
                value = reenteredPassword,
                label = { Text("Re-enter your password") },
                onValueChange = { reenteredPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            // Create Account Button
            Button(
                onClick = {
                    //TODO: Handle Account Creation Stuff
                    // Check if reenteredPassword == newPassword
                    if (newPassword == reenteredPassword) {
                        scope.launch {
                            val newUserID = ApiViewModel().createNewAccount(newAccount)
                            // This returns a pair, int and list of error messages.
                            if (newUserID.first > 0) {
                                toastMessage = "User Account Created!"
                                navController.navigate("login")
                            } else {
                                val errorList = newUserID.second
                                for (msg in errorList) {
                                    toastMessage = "Field (${msg.field}): ${msg.description}"
                                    delay(1000)
                                }
                                toastMessage = "User Account Not Created"
                            }
                        }
                    } else {
                        toastMessage = "Password and reentered password does not match"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Create Account")
            }
        }
    }
    toastMessage?.let { message ->
        DisplayToast(message = message)
        // Clear the toast message to avoid displaying it again on recomposition
        toastMessage = null
    }
}

