package pl.energosystem.energoservice.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun SettingsScreen(
    modifier: Modifier,
    isLoggedIn: MutableState<Boolean>,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onLogOutClicked: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Settings")

        Button(onClick = { isLoggedIn.value = viewModel.logOut(onLogOutClicked) }
        ) {
            Text(text = "Log Out")
        }
    }
}
