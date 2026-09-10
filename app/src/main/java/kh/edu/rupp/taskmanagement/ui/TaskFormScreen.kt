package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.DueDateField
import kh.edu.rupp.taskmanagement.ui.form.rememberTaskFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    task: Task?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // one screen for both jobs: a task to edit, or nothing to start from
    val state = rememberTaskFormState(task)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (task == null) R.string.form_title_add else R.string.form_title_edit
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        // a text action defaults to 40dp high, under the 48dp target
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = {},
                        enabled = state.isValid
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // value and onValueChange are the whole contract: show this, tell me that
            OutlinedTextField(
                value = state.title,
                onValueChange = { state.title = it },
                label = { Text(stringResource(R.string.form_title_label)) },
                isError = !state.isTitleValid,
                supportingText = {
                    if (!state.isTitleValid) Text(stringResource(R.string.form_title_error))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = { state.description = it },
                label = { Text(stringResource(R.string.form_description)) },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            PriorityPicker(
                selected = state.priority,
                onSelect = { state.priority = it }
            )
            DueDateField(
                dueDate = state.dueDate,
                onPick = { state.dueDate = it }
            )
        }
    }
}

// three values, one of them on: the row cannot hold two priorities or none
@Composable
private fun PriorityPicker(
    selected: Priority,
    onSelect: (Priority) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            stringResource(R.string.form_priority),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp)
        )
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            Priority.entries.forEachIndexed { index, priority ->
                SegmentedButton(
                    selected = priority == selected,
                    onClick = { onSelect(priority) },
                    shape = SegmentedButtonDefaults.itemShape(index, Priority.entries.size),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    icon = {},
                    label = { Text(priorityLabel(priority)) }
                )
            }
        }
    }
}
