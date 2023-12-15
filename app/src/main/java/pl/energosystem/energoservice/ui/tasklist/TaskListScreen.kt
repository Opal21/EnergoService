package pl.energosystem.energoservice.ui.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.model.Task
import pl.energosystem.energoservice.ui.AppViewModelProvider


@Composable
fun TaskListScreen(
    modifier: Modifier,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onProtocolClicked: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    val openTaskDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    TaskListScreenContent(
        taskList = uiState.value.tasks.filter { !it.completed },
        onTaskClick = {
            openTaskDialog.value = !openTaskDialog.value
            viewModel.openTask(it)
                      },
    )

    when {
        openTaskDialog.value -> {
            val openedTask = uiState.value.openedTask ?: return

            TaskDetailsDialog(
                onDismissRequest = { openTaskDialog.value = false },
                onCall = { viewModel.call(openedTask, context) },
                onNavigate = { viewModel.navigate(openedTask, context) },
                onProtocolClicked = {
                    openTaskDialog.value = false
                    onProtocolClicked(openedTask.id)
                                    },
                taskName = openedTask.title,
                description = openedTask.description,
                errorMessage = uiState.value.error
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
        contentPadding = PaddingValues(8.dp),
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
fun TaskListItem(
    task: Task,
    modifier: Modifier = Modifier,
    onTaskClick: (Task) -> Unit
) {
    Card(
        onClick = { onTaskClick(task) },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Address: " + task.address,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )

            Text(
                text = "Date: " + task.dueDate,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TaskListScreenContentPreview() {
    TaskListScreenContent(
        taskList = listOf(
            Task(
                title = "Wymiana wodomierza",
                dueDate = "24.10.2023",
                description = "Unless your app is receiving pending intents from other apps.",
                address = "1600 Amphitheatre Parkway, Mountain+View, California",
                phoneNumber = "664254824",
                completed = false,
            ),
            Task(
                title = "Naprawa podzielnika",
                dueDate = "24.10.2023",
                description = "Unless your app is receiving pending intents from other apps.",
                address = "1600 Amphitheatre Parkway, Mountain View, California",
                phoneNumber = "664254824",
                completed = false,
            )
        ),
        onTaskClick = {  }
    )
}
