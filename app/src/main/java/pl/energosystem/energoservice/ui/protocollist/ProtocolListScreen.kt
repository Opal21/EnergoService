package pl.energosystem.energoservice.ui.protocollist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProtocolListScreen(modifier: Modifier = Modifier, onProtocolClicked: () -> Unit) {
    Text(text = "List of protocols")
}