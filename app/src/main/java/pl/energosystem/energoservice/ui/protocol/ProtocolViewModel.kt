package pl.energosystem.energoservice.ui.protocol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.data.protocol.Protocol
import pl.energosystem.energoservice.data.protocol.ProtocolsRepository

class ProtocolViewModel(
    private val protocolsRepository: ProtocolsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProtocolUiState())
    val uiState: StateFlow<ProtocolUiState> = _uiState

    fun saveProtocol(protocol: Protocol) {
        if (allFieldsAreFull()) {
            viewModelScope.launch {
                protocolsRepository.insertProtocol(protocol)
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