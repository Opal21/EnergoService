package pl.energosystem.energoservice.ui.tasklist

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.data.task.Task
import pl.energosystem.energoservice.ui.AppViewModelProvider
import java.time.LocalDate

@Composable
fun TaskListScreen(
    modifier: Modifier,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val openedTask = viewModel.uiState.collectAsState().value.openedTask
    if (openedTask != null) {
        TaskDetailsScreen(
            task = openedTask,
            getNavigationIntent = viewModel::getNavigationIntent,
            getDialIntent = viewModel::getDialIntent,
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(text = "List of all tasks")
            }

            item {
                TaskListItem(
                    task = Task(
                        id = 0,
                        shortDescription = "Short description",
                        longDescription = "Unless your app is receiving pending intents from other apps, the above methods to create a PendingIntent are probably the only PendingIntent methods you'll ever need.\n" +
                                "\n" +
                                "Each method takes the current app Context, the Intent you want to wrap, and one or more flags that specify how the intent should be used (such as whether the intent can be used more than once).\n" +
                                "\n" +
                                "For more information about using pending intents, see the documentation for each of the respective use cases, such as in the Notifications and App Widgets API guides.",
                        address = "1600 Amphitheatre Parkway, Mountain+View, California",
                        phoneNumber = "664254824",
                        date =  LocalDate.now().toString(),
                        isDone = false,
                    ),
                    modifier = modifier,
                    openTask = viewModel::openTask
                )
                Spacer(Modifier.height(8.dp))
            }

            items(items = viewModel.uiState.value.tasks, key = { it.id }) {task ->
                TaskListItem(
                    task = task,
                    modifier = modifier,
                    openTask = viewModel::openTask
                )
                Spacer(Modifier.height(8.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListItem(task: Task, modifier: Modifier = Modifier, openTask: (Task) -> Unit) {
    Card(
        onClick = { openTask(task) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = task.shortDescription)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = task.address)
                Text(text = task.date)
            }
        }
    }
}

@Composable
fun TaskDetailsScreen(
    task: Task,
    getNavigationIntent: (String) -> Intent,
    getDialIntent: (String) -> Intent,
) {
    val context = LocalContext.current
    // Task long description
    Column {
        Text(text = task.longDescription)
        Row {
            Button(
                onClick = {
                    val mapIntent = getNavigationIntent(task.address)
                    mapIntent.resolveActivity(context.packageManager)?.let {
                        startActivity(context, mapIntent, null)
                    }

                    // Attempt to start an activity that can handle the Intent
                    startActivity(context, mapIntent, null)
                },
                modifier = Modifier.width(150.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(imageVector = Icons.Default.LocationOn, contentDescription = "Navigate")
                    Text(text = "Navigate")
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {
                    val dialIntent = getDialIntent(task.phoneNumber)
                    // Attempt to start an activity that can handle the Intent
                    startActivity(context, dialIntent, null)
                },
                modifier = Modifier.width(150.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(imageVector = Icons.Default.Phone, contentDescription = "Call")
                    Text(text = "Call")
                }
            }
        }
    }
}