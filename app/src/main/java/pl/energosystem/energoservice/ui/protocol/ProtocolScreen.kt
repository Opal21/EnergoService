package pl.energosystem.energoservice.ui.protocol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.model.Device
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.ui.AppViewModelProvider
import java.time.LocalDate

@Composable
fun ProtocolScreen(
    taskId: String?,
    protocolId: String?,
    modifier: Modifier = Modifier,
    closeProtocol: () -> Unit = {},
) {
    val viewModel: ProtocolViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        taskId?.let { viewModel.getProtocolDataFromTask(it) }
        protocolId?.let { viewModel.getProtocolDataFromProtocol(it) }
        viewModel.createBarcodeScanner(context)
    }

    ProtocolScreenContent(
        protocol = uiState.protocol,
        errorMessage = uiState.errorMessage,
        onLocatorsNameChange = viewModel::onLocatorsNameChange,
        onCommentsChange = viewModel::onCommentsChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onSave = { viewModel.saveProtocol(taskId) },
        closeProtocol = closeProtocol,
        onOldDeviceTypeChange = viewModel::onOldDeviceTypeChange,
        onOldDeviceReadoutChange = viewModel::onOldDeviceReadoutChange,
        onOldDeviceSerialNumberChange = viewModel::onOldDeviceSerialNumberChange,
        onNewDeviceTypeChange = viewModel::onNewDeviceTypeChange,
        onNewDeviceReadoutChange = viewModel::onNewDeviceReadoutChange,
        onNewDeviceSerialNumberChange = viewModel::onNewDeviceSerialNumberChange,
        scanOldDeviceSerialNumber = viewModel::scanOldDeviceSerialNumber,
        scanNewDeviceSerialNumber = viewModel::scanNewDeviceSerialNumber,
        addNewDevice = viewModel::addDevice,
        modifier = modifier
    )
}

@Composable
fun ProtocolScreenContent(
    protocol: Protocol,
    errorMessage: String,
    onLocatorsNameChange: (String) -> Unit,
    onCommentsChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onOldDeviceTypeChange: (String) -> Unit,
    onOldDeviceReadoutChange: (String) -> Unit,
    onOldDeviceSerialNumberChange: (String) -> Unit,
    onNewDeviceTypeChange: (String) -> Unit,
    onNewDeviceReadoutChange: (String) -> Unit,
    onNewDeviceSerialNumberChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    onSave: () -> Unit,
    scanNewDeviceSerialNumber: () -> Unit,
    scanOldDeviceSerialNumber: () -> Unit,
    addNewDevice: () -> Unit,
    closeProtocol: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { ProtocolScreenTopBar(
            closeProtocol = closeProtocol,
            onSave = onSave,
        ) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = protocol.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Address: " + protocol.address,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Locator's info",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            LocatorsNameTextField(
                locatorsNameFieldValue = protocol.locatorsName,
                onLocatorsNameChange = onLocatorsNameChange,
                onNext = onNext
            )

            PhoneNumberField(
                phoneNumberValue = protocol.locatorsPhoneNumber,
                onPhoneNumberChange = onPhoneNumberChange,
                onNext = onNext
            )

            Text(
                text = "Protocol's info",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            DescriptionTextField(
                descriptionFieldValue = protocol.description,
                onDescriptionChange = onDescriptionChange,
                onNext = onNext
            )

            CommentsTextField(
                commentsFieldValue = protocol.comments,
                onCommentsChange = onCommentsChange,
                onNext = onNext
            )

            if (protocol.newDevice != null)
                DeviceInfoFields(
                    deviceAge = "New Device",
                    deviceTypeValue = protocol.newDevice.type,
                    onDeviceTypeChange = onNewDeviceTypeChange,
                    lastReadoutValue = protocol.newDevice.readout,
                    onLastReadoutChange = onNewDeviceReadoutChange,
                    serialNumberValue = protocol.newDevice.serialNumber,
                    onSerialNumberChange = onNewDeviceSerialNumberChange,
                    scanSerialNumber = scanNewDeviceSerialNumber,
                    onNext = onNext,
                    modifier = Modifier
                        .padding(it)
                )

            if (protocol.oldDevice != null)
                DeviceInfoFields(
                    deviceAge = "Old Device",
                    deviceTypeValue = protocol.oldDevice.type,
                    onDeviceTypeChange = onOldDeviceTypeChange,
                    lastReadoutValue = protocol.oldDevice.readout,
                    onLastReadoutChange = onOldDeviceReadoutChange,
                    serialNumberValue = protocol.oldDevice.serialNumber,
                    onSerialNumberChange = onOldDeviceSerialNumberChange,
                    scanSerialNumber = scanOldDeviceSerialNumber,
                    onNext = onNext,
                    modifier = Modifier
                        .padding(it)
                )

            if (protocol.newDevice == null || protocol.oldDevice == null)
                AddDeviceButton(buttonLabel = "Add device", onCreate = addNewDevice)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtocolScreenTopBar(
    closeProtocol: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = "Create protocol") },
        navigationIcon = {
            IconButton(onClick = closeProtocol) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Back"
                )
            }
        },
        actions = { SaveButton(buttonLabel = "Save", onSave = onSave) },
        modifier = modifier
    )
}

