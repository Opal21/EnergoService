package pl.energosystem.energoservice.ui.tasklist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import pl.energosystem.energoservice.ui.login.LogInScreen

@Composable
fun TaskListScreen(
    modifier: Modifier,
    onTaskClicked: () -> Unit) {
    Text(text = "List of tasks")
}

