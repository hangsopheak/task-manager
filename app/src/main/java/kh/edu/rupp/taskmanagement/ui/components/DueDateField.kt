package kh.edu.rupp.taskmanagement.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val dayMonthYear = DateTimeFormatter.ofPattern("EEE d MMM yyyy")

// the field cannot be typed into: a date the user writes by hand is a date the app has to guess
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDateField(
    dueDate: LocalDate,
    onPick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPickerOpen by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = dueDate.format(dayMonthYear),
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(R.string.form_due_date)) },
        trailingIcon = {
            IconButton(onClick = { isPickerOpen = true }) {
                Icon(
                    painterResource(R.drawable.ic_calendar),
                    contentDescription = stringResource(R.string.form_pick_date)
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
    if (isPickerOpen) {
        // the picker counts milliseconds from 1970 in UTC, the app keeps a plain date
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis =
                dueDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { isPickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val picked = pickerState.selectedDateMillis
                        if (picked != null) {
                            onPick(Instant.ofEpochMilli(picked).atZone(ZoneOffset.UTC).toLocalDate())
                        }
                        isPickerOpen = false
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { isPickerOpen = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}
