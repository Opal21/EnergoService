package pl.energosystem.energoservice.ui.protocol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.data.protocol.Protocol
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun ProtocolScreen(
    id: String?,
    modifier: Modifier,
    viewModel: ProtocolViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSubmit: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    ProtocolScreenContent(
        errorMessage = uiState.value.errorMessage,
        locatorsNameFieldValue = uiState.value.locatorNameTextField,
        onLocatorsNameChange = viewModel::onLocatorsNameChange,
        commentsFieldValue = uiState.value.commentsTextField,
        onCommentsChange = viewModel::onCommentsChange,
        serviceType = uiState.value.serviceType,
        onServiceTypeChange = viewModel::onServiceTypeChange,
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = viewModel::saveProtocol,
        modifier = modifier
    )
}

@Composable
fun ProtocolScreenContent(
    errorMessage: String,
    locatorsNameFieldValue: String,
    onLocatorsNameChange: (String) -> Unit,
    commentsFieldValue: String,
    onCommentsChange: (String) -> Unit,
    serviceType: ServiceType?,
    onServiceTypeChange: (ServiceType) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    onDone: (Protocol) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.headlineSmall
        )

        LocatorsNameTextField(
            locatorsNameFieldValue = locatorsNameFieldValue,
            onLocatorsNameChange = onLocatorsNameChange,
            onNext = onNext
        )

        CommentsTextField(
            commentsFieldValue = commentsFieldValue,
            onCommentsChange = onCommentsChange,
            onNext = onNext
        )

        ServiceTypeField(
            serviceType = serviceType,
            onServiceTypeChange = onServiceTypeChange,
            onDone = onDone
        )
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
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
    )
}

@Composable
fun ServiceTypeField(
    serviceType: ServiceType?,
    onServiceTypeChange: (ServiceType) -> Unit,
    onDone: (Protocol) -> Unit,
    modifier: Modifier = Modifier
) {
    val types = listOf(ServiceType.INSTALLATION, ServiceType.FIX, ServiceType.REPLACEMENT)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(types[0] ) }

    Column {
        types.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (it == selectedOption),
                        onClick = { onOptionSelected(it) }
                    )
            ) {
                RadioButton(selected = (it == selectedOption), onClick = { onOptionSelected(it) })
                Text(
                    text = it.toString(),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ProtocolScreenContentPreview() {
    ProtocolScreenContent(
        errorMessage = "This is an error!",
        locatorsNameFieldValue = "",
        onLocatorsNameChange = {  },
        commentsFieldValue = "",
        onCommentsChange = {  },
        serviceType = ServiceType.INSTALLATION,
        onServiceTypeChange = {  },
        onNext = {  },
        onDone = {  },
    )
}
