package com.example.explorecity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
                setContent {
                    val navController = rememberNavController()
                    val context = LocalContext.current

                    LaunchedEffect(Unit) {
                        when (getLastPage(context)) {
                            "home" -> navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                            "host" -> navController.navigate("host_home") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                            "login" -> navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                            else -> { /* Stay on the current start destination */ }
                        }
                    }

                    getLastPage(context)?.let {
                        NavHost(navController = navController, startDestination = it) {
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
                }
            }
        }
    }
}

fun saveLastPage(context: Context, page: String) {
    val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("lastPage", page)
        apply()
    }
}

fun getLastPage(context: Context): String? {
    val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    return sharedPref.getString("lastPage", null) // Default is null
}
