package pl.energosystem.energoservice.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import pl.energosystem.energoservice.EnergoServiceApplication
import pl.energosystem.energoservice.ui.login.LogInViewModel
import pl.energosystem.energoservice.ui.protocol.ProtocolViewModel
import pl.energosystem.energoservice.ui.protocollist.ProtocolListViewModel
import pl.energosystem.energoservice.ui.settings.SettingsViewModel
import pl.energosystem.energoservice.ui.tasklist.TaskListViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for HomeViewModel
        initializer {
            LogInViewModel(
                energoServiceApplication().container.accountService
            )
        }

        // Initializer for ProtocolListViewModel
        initializer {
            ProtocolListViewModel(
                energoServiceApplication().container.protocolsRepository
            )
        }

        // Initializer for ProtocolListViewModel
        initializer {
            TaskListViewModel(
                energoServiceApplication().container.tasksRepository
            )
        }

        // Initializer for ProtocolListViewModel
        initializer {
            SettingsViewModel(
                energoServiceApplication().container.accountService
            )
        }

        // Initializer for ProtocolListViewModel
        initializer {
            ProtocolViewModel(
                energoServiceApplication().container.protocolsRepository,
                energoServiceApplication().container.tasksRepository
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [EnergoServiceApplication].
 */
fun CreationExtras.energoServiceApplication(): EnergoServiceApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EnergoServiceApplication)
