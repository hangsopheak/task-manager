package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.FilterChipRow
import java.time.LocalDate

@Composable
fun TaskListScreen(initialTasks: List<Task>, modifier: Modifier = Modifier) {
    // the screen above the cards keeps the list, so every card can stay stateless
    val tasks = remember { initialTasks.toMutableStateList() }
    var selectedFilter by rememberSaveable { mutableStateOf(TaskFilter.ALL) }
    // the count and the visible rows are worked out from the list, never stored beside it
    val doneCount by remember { derivedStateOf { tasks.count { it.isDone } } }
    val filterCounts by remember {
        derivedStateOf {
            mapOf(
                TaskFilter.ALL to tasks.size,
                TaskFilter.ACTIVE to tasks.size - doneCount,
                TaskFilter.DONE to doneCount
            )
        }
    }
    val visibleTasks by remember {
        derivedStateOf {
            when (selectedFilter) {
                TaskFilter.ALL -> tasks.toList()
                TaskFilter.ACTIVE -> tasks.filter { !it.isDone }
                TaskFilter.DONE -> tasks.filter { it.isDone }
            }
        }
    }
    val newTitle = stringResource(R.string.new_task_title)
    val newDescription = stringResource(R.string.new_task_description)
    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Text(
                stringResource(R.string.tasks_done_count, doneCount, tasks.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.list_padding),
                    vertical = dimensionResource(R.dimen.list_spacing)
                )
            )
            FilterChipRow(
                selected = selectedFilter,
                counts = filterCounts,
                onSelect = { selectedFilter = it }
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(dimensionResource(R.dimen.list_padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.list_spacing))
            ) {
                items(visibleTasks) { task ->
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
