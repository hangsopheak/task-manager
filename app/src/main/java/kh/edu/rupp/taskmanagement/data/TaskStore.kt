package kh.edu.rupp.taskmanagement.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

// one list kept above the graph, so every screen reads and changes the same tasks
class TaskStore(startTasks: List<Task>) {
    val tasks = startTasks.toMutableStateList()

    // an id is handed out once and never used again, so two rows can never share one
    private var nextNumber = startTasks.size + 1

    // the one sentence to show the user, kept as a string id so it can be translated
    var message by mutableStateOf<Int?>(null)

    fun find(taskId: String?): Task? = tasks.firstOrNull { it.id == taskId }

    fun toggle(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task.copy(isDone = !task.isDone)
    }

    // one function for both jobs: an id that is already here is an edit, anything else is new
    fun save(
        taskId: String?,
        title: String,
        description: String,
        dueDate: LocalDate,
        priority: Priority
    ) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index >= 0) {
            tasks[index] = tasks[index].copy(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority
            )
        } else {
            // the newest task goes on top, where the user is already looking
            tasks.add(
                0,
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
    }
}

@Composable
fun rememberTaskStore(): TaskStore = remember { TaskStore(sampleTasks) }
