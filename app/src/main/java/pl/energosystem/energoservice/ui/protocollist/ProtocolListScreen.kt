package pl.energosystem.energoservice.ui.protocollist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.ui.AppViewModelProvider

@Composable
fun ProtocolListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProtocolListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onProtocolClicked: (String) -> Unit,
    ) {
    val uiState = viewModel.uiState.collectAsState()

    ProtocolListScreenContent(
        protocols = uiState.value.protocols,
        onProtocolClick = onProtocolClicked,
        modifier = modifier
    )
}

@Composable
fun ProtocolListScreenContent(
    protocols: List<Protocol>,
    onProtocolClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(protocols) {protocol ->
            ProtocolListItem(
                protocol = protocol,
                onProtocolClick = onProtocolClick,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtocolListItem(
    protocol: Protocol,
    onProtocolClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onProtocolClick(protocol.id) },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = protocol.title,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Date: " + protocol.creationDate
            )

            Text(
                text = protocol.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = protocol.comments,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ProtocolListScreenContentPreview() {
    ProtocolListScreenContent(
        protocols = listOf(
            Protocol(
                title = "Wymiana wodomierza",
                creationDate = "19.10.2023",
                description = "Zdemontować stary wodomierz i zamontować nowy.",
                comments = "Lokator nie był obecny",
                address = "Testowa 13a 44-200 Rybnik",
                locatorsPhoneNumber = "664254824",
                locatorsName = "Jan Kowalski"
            ),
            Protocol(
                title = "Wymiana wodomierza",
                creationDate = "19.10.2023",
                description = "Zdemontować stary wodomierz i zamontować nowy.",
                comments = "Lokator nie był obecny",
                address = "Testowa 13a 44-200 Rybnik",
                locatorsPhoneNumber = "664254824",
                locatorsName = "Jan Kowalski"
            )
        ),
        onProtocolClick = {  }
    )
}