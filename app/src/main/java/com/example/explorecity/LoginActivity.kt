package com.example.explorecity

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.UserInformation
import com.example.explorecity.ui.theme.DarkBlue
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginActivity(navController: NavController, apiVM: ApiViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val  logoPainter = painterResource(id = R.drawable.logo)

    // User Info instance
    val userInfo = UserInformation.instance

    // Make scope for API call
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo placeholder

        Image(
            painter = logoPainter,
            contentDescription = "App Logo",
            modifier = Modifier.padding(bottom = 4.dp)
        )


        // App name
        Text(
            text = "ExploreCity",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkBlue,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Username/Email TextField
        OutlinedTextField(
            value = username, // bind to a state
            label = { Text("Username/Email") },
            onValueChange = {text->
                username = text
            }, // update the state
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { /* Focus on the next TextField */ }),
            visualTransformation = VisualTransformation.None
        )

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .padding(bottom = 24.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(onDone = { /* Handle login action */ })
        )

        // Submit Button

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))  // This takes up one-third of the width
            Button(
                onClick = {
                    // TODO: Handle Login Stuff
                    // Store username and password for API
                    userInfo.setUsername(username)
                    userInfo.setPassword(password)
                    scope.launch {
                        Log.d("LAUNCH", "This is ran")
                        var loginResponse = ApiViewModel().isLoginValid()
                        if (loginResponse > 0) {
                            // Logic for successful login
                            toastMessage = "Login successful!"
                            navController.navigate("home")
                        } else {
                            // Logic for unsuccessful login
                            toastMessage = "Login Failed!"
                        }
                    }
//                    if (username == "Tester" && password == "1234") {
//                        // Logic for successful login
//                        toastMessage = "Login successful!"
//                        navController.navigate("home")
//                    } else {
//                        // Logic for unsuccessful login
//                        toastMessage = "Login Failed!"
//                    }
                },
                modifier = Modifier.weight(10f)
            ) {
                Text("Submit")
            }
            Spacer(modifier = Modifier.weight(1f))  // This takes up one-third of the width
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))  // This takes up one-third of the width
            // Create Account Button
            Button(onClick = {
                navController.navigate("create_account")
            }, modifier = Modifier.weight(10f))
            {
                Text(text = "Create Account")
            }
            Spacer(modifier = Modifier.weight(1f))  // This takes up one-third of the width
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))  // This takes up one-third of the width
            // Create Account Button
            Button(onClick = {
                navController.navigate("home")
            }, modifier = Modifier.weight(10f))
            {
                Text(text = "Continue as Guest")
            }
            Spacer(modifier = Modifier.weight(1f))  // This takes up one-third of the width
        }
    }
    toastMessage?.let { message ->
        DisplayToast(message = message)
        // Clear the toast message to avoid displaying it again on recomposition
        toastMessage = null
    }
}

@Composable
fun DisplayToast(message: String) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    val apiVM = ApiViewModel()
    LoginActivity(navController = navController, apiVM)
}
