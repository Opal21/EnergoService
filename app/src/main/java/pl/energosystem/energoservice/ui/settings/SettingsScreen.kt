package pl.energosystem.energoservice.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun SettingsScreen(
    isLoggedIn: MutableState<Boolean>,
    modifier: Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onLogOutClicked: () -> Unit,
) {

    SettingsScreenContent(
        logOut = {
            isLoggedIn.value = !isLoggedIn.value
            viewModel.logOut(onLogOutClicked)
                 },
        modifier = modifier
    )
}

@Composable
fun SettingsScreenContent(
    logOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Settings")

        Button(
            onClick = logOut
        ) {
            Text(text = "Log Out")
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SettingsScreenContentPreview() {
    SettingsScreenContent(
        logOut = {  }
    )
}