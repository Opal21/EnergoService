package pl.energosystem.energoservice.ui.tasklist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.data.task.Task
import pl.energosystem.energoservice.ui.AppViewModelProvider
import pl.energosystem.energoservice.ui.protocol.ServiceType
import java.time.LocalDate

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun TaskListScreen(
    modifier: Modifier,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onProtocolClicked: (Int) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    val openTaskDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    TaskListScreenContent(
        taskList = uiState.value.tasks,
        onTaskClick = {
            openTaskDialog.value = !openTaskDialog.value
            viewModel.openTask(it)
                      },
        modifier = modifier
    )
    when {
        openTaskDialog.value -> {
            val openedTask = uiState.value.openedTask ?: return
            TaskDetailsDialog(
                onDismissRequest = { openTaskDialog.value = false },
                onCall = {
                    openTaskDialog.value = false
                    val callIntent = viewModel.getDialIntent(openedTask.phoneNumber)
                    if (callIntent.resolveActivity(context.packageManager) != null) {
                        startActivity(context, callIntent, null)
                    }
                    // Attempt to start an activity that can handle the Intent
                    startActivity(context, callIntent, null)
                },
                onNavigate = {
                    openTaskDialog.value = false
                    val mapIntent = viewModel.getNavigationIntent(openedTask.address)
                    mapIntent.resolveActivity(context.packageManager)
                        ?.let {
                            startActivity(context, mapIntent, null)
                        }

                    // Attempt to start an activity that can handle the Intent
                    startActivity(context, mapIntent, null)
                },
                onProtocolClicked = {
                    openTaskDialog.value = false
                    onProtocolClicked(openedTask.id)
                                    },
                name = openedTask.name,
                description = openedTask.description,
            )
        }
    }
}

@Composable
fun TaskListScreenContent(
    taskList: List<Task>,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        items(taskList) { task ->
            TaskListItem(
                task = task,
                onTaskClick = onTaskClick,
                modifier = modifier
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListItem(task: Task, modifier: Modifier = Modifier, onTaskClick: (Task) -> Unit) {
    Card(
        onClick = { onTaskClick(task) },
        modifier = modifier
            .height(160.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = task.address,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Data: " + task.date,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun TaskDetailsDialog(
    onDismissRequest: () -> Unit,
    onCall: () -> Unit,
    onNavigate: () -> Unit,
    onProtocolClicked: () -> Unit,
    name: String,
    description: String,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(575.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = name)

                Text(text = description)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Button(
                        onClick = onNavigate,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Navigate")
                        }
                    }

                    Button(
                        onClick = onCall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(text = "Call")
                        }
                    }
                }
                Button(
                    onClick = onProtocolClicked,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(text = "Create protocol")
                    }
                }
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TaskListScreenContentPreview() {
    TaskListScreenContent(
        taskList = listOf(
            Task(
                id = 0,
                name = "Wymiana wodomierza",
                description = "Unless your app is receiving pending intents from other apps.",
                address = "1600 Amphitheatre Parkway, Mountain+View, California",
                phoneNumber = "664254824",
                date =  LocalDate.now().toString(),
                isDone = false,
                serviceType = ServiceType.REPLACEMENT
            ),
            Task(
                id = 0,
                name = "Naprawa podzielnika",
                description = "Unless your app is receiving pending intents from other apps.",
                address = "1600 Amphitheatre Parkway, Mountain View, California",
                phoneNumber = "664254824",
                date =  LocalDate.now().toString(),
                isDone = false,
                serviceType = ServiceType.REPLACEMENT
            )
        ),
        onTaskClick = {  }
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun TaskDetailsDialogPreview() {
    TaskDetailsDialog(
        onDismissRequest = {  },
        onCall = {  },
        onNavigate = {  },
        name = "Wymiana wodomierza",
        description = "Test dialog text, let me know what you think.",
        onProtocolClicked = {  }
    )
}
