package pl.energosystem.energoservice.ui.protocol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.data.protocol.Protocol
import pl.energosystem.energoservice.data.protocol.ProtocolsRepository
import pl.energosystem.energoservice.data.task.TasksRepository

class ProtocolViewModel(
    private val protocolsRepository: ProtocolsRepository,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProtocolUiState())
    val uiState: StateFlow<ProtocolUiState> = _uiState


//    init {
//        observeEmails()
//    }
//
//    private fun observeEmails() {
//        viewModelScope.launch {
//            emailsRepository.getAllEmails()
//                .catch { ex ->
//                    _uiState.value = ReplyHomeUIState(error = ex.message)
//                }
//                .collect { emails ->
//                    /**
//                     * We set first email selected by default for first App launch in large-screens
//                     */
//                    _uiState.value = ReplyHomeUIState(
//                        emails = emails,
//                        openedEmail = emails.first()
//                    )
//                }
//        }
//    }

    fun getProtocolData(protocolId: Int) {
        viewModelScope.launch {
            val protocolFlow = protocolsRepository.getProtocolStream(id = protocolId)
            protocolFlow.collect {
                _uiState.value = _uiState.value.copy(commentsTextField = it?.comments ?: "")
            }
        }
    }

    fun saveProtocol() {
        if (allFieldsAreFull()) {
            val protocol = Protocol(
                locatorName = uiState.value.locatorNameTextField,
                comments = uiState.value.commentsTextField,
                serviceType = uiState.value.serviceType!!
            )
            viewModelScope.launch {
                protocolsRepository.insertProtocol(protocol)
                _uiState.value = _uiState.value.copy(errorMessage = "Saved correctly")
            }
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Fill all fields before saving!")
        }
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
    val locatorNameTextField: String = "",
    val commentsTextField: String = "",
    val serviceType: ServiceType? = null,
    val errorMessage: String = "",
)