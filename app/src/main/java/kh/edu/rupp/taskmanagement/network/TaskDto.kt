package kh.edu.rupp.taskmanagement.network

import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

// the shape on the wire: ids are strings and dates are ISO text, matching template.json
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: String,
    val isDone: Boolean
)

// the string becomes a LocalDate here, so the rest of the app never parses again
fun TaskDto.toTask(): Task = Task(
    id = id,
    title = title,
    description = description,
    dueDate = LocalDate.parse(dueDate),
    priority = Priority.valueOf(priority),
    isDone = isDone
)

fun Task.toDto(): TaskDto = TaskDto(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate.toString(),
    priority = priority.name,
    isDone = isDone
)