@Composable
fun SaveButton(
    buttonLabel: String,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onSave,
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(text = buttonLabel)
    }
}

@Composable
fun LocatorsNameTextField(
    locatorsNameFieldValue: String,
    onLocatorsNameChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = locatorsNameFieldValue,
        onValueChange = onLocatorsNameChange,
        label = { Text(text = "Locators name") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = onNext),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun DescriptionTextField(
    descriptionFieldValue: String,
    onDescriptionChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = descriptionFieldValue,
        onValueChange = onDescriptionChange,
        label = { Text(text = "Description") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = onNext),
        minLines = 3,
        maxLines = 5,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
    )
}

@Composable
fun CommentsTextField(
    commentsFieldValue: String,
    onCommentsChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = commentsFieldValue,
        onValueChange = onCommentsChange,
        label = { Text(text = "Comments") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = onNext),
        minLines = 3,
        maxLines = 5,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
    )
}

@Composable
fun PhoneNumberField(
    phoneNumberValue: String,
    onPhoneNumberChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = phoneNumberValue,
        onValueChange = onPhoneNumberChange,
        label = { Text(text = "Phone number") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = onNext),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
    )
}

@Composable
fun AddDeviceButton(
    buttonLabel: String,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onCreate) {
        Text(text = buttonLabel)
    }
}

@Composable
fun DeviceInfoFields(
    deviceAge: String,
    deviceTypeValue: String,
    onDeviceTypeChange: (String) -> Unit,
    lastReadoutValue: String,
    onLastReadoutChange: (String) -> Unit,
    serialNumberValue: String,
    onSerialNumberChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    scanSerialNumber: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = deviceAge,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        DeviceTypeTextField(
            deviceTypeValue = deviceTypeValue,
            onDeviceTypeChange = onDeviceTypeChange,
            onNext = onNext
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SerialNumberField(
                serialNumberValue = serialNumberValue,
                onSerialNumberChange = onSerialNumberChange,
                onNext = onNext,
                modifier = Modifier
                    .weight(1f)
            )

            ScanSerialNumberButton(
                onClick = scanSerialNumber,
            )
        }

        LastReadOutTextField(
            lastReadoutValue = lastReadoutValue,
            onLastReadoutChange = onLastReadoutChange,
            onNext = onNext
        )
    }
}

@Composable
fun DeviceTypeTextField(
    deviceTypeValue: String,
    onDeviceTypeChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = deviceTypeValue,
        onValueChange = onDeviceTypeChange,
        label = { Text(text = "Device type") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = onNext),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun LastReadOutTextField(
    lastReadoutValue: String,
    onLastReadoutChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = lastReadoutValue,
        onValueChange = onLastReadoutChange,
        label = { Text(text = "Last readout") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = onNext),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SerialNumberField(
    serialNumberValue: String,
    onSerialNumberChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = serialNumberValue,
        onValueChange = onSerialNumberChange,
        label = { Text(text = "Serial number") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = onNext),
        singleLine = true,
        trailingIcon = { Icons.Default.Edit },
        modifier = modifier
            .defaultMinSize(minHeight = 40.dp)
    )
}

@Composable
fun ScanSerialNumberButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(50.dp)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add icon")
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ProtocolScreenContentPreview() {
    ProtocolScreenContent(
        protocol = Protocol(
            title = "Wymiana wodomierza",
            creationDate = LocalDate.now().toString(),
            description = "Zdemontowano stary wodomierz i zamontowano nowy",
            comments = "brak uwag",
            address = "Damrota 7, 44-200 Rybnik",
            locatorsPhoneNumber = "664254824",
            locatorsName = "Jan Kowalski",
            oldDevice = Device(
                type = "wodomierz",
                readout = "22.324",
                serialNumber = "23432534"
            ),
            newDevice = Device(
                type = "wodomierz",
                readout = "22.324",
                serialNumber = "23432534"
            )
        ),
        errorMessage = "test error message",
        onLocatorsNameChange = {  },
        onDescriptionChange = {  },
        onCommentsChange = {  },
        onNext = {  },
        onSave = {  },
        closeProtocol = {  },
        onPhoneNumberChange = {  },
        onOldDeviceReadoutChange =  {  },
        onOldDeviceTypeChange = {  },
        onOldDeviceSerialNumberChange = {  },
        onNewDeviceSerialNumberChange =  {  },
        onNewDeviceTypeChange = {  },
        onNewDeviceReadoutChange = {  },
        scanNewDeviceSerialNumber = {},
        scanOldDeviceSerialNumber = {},
        addNewDevice = {},
    )
}
