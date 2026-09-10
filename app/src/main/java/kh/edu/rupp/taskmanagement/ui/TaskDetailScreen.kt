package kh.edu.rupp.taskmanagement.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.ConfirmDeleteDialog
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onToggle: () -> Unit,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // whether the question is on screen is this screen's business and nobody else's
    var isDeleteAsked by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val shareText = stringResource(R.string.share_task_text, task.title, dueDateText(task.dueDate))
    val chooserTitle = stringResource(R.string.share_chooser_title)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.task_detail_title)) },
                navigationIcon = {
                    // Up and system back do the same thing: leave this destination
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = onEdit
                    ) {
                        Text(stringResource(R.string.edit))
                    }
                    TextButton(
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = { isDeleteAsked = true }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                    TextButton(
                        // a text action defaults to 40dp high, under the 48dp target
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = {
                            // nothing here names an app: Android asks the user who can take this
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, chooserTitle))
                        }
                    ) {
                        Text(stringResource(R.string.share))
                    }
                }
            )
        }
    ) { innerPadding ->
        TaskDetailContent(
            task = task,
            onToggle = onToggle,
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
        )
        if (isDeleteAsked) {
            ConfirmDeleteDialog(
                onConfirm = {
                    isDeleteAsked = false
                    onDelete()
                },
                onDismiss = { isDeleteAsked = false }
            )
        }
    }
}

// the body on its own, so a wide screen can show it beside the list
@Composable
fun TaskDetailContent(
    task: Task,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val doneLabel = stringResource(R.string.task_done)
    Column(modifier.fillMaxSize()) {
        Text(task.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                priorityLabel(task.priority),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                "·",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                stringResource(R.string.task_due, dueDateText(task.dueDate)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(16.dp))
        // the detail screen has the room to show the description in full
        Text(
            task.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(max = 640.dp)
        )
        Spacer(Modifier.weight(1f))
        // the caps do nothing on a phone and stop a wide pane stretching the row
        Card(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    doneLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                // the same event the list card sends, so the two screens cannot disagree
                Switch(
                    checked = task.isDone,
                    onCheckedChange = { onToggle() },
                    modifier = Modifier.semantics { contentDescription = doneLabel }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    TaskManagerTheme {
        TaskDetailScreen(sampleTasks.first(), onToggle = {}, onBack = {}, onEdit = {}, onDelete = {})
    }
}
