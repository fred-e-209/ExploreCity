package com.example.explorecity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explorecity.ui.theme.DarkBlue
import com.example.explorecity.ui.theme.ExploreCityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCreationActivity(navController: NavController) {
    // This is where you'd design your account creation screen.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Back Arrow (For now, it's just a simple text. Replace it with an Icon later.)
        Button(
            onClick = {navController.navigate("login") })
        {
            Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = "back" )
        }
        Text(
            text = "<-",
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 32.dp)
        )

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
            value = "", // bind to a state
            label = { Text("Display Name") },
            onValueChange = { /* update the state */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Email TextField
        OutlinedTextField(
            value = "",
            label = { Text("Enter your email") },
            onValueChange = { /* update the state */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Username TextField
        OutlinedTextField(
            value = "",
            label = { Text("Enter your username") },
            onValueChange = { /* update the state */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password TextField
        OutlinedTextField(
            value = "",
            label = { Text("Enter your password") },
            onValueChange = { /* update the state */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Re-Password TextField
        OutlinedTextField(
            value = "",
            label = { Text("Re-enter your password") },
            onValueChange = { /* update the state */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Create Account Button
        Button(
            onClick = { /* Handle account creation logic */ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Create Account")
        }
    }
}

