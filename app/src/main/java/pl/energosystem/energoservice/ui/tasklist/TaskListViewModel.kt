package pl.energosystem.energoservice.ui.tasklist

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.data.task.Task
import pl.energosystem.energoservice.data.task.TasksRepository
import pl.energosystem.energoservice.ui.protocol.ServiceType
import java.time.LocalDate

class TaskListViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    init {
        observeTasks()
        viewModelScope.launch {
            tasksRepository.insertTask(
                Task(
                    id = 0,
                    name = "Task name",
                    description = "Unless your app is receiving pending intents from other apps, the above methods to create a PendingIntent are probably the only PendingIntent methods you'll ever need.",
                    address = "1600 Amphitheatre Parkway, Mountain+View, California",
                    phoneNumber = "664254824",
                    date =  LocalDate.now().toString(),
                    isDone = false,
                    ServiceType.INSTALLATION
                )
            )
        }
    }

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(TaskListUiState(loading = true))
    val uiState: StateFlow<TaskListUiState> = _uiState

    private fun observeTasks() {
        viewModelScope.launch{
            tasksRepository.getAllTasksStream()
                .catch { error ->
                    _uiState.value = TaskListUiState(error = error.message)
                }
                .collect {tasks ->
                    _uiState.value = TaskListUiState(
                        tasks = tasks
                    )
                }
        }
    }

    fun markTaskAsDone(task: Task) {
        val doneTask = Task(
            id = task.id,
            name = task.name,
            description = task.description,
            address = task.address,
            phoneNumber = task.phoneNumber,
            date = task.date,
            isDone = true,
            serviceType = task.serviceType
        )
        viewModelScope.launch {
            tasksRepository.updateTask(doneTask)
        }
    }

    fun openTask(task: Task) {
        _uiState.value = _uiState.value.copy(openedTask = task)
    }

    fun getNavigationIntent(address: String) : Intent {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        return mapIntent
    }

    fun getDialIntent(phoneNumber: String) = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
}

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val openedTask: Task? = null,
    val loading: Boolean = false,
    val error: String? = null
)