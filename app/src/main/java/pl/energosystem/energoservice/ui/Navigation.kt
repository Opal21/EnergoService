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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.energosystem.energoservice.R
import pl.energosystem.energoservice.ui.EnergoServiceRoute.PROTOCOL_LIST
import pl.energosystem.energoservice.ui.EnergoServiceRoute.SETTINGS
import pl.energosystem.energoservice.ui.EnergoServiceRoute.TASK_LIST
import pl.energosystem.energoservice.ui.protocollist.ProtocolListScreen
import pl.energosystem.energoservice.ui.settings.SettingsScreen
import pl.energosystem.energoservice.ui.tasklist.TaskListScreen

object EnergoServiceRoute {
    const val TASK_LIST = "TaskList"
    const val PROTOCOL_LIST = "ProtocolList"
    const val SETTINGS = "Settings"
}

data class EnergoServiceTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class EnergoServiceNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: EnergoServiceTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

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
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
) {
    AnimatedVisibility(
        visible = bottomBarState.value,
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
    navController: NavHostController = rememberNavController(),
    isLoggedIn: MutableState<Boolean>
) {
    NavHost(
        navController = navController,
        startDestination = TASK_LIST
    ) {
        composable(SETTINGS) {
            SettingsScreen(modifier, isLoggedIn)
        }
        composable(TASK_LIST) {
            TaskListScreen(modifier, onTaskClicked = { })
        }
        composable(PROTOCOL_LIST) {
            ProtocolListScreen(modifier) {
                navController.navigate("protocol/$it")
            }
        }
    }
}
