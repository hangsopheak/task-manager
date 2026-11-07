package kh.edu.rupp.taskmanagement.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Priority
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.ConfirmDiscardDialog
import kh.edu.rupp.taskmanagement.ui.components.DueDateField
import kh.edu.rupp.taskmanagement.ui.form.TaskFormState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    task: Task?,
    otherTitles: List<String>,
    onBack: () -> Unit,
    onSave: (String, String, LocalDate, Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    // keyed on the task, so opening another task starts the form again from its values
    val form = remember(task) { TaskFormState(task, otherTitles) }
    var isDiscardAsked by rememberSaveable { mutableStateOf(false) }
    // only while there are changes: Back on an untouched form just closes it
    BackHandler(enabled = form.isDirty) { isDiscardAsked = true }
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
                    IconButton(onClick = { if (form.isDirty) isDiscardAsked = true else onBack() }) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            onSave(form.title.trim(), form.description.trim(), form.dueDate, form.priority)
                        },
                        enabled = form.isValid
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
                value = form.title,
                onValueChange = {
                    form.title = it
                    form.isTitleTouched = true
                },
                label = { Text(stringResource(R.string.form_title_label)) },
                isError = form.showTitleError || !form.isTitleFree,
                supportingText = {
                    if (form.showTitleError) {
                        Text(stringResource(R.string.form_title_error))
                    } else if (!form.isTitleFree) {
                        Text(stringResource(R.string.form_title_taken))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.description,
                onValueChange = { form.description = it },
                label = { Text(stringResource(R.string.form_description)) },
                isError = !form.isDescriptionValid,
                supportingText = {
                    if (!form.isDescriptionValid) {
                        Text(stringResource(R.string.form_description_error))
                    }
                },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            PriorityPicker(
                selected = form.priority,
                onSelect = { form.priority = it }
            )
            DueDateField(
                dueDate = form.dueDate,
                onPick = { form.dueDate = it }
            )
        }
        if (isDiscardAsked) {
            ConfirmDiscardDialog(
                onConfirm = {
                    isDiscardAsked = false
                    onBack()
                },
                onDismiss = { isDiscardAsked = false }
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
