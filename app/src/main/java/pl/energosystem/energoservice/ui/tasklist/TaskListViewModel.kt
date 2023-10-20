package pl.energosystem.energoservice.ui.tasklist

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.model.Task
import pl.energosystem.energoservice.model.service.TaskStorageService

class TaskListViewModel(
    private val tasksStorageService: TaskStorageService
) : ViewModel() {

    private var openedTask: Task? = null

    val uiState: StateFlow<TaskListUiState> =
        tasksStorageService.tasks
            .filterNotNull()
            .map { TaskListUiState(
                tasks = it,
                openedTask = openedTask
            ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TaskListUiState(emptyList())
            )


    fun markTaskAsDone(task: Task) {
        val doneTask = task.copy(completed = true)
        viewModelScope.launch {
            tasksStorageService.update(doneTask)
        }
    }

    fun openTask(task: Task) {
        openedTask = task
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val openedTask: Task? = null,
    val loading: Boolean = false,
    val error: String? = null
)