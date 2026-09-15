package kh.edu.rupp.taskmanagement.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.taskmanagement.auth.AuthOutcome
import kh.edu.rupp.taskmanagement.auth.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    var state by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    var signedIn by mutableStateOf(false)
        private set

    fun signIn(email: String, password: String) {
        submit { repository.signIn(email, password) }
    }

    fun createAccount(email: String, password: String) {
        submit { repository.createAccount(email, password) }
    }

    private fun submit(call: suspend () -> AuthOutcome) {
        viewModelScope.launch {
            state = LoginUiState.Submitting
            when (val outcome = call()) {
                is AuthOutcome.Success -> signedIn = true
                is AuthOutcome.Failure -> state = LoginUiState.Error(outcome.error)
            }
        }
    }
}
