package kh.edu.rupp.taskmanagement.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.ui.TaskFilter

@Composable
private fun filterLabel(filter: TaskFilter): String = when (filter) {
    TaskFilter.ALL -> stringResource(R.string.filter_all)
    TaskFilter.ACTIVE -> stringResource(R.string.filter_active)
    TaskFilter.DONE -> stringResource(R.string.filter_done)
}

// the row is told which chip is on and reports the tap, the same shape as a task card
@Composable
fun FilterChipRow(
    selected: TaskFilter,
    counts: Map<TaskFilter, Int>,
    onSelect: (TaskFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.list_padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.list_spacing))
    ) {
        TaskFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter == selected,
                onClick = { onSelect(filter) },
                label = { Text(filterLabel(filter)) },
                trailingIcon = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Text(counts.getValue(filter).toString())
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}
