package pl.energosystem.energoservice.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LogInUiState())
    val uiState: StateFlow<LogInUiState> = _uiState

    private val logInData = MutableStateFlow(Pair(true, false))
    private val emailFieldText = MutableStateFlow("")
    private val passwordTextField = MutableStateFlow("")

    fun logIn(
        email: String,
        password: String,
        onLogInComplete: () -> Unit,
    ) {
        if (email.isNotBlank() && password.isNotBlank())
            onLogInComplete()
        else
            _uiState.value = _uiState.value.copy(isEmailValid = false)
    }

    fun onEmailChange(newEmail: String) {
        if(newEmail != uiState.value.emailFieldText) {
            _uiState.value = _uiState.value.copy(emailFieldText = newEmail)
        }
    }

    fun onPasswordChange(newPassword: String) {
        if(newPassword != uiState.value.passwordTextField) {
            _uiState.value = _uiState.value.copy(passwordTextField = newPassword)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class LogInUiState(
    var emailFieldText: String = "",
    var passwordTextField: String = "",
    val isEmailValid: Boolean = true,
    val isPasswordEmpty: Boolean = false
)
