package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import java.time.LocalDate

@Composable
fun TaskListScreen(initialTasks: List<Task>, modifier: Modifier = Modifier) {
    // the screen above the cards keeps the list, so every card can stay stateless
    val tasks = remember { initialTasks.toMutableStateList() }
    val newTitle = stringResource(R.string.new_task_title)
    val newDescription = stringResource(R.string.new_task_description)
    Box(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(dimensionResource(R.dimen.list_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.list_spacing))
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    isDone = task.isDone,
                    onToggle = {
                        val index = tasks.indexOf(task)
                        tasks[index] = task.copy(isDone = !task.isDone)
                    }
                )
            }
        }
        FloatingActionButton(
            // the same canned row every time: typing a real one comes later in the course
            onClick = {
                tasks.add(
                    Task(
                        id = "t${tasks.size + 1}",
                        title = newTitle,
                        description = newDescription,
                        dueDate = LocalDate.of(2025, 9, 30),
                        priority = Priority.MEDIUM,
                        isDone = false
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.list_padding))
        ) {
            Text("+", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
