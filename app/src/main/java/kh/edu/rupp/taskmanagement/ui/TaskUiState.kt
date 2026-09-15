package kh.edu.rupp.taskmanagement.ui

import kh.edu.rupp.taskmanagement.model.Task

// the four things that actually happen when a screen loads
sealed interface TaskUiState {
    data object Loading : TaskUiState
    data class Success(val tasks: List<Task>) : TaskUiState
    data object Empty : TaskUiState
    data class Error(val message: String) : TaskUiState
}
