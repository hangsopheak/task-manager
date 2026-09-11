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
class TaskFormState(task: Task?, private val otherTitles: List<String>) {
    var title by mutableStateOf(task?.title ?: "")
    var description by mutableStateOf(task?.description ?: "")
    var priority by mutableStateOf(task?.priority ?: Priority.MEDIUM)
    // a fixed date, never today, so the empty form reads the same in any year
    var dueDate by mutableStateOf(task?.dueDate ?: LocalDate.of(2025, 9, 12))

    // nothing keeps a record of whether the form is valid: it is worked out from the values
    val isTitleValid: Boolean
        get() = title.isNotBlank()

    // this rule needs the other tasks: two rows with one title cannot be told apart
    val isTitleFree: Boolean
        get() = otherTitles.none { it.equals(title.trim(), ignoreCase = true) }

    // this rule needs two fields: urgent work has to say what it is
    val isDescriptionValid: Boolean
        get() = priority != Priority.HIGH || description.isNotBlank()

    // every rule for this form is above, so there is one place to read and one to change
    val isValid: Boolean
        get() = isTitleValid && isTitleFree && isDescriptionValid
}

// a new task starts empty, an existing one starts filled in
@Composable
fun rememberTaskFormState(task: Task?, otherTitles: List<String>): TaskFormState =
    remember(task, otherTitles) { TaskFormState(task, otherTitles) }
