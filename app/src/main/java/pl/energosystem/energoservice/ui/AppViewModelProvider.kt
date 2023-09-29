package pl.energosystem.energoservice.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import pl.energosystem.energoservice.EnergoServiceApplication
import pl.energosystem.energoservice.ui.login.LogInViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            LogInViewModel()
        }

//        // Initializer for ProtocolListViewModel
//        initializer {
//            ProtocolListViewModel(
//                energoServiceApplication().container.protocolsRepository
//            )
//        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PixelApplication].
 */
fun CreationExtras.energoServiceApplication(): EnergoServiceApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EnergoServiceApplication)
