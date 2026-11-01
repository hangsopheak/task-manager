package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.ui.components.MonthGrid
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    tasks: List<Task>,
    onToggle: (Task) -> Unit,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // open on the earliest due date, never on today, so the fixed sample data is always in view
    val firstDue = tasks.minOfOrNull { it.dueDate } ?: LocalDate.of(2025, 9, 1)
    var shownMonth by rememberSaveable { mutableStateOf(YearMonth.from(firstDue)) }
    var selectedDay by rememberSaveable { mutableStateOf(firstDue) }
    // worked out from the one list on every recomposition, never stored beside it
    val markedDays = tasks.map { it.dueDate }.toSet()
    val dayTasks = tasks.filter { it.dueDate == selectedDay }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.calendar_title)) })
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MonthGrid(
                month = shownMonth,
                selected = selectedDay,
                markedDays = markedDays,
                onPrevious = { shownMonth = shownMonth.minusMonths(1) },
                onNext = { shownMonth = shownMonth.plusMonths(1) },
                onSelect = { selectedDay = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                dueDateText(selectedDay).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
            if (dayTasks.isEmpty()) {
                Text(
                    stringResource(R.string.calendar_nothing_due),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dayTasks) { task ->
                    TaskItem(
                        task = task,
                        isDone = task.isDone,
                        onToggle = { onToggle(task) },
                        onClick = { onTaskClick(task.id) }
                    )
                }
            }
        }
    }
}
