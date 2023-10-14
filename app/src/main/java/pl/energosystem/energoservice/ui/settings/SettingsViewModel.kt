package pl.energosystem.energoservice.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.model.service.AccountService

class SettingsViewModel(
    private val accountService: AccountService
) : ViewModel() {

    fun logOut(restartApp: () -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
            restartApp()
        }
    }
}

