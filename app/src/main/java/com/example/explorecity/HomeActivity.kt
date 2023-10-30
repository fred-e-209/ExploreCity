package com.example.explorecity

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            title = "My Events",
            selectedIcon = Icons.Filled.List,
            route = "events",
            unselectedIcon = Icons.Outlined.List,
        ),
        BottomNavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            route = "search",
            unselectedIcon = Icons.Outlined.Search,
        ),
        BottomNavigationItem(
            title = "Explore",
            selectedIcon = Icons.Filled.LocationOn,
            route = "explore",
            unselectedIcon = Icons.Outlined.LocationOn,
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            route = "profile",
            unselectedIcon = Icons.Outlined.Person,
        )

    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navBarController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold (
            bottomBar = {
                NavigationBar  {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navBarController.navigate(item.route) {
                                    // Prevents re-navigating to the same destination
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            label = { Text(item.title) },
                            icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        }else item.unselectedIcon
                                        , contentDescription = "Nav Bar Icon"
                                    )
                            })
                    }
                }
            }
        ) {
            NavHost(navBarController, startDestination = "events") {
                composable("events") { MyEventsActivity(navBarController) }
                composable("search") { SearchActivity() }
                composable("explore") { ExploreActivity() }
                composable("profile") { ProfileActivity(navController)}
                composable("details") { DetailsActivity(navBarController)}
        }
    }
}
}