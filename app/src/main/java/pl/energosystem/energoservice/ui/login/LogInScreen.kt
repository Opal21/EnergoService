package pl.energosystem.energoservice.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.R
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun LogInScreen(
    onSuccessfulLogIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogInViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LogInScreenContent(
        emailFieldValue = uiState.value.emailFieldText,
        onEmailChange = viewModel::onEmailChange,
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        passwordFieldValue = uiState.value.passwordTextField,
        onPasswordChange = viewModel::onPasswordChange,
        onDone = { focusManager.clearFocus() },
        onLogInClick = { viewModel.logIn(onSuccessfulLogIn) },
        modifier = modifier,
    )
}

@Composable
fun LogInScreenContent(
    emailFieldValue: String,
    onEmailChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    passwordFieldValue: String,
    onPasswordChange: (String) -> Unit,
    onDone: KeyboardActionScope.() -> Unit,
    onLogInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        CompanyIcon()

        EmailTextField(
            emailFieldValue = emailFieldValue,
            onEmailChange = onEmailChange,
            onNext = onNext,
            modifier = modifier
        )

        PasswordTextField(
            passwordFieldValue = passwordFieldValue,
            onPasswordChange = onPasswordChange,
            onDone = onDone,
            modifier = modifier
        )

        LogInButton(onLogInClick = onLogInClick)
    }
}

@Composable
fun CompanyIcon(modifier: Modifier = Modifier) {
    Image(
        imageVector = Icons.Default.Settings,
        contentDescription = null,
        modifier = modifier
            .size(80.dp)
    )
}

@Composable
fun EmailTextField(
    emailFieldValue: String,
    onEmailChange: (String) -> Unit,
    onNext: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = emailFieldValue,
        onValueChange = onEmailChange,
        label = { Text(text = stringResource(R.string.email_field_label)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = onNext),
        modifier = modifier,
        )
}

@Composable
fun PasswordTextField(
    passwordFieldValue: String,
    onPasswordChange: (String) -> Unit,
    onDone: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = passwordFieldValue,
        onValueChange = onPasswordChange,
        label = { Text(text = stringResource(R.string.password_field_label)) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = onDone),
        modifier = modifier,
    )
}

@Composable
fun LogInButton(
    onLogInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onLogInClick,
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.log_in_button_text))
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true,)
fun LogInScreenContentPreview() {
    LogInScreenContent(
        emailFieldValue = "",
        onEmailChange = {  },
        onNext = {  },
        passwordFieldValue = "",
        onPasswordChange = {  },
        onDone = {  },
        onLogInClick = {  },
    )
}
