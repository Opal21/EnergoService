package pl.energosystem.energoservice.ui.protocollist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.data.protocol.Protocol
import pl.energosystem.energoservice.data.protocol.ProtocolsRepository
import pl.energosystem.energoservice.ui.protocol.ServiceType

class ProtocolListViewModel(
    private val protocolsRepository: ProtocolsRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            protocolsRepository.insertProtocol(
                Protocol(
                    id = 0,
                    locatorName = "Jan Kowalski",
                    comments = "Test comment",
                    serviceType = ServiceType.INSTALLATION
                )
            )
        }
    }

    val uiState: StateFlow<ProtocolListUiState> =
        protocolsRepository.getAllProtocolsStream()
            .filterNotNull()
            .map { ProtocolListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProtocolListUiState(emptyList())
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ProtocolListUiState(
    val protocols: List<Protocol>
)