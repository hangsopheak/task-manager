package kh.edu.rupp.taskmanagement.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.data.TaskRepository
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository = TaskRepository()) : ViewModel() {

    var state by mutableStateOf<TaskUiState>(TaskUiState.Loading)
        private set

    // the one sentence to show the user, kept as a string id so it can be translated
    var message by mutableStateOf<Int?>(null)
        private set

    fun onMessageShown() {
        message = null
    }

    // an id is handed out once and never used again, so two rows can never share one
    private var nextNumber = 9

    fun load() {
        viewModelScope.launch {
            state = TaskUiState.Loading
            repository.getTasks()
                .onSuccess { tasks ->
                    nextNumber = tasks.size + 1
                    state = if (tasks.isEmpty()) TaskUiState.Empty else TaskUiState.Success(tasks)
                }
                .onFailure { error ->
                    state = TaskUiState.Error(error.message ?: "Could not reach the server")
                }
        }
    }

    fun find(taskId: String?): Task? =
        (state as? TaskUiState.Success)?.tasks?.firstOrNull { it.id == taskId }

    // the titles a duplicate check needs, minus the task being edited
    fun taskTitles(excludeId: String?): List<String> =
        (state as? TaskUiState.Success)
            ?.tasks
            ?.filter { it.id != excludeId }
            ?.map { it.title }
            ?: emptyList()

    fun toggle(task: Task) {
        editInPlace(task.copy(isDone = !task.isDone))
    }

    fun delete(task: Task) {
        val tasks = (state as? TaskUiState.Success)?.tasks ?: return
        state = TaskUiState.Success(tasks.filter { it.id != task.id })
        message = R.string.task_deleted
    }

    // one function for both jobs: an id that is already here is an edit, anything else is new
    fun save(taskId: String?, title: String, description: String, dueDate: LocalDate, priority: Priority) {
        val tasks = (state as? TaskUiState.Success)?.tasks ?: return
        val index = tasks.indexOfFirst { it.id == taskId }
        state = if (index >= 0) {
            TaskUiState.Success(tasks.copyAt(index, tasks[index].copy(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority
            )))
        } else {
            // the newest task goes on top, where the user is already looking
            TaskUiState.Success(listOf(
                Task(
                    id = "t${nextNumber++}",
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = priority,
                    isDone = false
                )
            ) + tasks)
        }
        message = R.string.task_saved
    }

    private fun editInPlace(task: Task) {
        val tasks = (state as? TaskUiState.Success)?.tasks ?: return
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            state = TaskUiState.Success(tasks.copyAt(index, task))
        }
    }
}

private fun List<Task>.copyAt(index: Int, task: Task): List<Task> =
    subList(0, index) + task + subList(index + 1, size)
