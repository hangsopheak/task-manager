package kh.edu.rupp.taskmanagement.ui

import kh.edu.rupp.taskmanagement.auth.AuthError

// the button is idle, busy, or holding a sentence the user can read
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Submitting : LoginUiState
    data class Error(val error: AuthError) : LoginUiState
}
