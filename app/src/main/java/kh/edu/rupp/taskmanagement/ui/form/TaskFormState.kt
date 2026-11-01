package kh.edu.rupp.taskmanagement.ui.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kh.edu.rupp.taskmanagement.model.Task

// one holder for the whole form, so the screen reads and writes one thing
class TaskFormState(task: Task?) {
    var title by mutableStateOf(task?.title ?: "")
}
