package pl.energosystem.energoservice.ui.protocol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.model.service.ProtocolStorageService
import pl.energosystem.energoservice.model.service.TaskStorageService

class ProtocolViewModel(
    private val protocolStorageService: ProtocolStorageService,
    private val taskStorageService: TaskStorageService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProtocolUiState())
    val uiState: StateFlow<ProtocolUiState> = _uiState

    fun getProtocolDataFromTask(taskId: String) {
        viewModelScope.launch {
            try {
                val task = taskStorageService.getTask(taskId)
                _uiState.value =
                    if (task == null) ProtocolUiState()
                    else
                        ProtocolUiState(
                            title = task.title,
                            commentsTextField = task.description,
                        )
            } catch (e: FirebaseException) {
                _uiState.value = _uiState.value.copy(errorMessage = "Something went wrong!")
            }
        }
    }

    fun saveProtocol(taskId: String?) {
        if (allFieldsAreFull()) {
            val protocol = Protocol(
                address = uiState.value.commentsTextField,
                description = uiState.value.commentsTextField,
                taskId = taskId ?: ""
            )
            viewModelScope.launch {
                try {
                    protocolStorageService.save(protocol)
                    _uiState.value = _uiState.value.copy(errorMessage = "Saved correctly")
                    taskId?.let { markTaskDone(taskId) }
                } catch (e: FirebaseException) {
                    _uiState.value = _uiState.value.copy(errorMessage = "Something went wrong!")
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Fill all fields before saving!")
        }
    }

    private suspend fun markTaskDone(taskId: String) {
        val task = taskStorageService.getTask(taskId) ?: return
        taskStorageService.update(task.copy(completed = true))
    }

    private fun allFieldsAreFull() =
        uiState.value.locatorNameTextField.isNotBlank() && uiState.value.serviceType != null

    fun onLocatorsNameChange(newName: String) {
        if (newName != uiState.value.locatorNameTextField)
            _uiState.value = _uiState.value.copy(locatorNameTextField = newName)
    }

    fun onCommentsChange(newComments: String) {
        if (newComments != uiState.value.commentsTextField)
            _uiState.value = _uiState.value.copy(commentsTextField = newComments)
    }

    fun onServiceTypeChange(newServiceType: ServiceType) {
        if (newServiceType != uiState.value.serviceType)
            _uiState.value = _uiState.value.copy(serviceType = newServiceType)
    }
}

enum class ServiceType {
    INSTALLATION,
    REPLACEMENT,
    FIX
}

data class ProtocolUiState(
    val title: String = "",
    val locatorNameTextField: String = "",
    val commentsTextField: String = "",
    val serviceType: ServiceType? = null,
    val errorMessage: String = "",
)