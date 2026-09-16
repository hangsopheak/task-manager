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

    // the network is the origin of the data, Room is the copy the screen reads
    suspend fun refresh(): Result<List<Task>> = runCatching {
        val tasks = taskApi.getTasks().map { it.toTask() }
        dao.upsertAll(tasks.map { it.toEntity() })
        tasks
    }

    suspend fun addTask(task: Task): Result<Task> = runCatching {
        val saved = taskApi.createTask(task.toDto()).toTask()
        dao.insert(saved.toEntity())
        saved
    }

    suspend fun updateTask(task: Task): Result<Task> = runCatching {
        val saved = taskApi.updateTask(task.id, task.toDto()).toTask()
        dao.insert(saved.toEntity())
        saved
    }

    suspend fun setPlace(taskId: String, label: String?, latitude: Double?, longitude: Double?) {
        dao.setPlace(taskId, label, latitude, longitude)
    }

    suspend fun deleteTask(taskId: String): Result<Unit> = runCatching {
        taskApi.deleteTask(taskId)
        dao.deleteById(taskId)
    }
}
