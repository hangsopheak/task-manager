package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Task

@Composable
fun TaskListScreen(tasks: List<Task>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.list_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.list_spacing))
    ) {
        items(tasks) { task ->
            TaskItem(task)
        }
    }
}
