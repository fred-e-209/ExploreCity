package com.example.explorecity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.UserInformation
import com.example.explorecity.ui.theme.ExploreCityTheme

@Composable
fun AppNavigator(viewModel: ApiViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginActivity(navController, viewModel)
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
        composable("host_prof") {
            HostProfileActivity(navController)
        }
        composable("create_event") {
            EventCreationActivity(navController)
        }
        composable("event_details") {
            DetailsActivity(navController)
        }
    }
}

class MainActivity : ComponentActivity() {
    // View Model for the API
    private val apiViewModel: ApiViewModel by viewModels()

    // User Info Data Store instance
    val userInfo = UserInformation.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExploreCityTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigator(apiViewModel)
                }
            }
        }
    }
}
