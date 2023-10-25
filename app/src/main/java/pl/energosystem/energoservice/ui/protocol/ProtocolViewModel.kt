package pl.energosystem.energoservice.ui.protocol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.model.service.AccountService
import pl.energosystem.energoservice.model.service.ProtocolStorageService
import pl.energosystem.energoservice.model.service.TaskStorageService
import java.time.LocalDate

class ProtocolViewModel(
    private val accountService: AccountService,
    private val protocolStorageService: ProtocolStorageService,
    private val taskStorageService: TaskStorageService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProtocolUiState())
    val uiState: StateFlow<ProtocolUiState> get() = _uiState

    suspend fun getProtocolDataFromTask(taskId: String) {
        try {
            val task = taskStorageService.getTask(taskId)
            _uiState.value =
                if (task == null) ProtocolUiState()
                else
                    ProtocolUiState(
                        protocol = Protocol(
                            title = task.title,
                            creationDate = LocalDate.now().toString(),
                            description = task.description,
                            address = task.address,
                            userId = accountService.currentUserId
                        ),
                    )
        } catch (e: FirebaseException) {
            _uiState.value = _uiState.value.copy(errorMessage = "Something went wrong!")
        }
    }

    fun saveProtocol(taskId: String?) {
        val protocol = uiState.value.protocol.copy(taskId = taskId ?: "")
        viewModelScope.launch {
            try {
                protocolStorageService.save(protocol)
                _uiState.value = _uiState.value.copy(errorMessage = "Saved correctly")
                taskId?.let { markTaskDone(taskId) }
            } catch (e: FirebaseException) {
                _uiState.value = _uiState.value.copy(errorMessage = "Something went wrong!")
            }
        }
    }

    fun onLocatorsNameChange(newName: String) {
        if (newName != uiState.value.protocol.locatorsName){
            val newProtocol = uiState.value.protocol.copy(locatorsName = newName)
            _uiState.value = uiState.value.copy(protocol = newProtocol)
        }
    }

    fun onDescriptionChange(newDescription: String) {
        if (newDescription != uiState.value.protocol.description) {
            val newProtocol = uiState.value.protocol.copy(description = newDescription)
            _uiState.value = uiState.value.copy(protocol = newProtocol)
        }
    }

    fun onCommentsChange(newComments: String) {
        if (newComments != uiState.value.protocol.comments) {
            val newProtocol = uiState.value.protocol.copy(comments = newComments)
            _uiState.value = uiState.value.copy(protocol = newProtocol)
        }
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        if (newPhoneNumber != _uiState.value.protocol.locatorsPhoneNumber) {
            val newProtocol = _uiState.value.protocol.copy(locatorsPhoneNumber = newPhoneNumber)
            _uiState.value = _uiState.value.copy(protocol = newProtocol)
        }
    }

    private suspend fun markTaskDone(taskId: String) {
        val task = taskStorageService.getTask(taskId) ?: return
        taskStorageService.update(task.copy(completed = true))
    }
}

data class ProtocolUiState(
    val protocol: Protocol = Protocol(),
    val errorMessage: String = ""
)
