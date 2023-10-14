package pl.energosystem.energoservice.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun EnergoServiceApp() {
    val navController = rememberNavController()
    // Subscribe to navBackStackEntry, required to get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: EnergoServiceRoute.TASK_LIST
    Scaffold(
        bottomBar = {
            EnergoServiceBottomNavBar(
                selectedDestination,
                navController,
            )
        }
    ) {
        EnergoServiceNavHost(Modifier.padding(it), navController)
    }
}

@Composable
@Preview
fun EnergoServiceAppPreview() {
    EnergoServiceApp()
}
