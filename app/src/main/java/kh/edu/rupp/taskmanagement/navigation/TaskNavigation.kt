package kh.edu.rupp.taskmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kh.edu.rupp.taskmanagement.data.TaskStore
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.ui.AppScaffold
import kh.edu.rupp.taskmanagement.ui.CalendarScreen
import kh.edu.rupp.taskmanagement.ui.SettingsScreen
import kh.edu.rupp.taskmanagement.ui.StatsScreen
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
            // the three tabs draw their screen inside AppScaffold, so only they show the bar
            entry<TaskList> {
                AppScaffold(TopLevelDestination.TASKS, onSelectTab = { selectTab(backStack, it) }) {
                    TaskListScreen(
                        tasks = store.tasks,
                        onToggle = { store.toggle(it) },
                        onAdd = { title, description -> store.add(title, description) },
                        onTaskClick = { taskId -> backStack.add(TaskDetail(taskId)) },
                        onOpenSettings = { backStack.add(Settings) }
                    )
                }
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
            entry<Calendar> {
                AppScaffold(TopLevelDestination.CALENDAR, onSelectTab = { selectTab(backStack, it) }) {
                    CalendarScreen()
                }
            }
            entry<Stats> {
                AppScaffold(TopLevelDestination.STATS, onSelectTab = { selectTab(backStack, it) }) {
                    StatsScreen()
                }
            }
            // a child of Tasks, not a tab: it is not drawn in AppScaffold, so it covers the bar
            entry<Settings> {
                SettingsScreen(onBack = { backStack.removeLastOrNull() })
            }
        }
    )
}

// Tasks stays at the bottom with the chosen tab on top, so Back from any tab lands on Tasks
private fun selectTab(backStack: NavBackStack<NavKey>, tab: TopLevelDestination) {
    backStack.retainAll { it == TaskList }
    if (tab.key != TaskList) {
        backStack.add(tab.key)
    }
}
