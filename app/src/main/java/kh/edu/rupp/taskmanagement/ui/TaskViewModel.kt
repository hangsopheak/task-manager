package kh.edu.rupp.taskmanagement.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.taskmanagement.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository = TaskRepository()) : ViewModel() {

    var state by mutableStateOf<TaskUiState>(TaskUiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            state = TaskUiState.Loading
            repository.getTasks()
                .onSuccess { tasks ->
                    state = if (tasks.isEmpty()) TaskUiState.Empty else TaskUiState.Success(tasks)
                }
                .onFailure { error ->
                    state = TaskUiState.Error(error.message ?: "Could not reach the server")
                }
        }
    }
}
