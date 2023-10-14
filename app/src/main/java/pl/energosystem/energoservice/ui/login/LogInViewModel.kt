package pl.energosystem.energoservice.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.common.isValidEmail
import pl.energosystem.energoservice.model.service.AccountService

class LogInViewModel(
    private val accountService: AccountService
) : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(onSuccessfulLogin: () -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = "Invalid email")
            return
        }

        if (password.isBlank()) {
            uiState.value = uiState.value.copy(errorMessage = "Empty password")
            return
        }

        viewModelScope.launch {
            accountService.authenticate(email, password)
            onSuccessfulLogin()
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = "Invalid email")
            return
        }

        viewModelScope.launch {
            accountService.sendRecoveryEmail(email)
            uiState.value = uiState.value.copy(errorMessage = "Recovery email sent")
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = ""
)
