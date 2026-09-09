package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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

// the card is told whether it is done and reports the tap, so it keeps nothing of its own
@Composable
fun TaskItem(
    task: Task,
    isDone: Boolean,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // the card opens the task, the box inside it still only toggles done
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.card_padding_horizontal),
                    vertical = dimensionResource(R.dimen.card_padding_vertical)
                ),
            horizontalArrangement =
                Arrangement.spacedBy(dimensionResource(R.dimen.card_row_spacing)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // the box is its own control, so it says which task it belongs to
            Checkbox(
                checked = isDone,
                onCheckedChange = { onToggle() },
                modifier = Modifier.semantics { contentDescription = task.title }
            )
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(dimensionResource(R.dimen.card_text_spacing))
            ) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color =
                        if (isDone) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (isDone) TextDecoration.LineThrough else null,
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
                val dueLabel = stringResource(R.string.task_due_label)
                Text(
                    "$dueLabel ${dueDateText(task.dueDate)} · ${priorityLabel(task.priority)}",
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
        TaskItem(sampleTasks.first(), isDone = true, onToggle = {}, onClick = {})
    }
}
