package pl.energosystem.energoservice.ui.protocol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.ui.AppViewModelProvider
import java.time.LocalDate

@Composable
fun ProtocolScreen(
    id: String?,
    modifier: Modifier = Modifier,
    closeProtocol: () -> Unit = {},
) {
    val viewModel: ProtocolViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        id?.let { viewModel.getProtocolDataFromTask(it) }
    }

    ProtocolScreenContent(
        protocol = uiState.protocol,
        errorMessage = uiState.errorMessage,
        onLocatorsNameChange = viewModel::onLocatorsNameChange,
        onCommentsChange = viewModel::onCommentsChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onSave = { viewModel.saveProtocol(id) },
        closeProtocol = closeProtocol,
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
    onNext: KeyboardActionScope.() -> Unit,
    onSave: () -> Unit,
    closeProtocol: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { ProtocolScreenTopBar(
            closeProtocol = closeProtocol,
            onSave = onSave,
            modifier = modifier
        ) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = protocol.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = protocol.address,
                style = MaterialTheme.typography.bodyMedium
            )

            LocatorsNameTextField(
                locatorsNameFieldValue = protocol.locatorsName,
                onLocatorsNameChange = onLocatorsNameChange,
                onNext = onNext
            )

            DescriptionTextField(
                descriptionFieldValue = protocol.description,
                onDescriptionChange = onDescriptionChange,
                onNext = onNext
            )

            PhoneNumberField(
                phoneNumberValue = protocol.locatorsPhoneNumber,
                onPhoneNumberChange = onPhoneNumberChange,
                onNext = onNext
            )

            CommentsTextField(
                commentsFieldValue = protocol.comments,
                onCommentsChange = onCommentsChange,
                onNext = onNext
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium
            )
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
        ),
        errorMessage = "test error message",
        onLocatorsNameChange = {  },
        onDescriptionChange = {  },
        onCommentsChange = {  },
        onNext = {  },
        onSave = {  },
        closeProtocol = {  },
        onPhoneNumberChange = {  },
    )
}
