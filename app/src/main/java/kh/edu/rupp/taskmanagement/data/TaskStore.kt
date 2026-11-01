package kh.edu.rupp.taskmanagement.data

import androidx.compose.runtime.toMutableStateList
import kh.edu.rupp.taskmanagement.model.Task

// one list kept above the navigation display, so every screen reads and changes the same tasks
class TaskStore(startTasks: List<Task>) {
    val tasks = startTasks.toMutableStateList()

    fun find(taskId: String?): Task? = tasks.firstOrNull { it.id == taskId }

    fun toggle(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task.copy(isDone = !task.isDone)
    }
}
