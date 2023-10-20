package pl.energosystem.energoservice.ui.protocollist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.model.service.ProtocolStorageService

class ProtocolListViewModel(protocolStorageService: ProtocolStorageService) : ViewModel() {

    val uiState: StateFlow<ProtocolListUiState> =
        protocolStorageService.protocols
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