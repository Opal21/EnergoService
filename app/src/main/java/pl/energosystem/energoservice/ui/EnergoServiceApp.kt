package pl.energosystem.energoservice.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pl.energosystem.energoservice.ui.login.LogInScreen

@Composable
fun EnergoServiceApp() {
    val navController = rememberNavController()
    val isLoggedIn = rememberSaveable { mutableStateOf(false) }
    // Subscribe to navBackStackEntry, required to get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: EnergoServiceRoute.TASK_LIST

    if (isLoggedIn.value) {
        Scaffold(
            bottomBar = {
                EnergoServiceBottomNavBar(
                    selectedDestination,
                    navController,
                    isLoggedIn,
                )
            }
        ) {
            EnergoServiceNavHost(Modifier.padding(it), navController, isLoggedIn)
        }
    } else {
        LogInScreen(onSuccessfulLogIn = { isLoggedIn.value = true })
    }


}

@Composable
@Preview
fun EnergoServiceAppPreview() {
    EnergoServiceApp()
}
