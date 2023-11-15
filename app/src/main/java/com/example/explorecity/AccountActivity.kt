package com.example.explorecity


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

@Composable
fun AutocompleteExample() {
    var text by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var coord by remember { mutableStateOf(LatLng(0.0,0.0)) }
    val context = LocalContext.current

    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            text = place.name ?: ""
            address = place.address ?:""
            coord = (place.latLng ?: LatLng(0.0, 0.0))
        }
    }

    // Function to launch the Autocomplete activity
    val launchAutocomplete = {
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyDPFDQebw-Qwis6bU68K_-pmylM4pJc88k")
        }
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
            listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))
            .build(context)
        autocompleteLauncher.launch(intent)
    }

    Column {
        Row {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter location") }
            )
            Button(onClick = { launchAutocomplete() }) {
                Text("Search")
            }
        }
    }
}

@Composable
fun AccountActivity(navController: NavController) {
    AutocompleteExample()
}

