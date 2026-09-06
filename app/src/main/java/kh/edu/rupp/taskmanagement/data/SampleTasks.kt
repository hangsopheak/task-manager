package kh.edu.rupp.taskmanagement.data

import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

// the dates are fixed values, never today, so this list reads the same in any year
val sampleTasks = listOf(
    Task(
        id = "t1",
        title = "Submit lab report",
        description = "Upload the PDF to the class group before midnight.",
        dueDate = LocalDate.of(2025, 9, 12),
        priority = Priority.HIGH,
        isDone = true
    ),
    Task(
        id = "t2",
        title = "Buy Kotlin book",
        description = "Second hand is fine if the pages are clean.",
        dueDate = LocalDate.of(2025, 9, 15),
        priority = Priority.LOW,
        isDone = false
    ),
    Task(
        id = "t3",
        title = "Team meeting notes",
        description = "Write up what the team agreed and share the file.",
        dueDate = LocalDate.of(2025, 9, 16),
        priority = Priority.MEDIUM,
        isDone = true
    ),
    Task(
        id = "t4",
        title = "Pay tuition fee",
        description = "Pay at the bank counter and keep the receipt.",
        dueDate = LocalDate.of(2025, 9, 19),
        priority = Priority.HIGH,
        isDone = false
    ),
    Task(
        id = "t5",
        title = "Gym at 6 pm",
        description = "Bring the running shoes this time.",
        dueDate = LocalDate.of(2025, 9, 20),
        priority = Priority.LOW,
        isDone = false
    ),
    Task(
        id = "t6",
        title = "Call the landlord",
        description = "Ask about the water bill for last month.",
        dueDate = LocalDate.of(2025, 9, 21),
        priority = Priority.MEDIUM,
        isDone = true
    ),
    Task(
        id = "t7",
        title = "Prepare group presentation",
        description = "Build ten slides and practise the demo twice.",
        dueDate = LocalDate.of(2025, 9, 22),
        priority = Priority.HIGH,
        isDone = false
    ),
    Task(
        id = "t8",
        title = "Register for next semester",
        description = "Choose the electives before the list closes.",
        dueDate = LocalDate.of(2025, 9, 24),
        priority = Priority.HIGH,
        isDone = false
    )
)
