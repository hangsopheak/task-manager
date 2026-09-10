package kh.edu.rupp.taskmanagement.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kh.edu.rupp.taskmanagement.model.Task

// one holder for the whole form, so the screen reads and writes one thing
class TaskFormState(task: Task?) {
    var title by mutableStateOf(task?.title ?: "")
}

// a new task starts empty, an existing one starts filled in
@Composable
fun rememberTaskFormState(task: Task?): TaskFormState = remember(task) { TaskFormState(task) }
