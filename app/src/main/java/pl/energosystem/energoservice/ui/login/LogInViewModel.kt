package pl.energosystem.energoservice.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
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
            try {
                accountService.authenticate(email, password)
                onSuccessfulLogin()
            } catch (e: FirebaseException) {
                uiState.value = uiState.value.copy(errorMessage = "Invalid credentials")
            }
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = "Invalid email")
            return
        }

        viewModelScope.launch {
            try {
                accountService.sendRecoveryEmail(email)
                uiState.value = uiState.value.copy(errorMessage = "Recovery email sent")
            } catch (e: FirebaseException) {
                uiState.value = uiState.value.copy(errorMessage = "Something went wrong")
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = ""
)
