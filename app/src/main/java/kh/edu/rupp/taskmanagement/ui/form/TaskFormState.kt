package kh.edu.rupp.taskmanagement.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

// one holder for the whole form, so the screen reads and writes one thing
class TaskFormState(task: Task?) {
    var title by mutableStateOf(task?.title ?: "")
    var description by mutableStateOf(task?.description ?: "")
    var priority by mutableStateOf(task?.priority ?: Priority.MEDIUM)
    // a fixed date, never today, so the empty form reads the same in any year
    var dueDate by mutableStateOf(task?.dueDate ?: LocalDate.of(2025, 9, 12))
}

// a new task starts empty, an existing one starts filled in
@Composable
fun rememberTaskFormState(task: Task?): TaskFormState = remember(task) { TaskFormState(task) }
