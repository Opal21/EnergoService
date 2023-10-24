package pl.energosystem.energoservice.ui.tasklist

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.model.Task
import pl.energosystem.energoservice.model.service.TaskStorageService

class TaskListViewModel(
    private val tasksStorageService: TaskStorageService
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            tasksStorageService.tasks
                .catch {ex ->
                    _uiState.value = TaskListUiState(error = ex.message)
                }
                .collect {
                    _uiState.value = TaskListUiState(tasks = it)
                }
        }
    }

    fun call(task: Task, context: Context) {
        val callIntent = getDialIntent(task.phoneNumber)
        // Attempt to start an activity that can handle the Intent
        try {
            ContextCompat.startActivity(context, callIntent, null)
        } catch (e: ActivityNotFoundException) {
            _uiState.value = _uiState.value.copy(error = e.message)
        }
    }

    fun navigate(task: Task, context: Context) {
        val mapIntent = getNavigationIntent(task.address)
        // Attempt to start an activity that can handle the Intent
        try {
            ContextCompat.startActivity(context, mapIntent, null)
        } catch (e: ActivityNotFoundException) {
            _uiState.value = _uiState.value.copy(error = e.message)
        }
    }

    fun openTask(task: Task?) {
        _uiState.value = _uiState.value.copy(openedTask = task)
    }

    private fun getNavigationIntent(address: String) : Intent {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        return mapIntent
    }

    private fun getDialIntent(phoneNumber: String) = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

}

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val openedTask: Task? = null,
    val loading: Boolean = false,
    val error: String? = null
)