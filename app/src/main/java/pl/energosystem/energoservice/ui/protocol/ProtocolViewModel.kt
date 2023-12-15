package pl.energosystem.energoservice.ui.protocol

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.model.Device
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

    private lateinit var scanner: GmsBarcodeScanner

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
                            locatorsPhoneNumber = task.phoneNumber,
                            address = task.address,
                            userId = accountService.currentUserId
                        ),
                    )
        } catch (e: FirebaseException) {
            _uiState.value = _uiState.value.copy(errorMessage = "Something went wrong!")
        }
    }

    suspend fun getProtocolDataFromProtocol(protocolId: String) {
        try {
            val protocol = protocolStorageService.getProtocol(protocolId)
            _uiState.value =
                if (protocol == null) ProtocolUiState()
                else
                    ProtocolUiState(
                        protocol = protocol,
                    )
        } catch (e: FirebaseException) {
            _uiState.value = _uiState.value.copy(errorMessage = "Something went wrong!")
        }
    }

    fun createBarcodeScanner(context: Context) {
        val options = GmsBarcodeScannerOptions.Builder()
            .enableAutoZoom()
            .build()

        scanner = GmsBarcodeScanning.getClient(context, options)
    }

    fun saveProtocol(taskId: String?) {
        val protocol = uiState.value.protocol.copy(taskId = taskId ?: "")
        viewModelScope.launch {
            try {
                if (protocol.id.isNotBlank() && protocolStorageService.getProtocol(protocol.id) != null) {
                    protocolStorageService.update(protocol)
                } else {
                    protocolStorageService.save(protocol)
                }
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

    fun onOldDeviceTypeChange(newDeviceType: String) {
        var oldDevice = _uiState.value.protocol.oldDevice
        if (oldDevice != null) {
            if (newDeviceType != oldDevice.type) {
                oldDevice = oldDevice.copy(type = newDeviceType)
                _uiState.value = _uiState.value.copy(
                    protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
                )
            }
        } else {
            oldDevice = Device(type = newDeviceType)
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
            )
        }
    }

    fun onOldDeviceReadoutChange(newReadout: String) {
        var oldDevice = _uiState.value.protocol.oldDevice
        if (oldDevice != null) {
            if (newReadout != oldDevice.readout) {
                oldDevice = oldDevice.copy(readout = newReadout)
                _uiState.value = _uiState.value.copy(
                    protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
                )
            }
        } else {
            oldDevice = Device(readout = newReadout)
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
            )
        }
    }

    fun onOldDeviceSerialNumberChange(newSerialNumber: String) {
        var oldDevice = _uiState.value.protocol.oldDevice
        if (oldDevice != null) {
            if (newSerialNumber != oldDevice.serialNumber) {
                oldDevice = oldDevice.copy(serialNumber = newSerialNumber)
                _uiState.value = _uiState.value.copy(
                    protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
                )
            }
        } else {
            oldDevice = Device(serialNumber = newSerialNumber)
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
            )
        }
    }

    fun onNewDeviceTypeChange(newDeviceType: String) {
        var newDevice = _uiState.value.protocol.newDevice
        if (newDevice != null) {
            if (newDeviceType != newDevice.type) {
                newDevice = newDevice.copy(type = newDeviceType)
                _uiState.value = _uiState.value.copy(
                    protocol = _uiState.value.protocol.copy(newDevice = newDevice)
                )
            }
        } else {
            newDevice = Device(type = newDeviceType)
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(newDevice = newDevice)
            )
        }
    }

    fun onNewDeviceReadoutChange(newReadout: String) {
        var newDevice = _uiState.value.protocol.newDevice
        if (newDevice != null) {
            if (newReadout != newDevice.readout) {
                newDevice = newDevice.copy(readout = newReadout)
                _uiState.value = _uiState.value.copy(
                    protocol = _uiState.value.protocol.copy(newDevice = newDevice)
                )
            }
        } else {
            newDevice = Device(readout = newReadout)
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(newDevice = newDevice)
            )
        }

    }

    fun onNewDeviceSerialNumberChange(newSerialNumber: String) {
        var newDevice = _uiState.value.protocol.newDevice
        if (newDevice != null) {
            if (newSerialNumber != newDevice.serialNumber) {
                newDevice = newDevice.copy(serialNumber = newSerialNumber)
                _uiState.value = _uiState.value.copy(
                    protocol = _uiState.value.protocol.copy(newDevice = newDevice)
                )
            }
        } else {
            newDevice = Device(serialNumber = newSerialNumber)
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(newDevice = newDevice)
            )
        }
    }

    fun scanOldDeviceSerialNumber() {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                println(" Raw value: " + barcode.rawValue)
                val serialNum = barcode.rawValue ?: ""
                var oldDevice = _uiState.value.protocol.oldDevice
                if (oldDevice != null) {
                    if (serialNum != oldDevice.serialNumber) {
                        oldDevice = oldDevice.copy(serialNumber = serialNum)
                        _uiState.value = _uiState.value.copy(
                            protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
                        )
                    }
                } else {
                    oldDevice = Device(serialNumber = serialNum)
                    _uiState.value = _uiState.value.copy(
                        protocol = _uiState.value.protocol.copy(oldDevice = oldDevice)
                    )
                }
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
            }
    }

    fun scanNewDeviceSerialNumber() {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                println("Raw value: " + barcode.rawValue)
                val serialNum = barcode.rawValue ?: ""

                var newDevice = _uiState.value.protocol.newDevice
                if (newDevice != null) {
                    if (serialNum != newDevice.serialNumber) {
                        newDevice = newDevice.copy(serialNumber = serialNum)
                        _uiState.value = _uiState.value.copy(
                            protocol = _uiState.value.protocol.copy(newDevice = newDevice)
                        )
                    }
                } else {
                    newDevice = Device(serialNumber = serialNum)
                    _uiState.value = _uiState.value.copy(
                        protocol = _uiState.value.protocol.copy(newDevice = newDevice)
                    )
                }
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
            }
    }

    fun addDevice() {
        if (_uiState.value.protocol.newDevice == null){
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(newDevice = Device())
            )
        } else if(_uiState.value.protocol.oldDevice == null) {
            _uiState.value = _uiState.value.copy(
                protocol = _uiState.value.protocol.copy(oldDevice = Device())
            )
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
