package pl.energosystem.energoservice.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.energosystem.energoservice.data.user.User
import pl.energosystem.energoservice.data.user.UsersRepository

class LogInViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    init {
        this.viewModelScope.launch {
            usersRepository.insertUser(User(0, "test", "test"))
        }
    }

    private val _uiState = MutableStateFlow(LogInUiState())
    val uiState: StateFlow<LogInUiState> = _uiState

    fun logIn(
        onLogInComplete: () -> Unit,
    ) {
        this.viewModelScope.launch {
            val userExists = usersRepository
                .getUser(uiState.value.emailFieldText, uiState.value.passwordTextField) != null

            if (userExists){
                clearTextFields()
                onLogInComplete()
            } else {
                _uiState.value = _uiState.value.copy(isEmailValid = false, isPasswordEmpty = true)
            }
        }
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

    private fun clearTextFields() {
        _uiState.value = _uiState.value.copy(emailFieldText = "", passwordTextField = "")
    }
}

data class LogInUiState(
    var emailFieldText: String = "",
    var passwordTextField: String = "",
    val isEmailValid: Boolean = true,
    val isPasswordEmpty: Boolean = false
)
