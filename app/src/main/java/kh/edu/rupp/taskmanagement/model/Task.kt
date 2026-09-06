package kh.edu.rupp.taskmanagement.model

import java.time.LocalDate

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: LocalDate,
    val priority: Priority,
    val isDone: Boolean
)
