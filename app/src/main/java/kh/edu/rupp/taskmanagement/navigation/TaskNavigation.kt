package kh.edu.rupp.taskmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kh.edu.rupp.taskmanagement.data.TaskStore
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.ui.TaskDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskGoneScreen
import kh.edu.rupp.taskmanagement.ui.TaskListScreen

@Composable
fun TaskNavigation() {
    // saved across a rotation, because every key on it is @Serializable
    val backStack = rememberNavBackStack(TaskList)
    // a plain remember, so the tasks start again after a rotation
    val store = remember { TaskStore(sampleTasks) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TaskList> {
                TaskListScreen(
                    tasks = store.tasks,
                    onToggle = { store.toggle(it) },
                    onAdd = { title, description -> store.add(title, description) },
                    onTaskClick = { taskId -> backStack.add(TaskDetail(taskId)) }
                )
            }
            entry<TaskDetail> { key ->
                val task = store.find(key.taskId)
                if (task != null) {
                    TaskDetailScreen(
                        task = task,
                        onToggle = { store.toggle(task) },
                        onBack = { backStack.removeLastOrNull() }
                    )
                } else {
                    // the key is saved across a rotation and the tasks are not, so it can outlive its task
                    TaskGoneScreen(onBack = { backStack.removeLastOrNull() })
                }
            }
        }
    )
}
