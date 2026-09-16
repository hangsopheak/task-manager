package kh.edu.rupp.taskmanagement.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.data.TaskRepository
import kh.edu.rupp.taskmanagement.data.local.DatabaseProvider
import android.location.Location
import kh.edu.rupp.taskmanagement.data.prefs.UserPrefs
import kh.edu.rupp.taskmanagement.location.LocationProvider
import kh.edu.rupp.taskmanagement.notifications.TaskReminders
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = TaskRepository(DatabaseProvider.get(app).taskDao())
    private val prefs = UserPrefs(app)
    private val location = LocationProvider(app)

    var state by mutableStateOf<TaskUiState>(TaskUiState.Loading)
        private set

    // two settings, one store: one reorders the list, the other repaints the app
    var sortOrder by mutableStateOf(SortOrder.DUE_DATE)
        private set

    var themeChoice by mutableStateOf(ThemeChoice.SYSTEM)
        private set

    var remindersEnabled by mutableStateOf(true)
        private set

    // how far the phone is from the watched task, while its screen is open
    var distanceMeters by mutableStateOf<Int?>(null)
        private set

    private var watchJob: Job? = null
    private var arrivalPosted = false

    private var currentTasks: List<Task> = emptyList()

    init {
        // the list on screen is whatever Room has, the moment Room has it
        viewModelScope.launch {
            repository.observeTasks().collect { tasks ->
                currentTasks = tasks
                publish()
            }
        }
        viewModelScope.launch {
            prefs.sortOrder.collect { order ->
                sortOrder = order
                publish()
            }
        }
        viewModelScope.launch {
            prefs.themeChoice.collect { choice ->
                themeChoice = choice
            }
        }
        viewModelScope.launch {
            prefs.remindersEnabled.collect { enabled ->
                remindersEnabled = enabled
            }
        }
    }

    private fun publish() {
        state = if (currentTasks.isEmpty()) {
            TaskUiState.Empty
        } else {
            TaskUiState.Success(sorted(currentTasks))
        }
    }

    private fun sorted(tasks: List<Task>): List<Task> = when (sortOrder) {
        SortOrder.DUE_DATE -> tasks.sortedBy { it.dueDate }
        SortOrder.PRIORITY -> tasks.sortedByDescending { it.priority.ordinal }
        SortOrder.TITLE -> tasks.sortedBy { it.title.lowercase() }
    }

    fun chooseSortOrder(order: SortOrder) {
        viewModelScope.launch { prefs.setSortOrder(order) }
    }

    fun chooseTheme(choice: ThemeChoice) {
        viewModelScope.launch { prefs.setThemeChoice(choice) }
    }

    fun chooseReminders(enabled: Boolean) {
        viewModelScope.launch { prefs.setRemindersEnabled(enabled) }
    }

    // the one sentence to show the user, kept as a string id so it can be translated
    var message by mutableStateOf<Int?>(null)
        private set

    fun onMessageShown() {
        message = null
    }

    // an id is handed out once and never used again, so two rows can never share one
    private var nextNumber = 9

    private companion object {
        const val ARRIVAL_METERS = 100
    }

    // a relaunch is no longer the refresh: this is
    fun refresh() {
        viewModelScope.launch {
            repository.refresh()
                .onSuccess { tasks ->
                    nextNumber = tasks.size + 1
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

    fun watchTask(task: Task) {
        watchJob?.cancel()
        distanceMeters = null
        arrivalPosted = false
        val latitude = task.latitude ?: return
        val longitude = task.longitude ?: return
        watchJob = viewModelScope.launch {
            location.updates().collect { fix ->
                val meters = distanceToPlace(fix, latitude, longitude)
                distanceMeters = meters
                // post once, not on every update: that is the bug everyone writes first
                if (meters <= ARRIVAL_METERS && !arrivalPosted) {
                    arrivalPosted = true
                    TaskReminders.postArrival(getApplication(), task)
                }
            }
        }
    }

    fun stopWatching() {
        watchJob?.cancel()
        watchJob = null
        distanceMeters = null
    }

    private fun distanceToPlace(fix: Location, latitude: Double, longitude: Double): Int {
        val results = FloatArray(1)
        Location.distanceBetween(fix.latitude, fix.longitude, latitude, longitude, results)
        return results[0].toInt()
    }

    fun setPlace(taskId: String, label: String?, latitude: Double?, longitude: Double?) {
        viewModelScope.launch { repository.setPlace(taskId, label, latitude, longitude) }
    }

    fun toggle(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isDone = !task.isDone))
            refresh()
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task.id)
            message = R.string.task_deleted
            refresh()
        }
    }

    // one function for both jobs: an id that is already here is an edit, anything else is new
    fun save(taskId: String?, title: String, description: String, dueDate: LocalDate, priority: Priority) {
        viewModelScope.launch {
            if (taskId != null) {
                val existing = find(taskId)
                if (existing != null) {
                    repository.updateTask(
                        existing.copy(
                            title = title,
                            description = description,
                            dueDate = dueDate,
                            priority = priority
                        )
                    )
                }
            } else {
                // the app owns the id, so a row keeps its identity wherever it travels
                repository.addTask(
                    Task(
                        id = "t${nextNumber++}",
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        priority = priority,
                        isDone = false
                    )
                )
            }
            message = R.string.task_saved
            refresh()
        }
    }
}

private fun List<Task>.copyAt(index: Int, task: Task): List<Task> =
    subList(0, index) + task + subList(index + 1, size)
