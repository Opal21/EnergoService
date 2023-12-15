package pl.energosystem.energoservice.ui.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TaskDetailsDialog(
    onDismissRequest: () -> Unit,
    onCall: () -> Unit,
    onNavigate: () -> Unit,
    onProtocolClicked: () -> Unit,
    taskName: String,
    description: String,
    errorMessage: String?,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp),
            ) {
                Text(
                    text = taskName,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(text = description)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {

                    NavigateButton(
                        onNavigate = onNavigate,
                        Modifier.weight(1f)
                    )

                    CallButton(
                        onCall = onCall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                    )
                }

                CreateProtocolButton(
                    onProtocolClicked = onProtocolClicked,
                    modifier = Modifier
                )

                DismissDialogButton(
                    onDismissRequest = onDismissRequest
                )

                if (errorMessage != null) ErrorMessageText(errorMessage = errorMessage)
            }
        }
    }
}

@Composable
fun CallButton(
    onCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onCall,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Navigate icon"
            )
            Text(text = "Call")
        }
    }
}

@Composable
fun NavigateButton(
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onNavigate,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Navigate icon"
            )
            Text(text = "Navigate")
        }
    }
}

@Composable
fun CreateProtocolButton(
    onProtocolClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onProtocolClicked,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = "Navigate icon"
            )
            Text(text = "Create protocol")
        }
    }
}

@Composable
fun DismissDialogButton(
    onDismissRequest: () -> Unit
) {
    TextButton(
        onClick = onDismissRequest
    ) {
        Text("Dismiss")
    }
}

@Composable
fun ErrorMessageText(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = errorMessage,
        modifier = modifier
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true, apiLevel = 33)
fun TaskDetailsDialogPreview() {
    TaskDetailsDialog(
        onDismissRequest = {  },
        onCall = {  },
        onNavigate = {  },
        taskName = "Wymiana wodomierza",
        description = "Test dialog text, let me know what you think.",
        errorMessage = null,
        onProtocolClicked = {  }
    )
}