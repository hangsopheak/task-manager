package kh.edu.rupp.taskmanagement.data

import kh.edu.rupp.taskmanagement.data.local.TaskDao
import kh.edu.rupp.taskmanagement.data.local.toEntity
import kh.edu.rupp.taskmanagement.data.local.toTask as taskFromRow
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.network.taskApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kh.edu.rupp.taskmanagement.network.toDto
import kh.edu.rupp.taskmanagement.network.toTask

// one owner for data access, so the screens never learn that Retrofit or Room exists
class TaskRepository(private val dao: TaskDao) {

    fun observeTasks(): Flow<List<Task>> =
        dao.observeTasks().map { rows -> rows.map { it.taskFromRow() } }

    suspend fun getTasks(): Result<List<Task>> = runCatching {
        taskApi.getTasks().map { it.toTask() }
    }

    suspend fun addTask(task: Task): Result<Task> = runCatching {
        taskApi.createTask(task.toDto()).toTask()
    }

    suspend fun updateTask(task: Task): Result<Task> = runCatching {
        taskApi.updateTask(task.id, task.toDto()).toTask()
    }

    suspend fun deleteTask(taskId: String): Result<Unit> = runCatching {
        taskApi.deleteTask(taskId)
    }
}
