package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.PriorityBar
import kh.edu.rupp.taskmanagement.ui.components.StatTile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(tasks: List<Task>, modifier: Modifier = Modifier) {
    // every number here is worked out from the one task list, so nothing can fall behind
    val doneCount by remember { derivedStateOf { tasks.count { it.isDone } } }
    val priorityCounts by remember {
        derivedStateOf { tasks.groupingBy { it.priority }.eachCount() }
    }
    val nextTask by remember { derivedStateOf { tasks.minByOrNull { it.dueDate } } }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.stats_title)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile(tasks.size, stringResource(R.string.stats_total), Modifier.weight(1f))
                StatTile(doneCount, stringResource(R.string.stats_done), Modifier.weight(1f))
                StatTile(
                    tasks.size - doneCount,
                    stringResource(R.string.stats_active),
                    Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            SectionLabel(stringResource(R.string.stats_by_priority))
            PriorityBar(
                label = priorityLabel(Priority.HIGH),
                fraction = share(priorityCounts[Priority.HIGH], tasks.size),
                color = MaterialTheme.colorScheme.primary
            )
            PriorityBar(
                label = priorityLabel(Priority.MEDIUM),
                fraction = share(priorityCounts[Priority.MEDIUM], tasks.size),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
            )
            PriorityBar(
                label = priorityLabel(Priority.LOW),
                fraction = share(priorityCounts[Priority.LOW], tasks.size),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            )
            Spacer(Modifier.height(8.dp))
            SectionLabel(stringResource(R.string.stats_next_due))
            val next = nextTask
            if (next != null) {
                NextDueCard(next)
            }
        }
    }
}

private fun share(count: Int?, total: Int): Float =
    if (total == 0) 0f else (count ?: 0).toFloat() / total

@Composable
private fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun NextDueCard(task: Task) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    dueDateText(task.dueDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
