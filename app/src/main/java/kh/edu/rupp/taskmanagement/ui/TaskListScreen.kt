package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.FilterChipRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    onToggle: (Task) -> Unit,
    onAdd: (String, String) -> Unit,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                    items(visibleTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            isDone = task.isDone,
                            onToggle = { onToggle(task) },
                            onClick = { onTaskClick(task.id) }
                        )
                    }
                }
            }
            FloatingActionButton(
                onClick = { onAdd(newTitle, newDescription) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(R.dimen.list_padding))
            ) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
