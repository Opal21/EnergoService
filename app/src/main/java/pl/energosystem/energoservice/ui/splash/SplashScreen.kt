package pl.energosystem.energoservice.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import pl.energosystem.energoservice.ui.AppViewModelProvider

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(factory = AppViewModelProvider.Factory),
    goFurther: (String) -> Unit
) {
    SplashScreenContent(
        modifier = modifier,
        showError = viewModel.showError.value,
        goFurther = { viewModel.onAppStart(goFurther) }
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    showError: Boolean,
    goFurther: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showError) {
            Text(text = "Something wrong happened. Please try again.")

            Button(
                onClick = {  },
                modifier = Modifier
                    .padding(16.dp, 8.dp),
            ) {
                Text(text = "Try again", fontSize = 16.sp)
            }

        } else {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        goFurther()
    }
}

@Composable
@Preview
fun SplashScreenContentPreview() {
    SplashScreenContent(
        showError = true,
        goFurther = { }
    )
}