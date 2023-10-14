package pl.energosystem.energoservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pl.energosystem.energoservice.ui.EnergoServiceApp
import pl.energosystem.energoservice.ui.theme.EnergoServiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnergoServiceTheme {
                EnergoServiceApp()
            }
        }
    }
}
