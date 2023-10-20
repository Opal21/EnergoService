package pl.energosystem.energoservice.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.energosystem.energoservice.R
import pl.energosystem.energoservice.ui.EnergoServiceRoute.LOG_IN
import pl.energosystem.energoservice.ui.EnergoServiceRoute.PROTOCOL
import pl.energosystem.energoservice.ui.EnergoServiceRoute.PROTOCOL_LIST
import pl.energosystem.energoservice.ui.EnergoServiceRoute.SETTINGS
import pl.energosystem.energoservice.ui.EnergoServiceRoute.SPLASH
import pl.energosystem.energoservice.ui.EnergoServiceRoute.TASK_LIST
import pl.energosystem.energoservice.ui.login.LogInScreen
import pl.energosystem.energoservice.ui.protocol.ProtocolScreen
import pl.energosystem.energoservice.ui.protocollist.ProtocolListScreen
import pl.energosystem.energoservice.ui.settings.SettingsScreen
import pl.energosystem.energoservice.ui.splash.SplashScreen
import pl.energosystem.energoservice.ui.tasklist.TaskListScreen

object EnergoServiceRoute {
    const val TASK_LIST = "TaskList"
    const val PROTOCOL_LIST = "ProtocolList"
    const val SETTINGS = "Settings"
    const val LOG_IN = "LogIn"
    const val PROTOCOL = "Protocol"
    const val SPLASH = "Splash"
}

data class EnergoServiceTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    EnergoServiceTopLevelDestination(
        route = TASK_LIST,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = R.string.tab_task_list
    ),
    EnergoServiceTopLevelDestination(
        route = PROTOCOL_LIST,
        selectedIcon = Icons.Default.List,
        unselectedIcon = Icons.Default.List,
        iconTextId = R.string.tab_protocol_list
    ),
    EnergoServiceTopLevelDestination(
        route = SETTINGS,
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Default.Settings,
        iconTextId = R.string.tab_settings
    )
)

@Composable
fun EnergoServiceBottomNavBar(
    selectedDestination: String,
    navController: NavHostController
) {
    AnimatedVisibility(
        visible = selectedDestination != LOG_IN,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                TOP_LEVEL_DESTINATIONS.forEach { destination ->
                    NavigationBarItem(
                        selected = selectedDestination == destination.route,
                        onClick = { navController.navigate(destination.route) },
                        icon = {
                            Icon(
                                imageVector = destination.selectedIcon,
                                contentDescription = stringResource(id = destination.iconTextId)
                            )
                        },
                        label = { Text(text = stringResource(id = destination.iconTextId)) }
                    )
                }
            }
        }
    )
}

@Composable
fun EnergoServiceNavHost(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SPLASH
    ) {
        composable(SPLASH) {
            SplashScreen(goFurther = { route ->
                navController.navigate(route) {
                    launchSingleTop = true
                    popUpTo(SPLASH) { inclusive = true }
                }
            })
        }

        composable(LOG_IN) {
            LogInScreen(modifier) {
                navController.navigate(TASK_LIST) {
                    launchSingleTop = true
                    popUpTo(LOG_IN) { inclusive = true }
                }
            }
        }
        composable(SETTINGS) {
            SettingsScreen(modifier) {
                navController.navigate(LOG_IN) {
                    launchSingleTop = true
                    popUpTo(SETTINGS) { inclusive = true }
                }
            }
        }
        composable(TASK_LIST) {
            TaskListScreen(modifier) {
                navController.navigate("protocol/${it}") {
                    launchSingleTop = true
                    popUpTo(TASK_LIST) { inclusive = true }
                }
            }
        }
        composable(PROTOCOL_LIST) {
            ProtocolListScreen(modifier) {
                navController.navigate("protocol/${it}") {
                    launchSingleTop = true
                    popUpTo(PROTOCOL_LIST) { inclusive = true }
                }
            }
        }
        composable(
            "$PROTOCOL/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
            ProtocolScreen(id = it.arguments?.getString("id")) {
                navController.popBackStack()
            }
        }
    }
}
