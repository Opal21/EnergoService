package pl.energosystem.energoservice.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun LogInScreen(
    onSuccessfulLogIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogInViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        Image(imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier
            .size(50.dp)
            .align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = uiState.value.emailFieldText,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),

        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = uiState.value.passwordTextField,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus() 
                }),
            )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onSuccessfulLogIn) {
            Text(text = "Log In")
        }
    }
}

@Composable
@Preview(
    device = "id:pixel_7",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
)
fun LogInScreenPreview() {
    LogInScreen(onSuccessfulLogIn = { /*TODO*/ })
}