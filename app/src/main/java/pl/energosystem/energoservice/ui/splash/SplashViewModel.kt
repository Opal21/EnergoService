package pl.energosystem.energoservice.ui.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pl.energosystem.energoservice.model.service.AccountService
import pl.energosystem.energoservice.ui.EnergoServiceRoute.LOG_IN
import pl.energosystem.energoservice.ui.EnergoServiceRoute.TASK_LIST

class SplashViewModel(
    private val accountService: AccountService
) : ViewModel() {
    val showError = mutableStateOf(false)

    fun onAppStart(goFurther: (String) -> Unit) {
        showError.value = false
        if (accountService.hasUser) goFurther(TASK_LIST)
        else goFurther(LOG_IN)
    }
}
