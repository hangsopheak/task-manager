package kh.edu.rupp.taskmanagement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

// the row as Room stores it, mirroring Task with types the database can hold
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val dueDate: LocalDate,
    val priority: String,
    val isDone: Boolean
)

fun TaskEntity.toTask(): Task = Task(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    priority = Priority.valueOf(priority),
    isDone = isDone
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    priority = priority.name,
    isDone = isDone
)
