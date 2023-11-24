package com.example.explorecity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explorecity.api.classes.event.Location
import com.example.explorecity.api.models.ApiViewModel
import com.example.explorecity.api.models.UserInformation
import com.example.explorecity.ui.theme.ExploreCityTheme
import com.google.android.gms.location.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            HomeActivity(navController, viewModel)
        }
        composable("host_home") {
            HostActivity(navController, viewModel)
        }
        composable("host_prof") {
            HostProfileActivity(navController)
        }
        composable("create_event") {
            EventCreationActivity(navController)
        }
        composable("event_details") {
            DetailsActivity(navController, viewModel)
        }
        composable("details") {
            DetailsActivity(navController, viewModel)
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
    // View Model for the API
    private val apiViewModel: ApiViewModel by viewModels()

    // User Location Gathering
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false

    // User Info Data Store instance
    private val userInfo = UserInformation.instance

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExploreCityTheme {

                // Sets up user location
                val context = LocalContext.current
                var currentLocation by remember {
                    mutableStateOf(Location(0.toDouble(), 0.toDouble()))
                }
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        for (lo in p0.locations) {
                            // Update UI with location data
                            currentLocation = Location(lo.latitude, lo.longitude)
                        }
                    }
                }

                // Get user permission
                val launcherMultiplePermissions = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissionsMap ->
                    val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                    if (areGranted) {
                        locationRequired = true
                        startLocationUpdates()
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }

                val permissions = arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                GlobalScope.launch {
                    if (permissions.all {
                            ContextCompat.checkSelfPermission(
                                context,
                                it
                            ) == PackageManager.PERMISSION_GRANTED
                        }) {
                        // Get the location
                        startLocationUpdates()
                    } else {
                        launcherMultiplePermissions.launch(permissions)
                    }
                    while (true) {
                        delay(5000)
                        Log.d("LOCATION", "Lat: ${currentLocation.lat} | Lon: ${currentLocation.lon}")
                        userInfo.setUserLocation(currentLocation.lat, currentLocation.lon)
                    }
                }

                GlobalScope.launch {
                    while (true) {
                        delay(60_000)
                        apiViewModel.sendUpdatedLocation()
                        Log.d("LOCATION", "Sent user location update")
                    }
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigator(apiViewModel)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 5000
                priority = Priority.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
}
