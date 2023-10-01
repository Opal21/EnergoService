package pl.energosystem.energoservice.ui.protocollist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun ProtocolListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProtocolListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onProtocolClicked: () -> Unit) {
    Text(text = "List of protocols")
}