package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.model.Task

// a wide screen has room for both, so the tap picks a task instead of opening a new screen
@Composable
fun TaskListDetailScreen(
    tasks: List<Task>,
    onToggle: (Task) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedId by rememberSaveable { mutableStateOf(tasks.firstOrNull()?.id) }
    val selected = tasks.firstOrNull { it.id == selectedId }
    Row(modifier.fillMaxSize()) {
        TaskListScreen(
            tasks = tasks,
            onToggle = onToggle,
            onAdd = onAdd,
            onTaskClick = { selectedId = it },
            modifier = Modifier.width(440.dp)
        )
        VerticalDivider()
        // the list pane brings its own bar, the detail pane has to clear the status bar itself
        Box(
            Modifier
                .weight(1f)
                .statusBarsPadding()
        ) {
            if (selected != null) {
                TaskDetailContent(
                    task = selected,
                    onToggle = { onToggle(selected) },
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
                )
            }
        }
    }
}
