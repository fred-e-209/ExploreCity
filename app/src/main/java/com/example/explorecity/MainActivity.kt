package com.example.explorecity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explorecity.ui.theme.ExploreCityTheme

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginActivity(navController)
        }
        composable("create_account") {
            AccountCreationActivity(navController)
        }
        composable("home") {
            HomeActivity(navController)
        }
        composable("host_home") {
            HostActivity(navController)
        }
        composable("create_event") {
            EventCreationActivity(navController)
        }
        composable("event_details") {
            DetailsActivity(navController)
        }
        composable("details") {
            DetailsActivity(navController)
        }
        composable("questions") {
            CommonQuestions(navController)
        }
        composable("account") {
            ProfileDetails(navController)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExploreCityTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigator()
                }
            }
        }
    }
}
