package pl.energosystem.energoservice.ui.settings

import androidx.lifecycle.ViewModel
import pl.energosystem.energoservice.data.user.UsersRepository

class SettingsViewModel(
    usersRepository: UsersRepository
) : ViewModel() {

    fun logOut(moveToLogInScreen: () -> Unit): Boolean {
        moveToLogInScreen()
        return false
    }
}

