package kh.edu.rupp.taskmanagement.data

import androidx.compose.runtime.toMutableStateList
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

// one list kept above the navigation display, so every screen reads and changes the same tasks
class TaskStore(startTasks: List<Task>) {
    val tasks = startTasks.toMutableStateList()

    fun find(taskId: String?): Task? = tasks.firstOrNull { it.id == taskId }

    fun toggle(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task.copy(isDone = !task.isDone)
    }

    // the same canned row every time: typing a real one comes later in the course
    fun add(title: String, description: String) {
        tasks.add(
            Task(
                id = "t${tasks.size + 1}",
                title = title,
                description = description,
                dueDate = LocalDate.of(2025, 9, 30),
                priority = Priority.MEDIUM,
                isDone = false
            )
        )
    }
}
