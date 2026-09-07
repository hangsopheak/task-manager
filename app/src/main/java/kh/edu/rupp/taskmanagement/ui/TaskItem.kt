package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun dueDateText(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("EEE d MMM"))

@Composable
fun priorityLabel(priority: Priority): String = when (priority) {
    Priority.LOW -> stringResource(R.string.priority_low)
    Priority.MEDIUM -> stringResource(R.string.priority_medium)
    Priority.HIGH -> stringResource(R.string.priority_high)
}

@Composable
fun TaskItem(task: Task, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // the box stays empty on every card: nothing reads isDone yet
            Box(
                Modifier
                    .size(18.dp)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(3.dp))
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // one line only, so a long description cannot change the height of the card
                Text(
                    task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${dueDateText(task.dueDate)} · ${priorityLabel(task.priority)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    TaskManagerTheme {
        TaskItem(sampleTasks.first())
    }
}
