package kh.edu.rupp.taskmanagement.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.data.TaskStore
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.ui.AppScaffold
import kh.edu.rupp.taskmanagement.ui.CalendarScreen
import kh.edu.rupp.taskmanagement.ui.LoginScreen
import kh.edu.rupp.taskmanagement.ui.NoTaskOpen
import kh.edu.rupp.taskmanagement.ui.SettingsScreen
import kh.edu.rupp.taskmanagement.ui.StatsScreen
import kh.edu.rupp.taskmanagement.ui.TaskDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskFormScreen
import kh.edu.rupp.taskmanagement.ui.TaskGoneScreen
import kh.edu.rupp.taskmanagement.ui.TaskListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TaskNavigation() {
    // saved across a rotation, because every key on it is @Serializable
    val backStack = rememberNavBackStack(Login)
    // a plain remember, so the tasks start again after a rotation
    val store = remember { TaskStore(sampleTasks) }
    // on a wide window it shows a list entry and a detail entry side by side as one scene
    val listDetail = rememberListDetailSceneStrategy<NavKey>(
        // Back closes the open task, the same as Up, instead of leaving both panes at once
        backNavigationBehavior = BackNavigationBehavior.PopUntilContentChange
    )
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategies = listOf(listDetail),
        // switching tabs is not a step forward, so a tab only fades
        transitionSpec = { fadeIn(tween(FADE_MILLIS)) togetherWith fadeOut(tween(FADE_MILLIS)) },
        popTransitionSpec = { fadeIn(tween(FADE_MILLIS)) togetherWith fadeOut(tween(FADE_MILLIS)) },
        entryProvider = entryProvider {
            // nothing is checked yet, so both buttons do the one thing they can do
            entry<Login> {
                LoginScreen(
                    onSignIn = { signIn(backStack) },
                    onCreateAccount = { signIn(backStack) }
                )
            }
            // the three tabs draw their screen inside AppScaffold, so only they show the bar
            entry<TaskList>(metadata = LIST_PANE) {
                AppScaffold(TopLevelDestination.TASKS, onSelectTab = { selectTab(backStack, it) }) {
                    TaskListScreen(
                        tasks = store.tasks,
                        onToggle = { store.toggle(it) },
                        onAdd = { backStack.add(AddTask) },
                        onTaskClick = { taskId -> openTask(backStack, taskId) },
                        onOpenSettings = { backStack.add(Settings) },
                        // only the screen on top shows the message, so two panes never show it twice
                        message = if (backStack.lastOrNull() == TaskList) store.message else null,
                        onMessageShown = { store.message = null }
                    )
                }
            }
            entry<TaskDetail>(metadata = CHILD_SLIDE + ListDetailSceneStrategy.detailPane()) { key ->
                val task = store.find(key.taskId)
                if (task != null) {
                    TaskDetailScreen(
                        task = task,
                        onToggle = { store.toggle(task) },
                        onBack = { backStack.removeLastOrNull() },
                        onEdit = { backStack.add(EditTask(task.id)) },
                        onDelete = {
                            store.delete(task)
                            store.message = R.string.task_deleted
                            backStack.removeLastOrNull()
                        },
                        message = if (backStack.lastOrNull() == key) store.message else null,
                        onMessageShown = { store.message = null }
                    )
                } else if (key in backStack) {
                    // the key is saved across a rotation and the tasks are not, so it can outlive its task
                    TaskGoneScreen(onBack = { backStack.removeLastOrNull() })
                }
                // after a delete the key has left the stack as well, so the screen slides away empty
            }
            // the day's tasks are a list too, so a task opened here gets the same right pane
            entry<Calendar>(metadata = LIST_PANE) {
                AppScaffold(TopLevelDestination.CALENDAR, onSelectTab = { selectTab(backStack, it) }) {
                    CalendarScreen(
                        tasks = store.tasks,
                        onToggle = { store.toggle(it) },
                        onTaskClick = { taskId -> openTask(backStack, taskId) },
                        // a task opened from here comes back here after a delete
                        message = if (backStack.lastOrNull() == Calendar) store.message else null,
                        onMessageShown = { store.message = null }
                    )
                }
            }
            entry<Stats> {
                AppScaffold(TopLevelDestination.STATS, onSelectTab = { selectTab(backStack, it) }) {
                    StatsScreen(store.tasks)
                }
            }
            // a child of Tasks, not a tab: it is not drawn in AppScaffold, so it covers the bar
            entry<Settings>(metadata = CHILD_SLIDE) {
                SettingsScreen(onBack = { backStack.removeLastOrNull() })
            }
            // the form is a child too, with no pane of its own, so it covers the whole window
            entry<AddTask>(metadata = CHILD_SLIDE) {
                TaskFormScreen(
                    task = null,
                    otherTitles = store.tasks.map { it.title },
                    onBack = { backStack.removeLastOrNull() },
                    onSave = { title, description, dueDate, priority ->
                        store.save(null, title, description, dueDate, priority)
                        store.message = R.string.task_saved
                        // the same call as Back, so Back from the list never opens the form again
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<EditTask>(metadata = CHILD_SLIDE) { key ->
                val task = store.find(key.taskId)
                if (task != null) {
                    // the task being edited keeps its own title, so leave it out of the list
                    TaskFormScreen(
                        task = task,
                        otherTitles = store.tasks.filter { it.id != task.id }.map { it.title },
                        onBack = { backStack.removeLastOrNull() },
                        onSave = { title, description, dueDate, priority ->
                            store.save(task.id, title, description, dueDate, priority)
                            store.message = R.string.task_saved
                            backStack.removeLastOrNull()
                        }
                    )
                } else {
                    TaskGoneScreen(onBack = { backStack.removeLastOrNull() })
                }
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

// one open task at a time, so a wide window never hides a pile of them behind the one on screen
private fun openTask(backStack: NavBackStack<NavKey>, taskId: String) {
    backStack.removeAll { it is TaskDetail }
    backStack.add(TaskDetail(taskId))
}

// the login screen leaves the stack for good, so Back from Tasks leaves the app
private fun signIn(backStack: NavBackStack<NavKey>) {
    // two calls in one click, so the display never sees an empty stack
    backStack.clear()
    backStack.add(TaskList)
}

private const val FADE_MILLIS = 400
private const val SLIDE_MILLIS = 500

// the screen on top decides the motion both ways: in from the side, and out the way it came
private val CHILD_SLIDE =
    NavDisplay.transitionSpec {
        slideIntoContainer(SlideDirection.Start, tween(SLIDE_MILLIS)) togetherWith
            slideOutOfContainer(SlideDirection.Start, tween(SLIDE_MILLIS))
    } + NavDisplay.popTransitionSpec {
        slideIntoContainer(SlideDirection.End, tween(SLIDE_MILLIS)) togetherWith
            slideOutOfContainer(SlideDirection.End, tween(SLIDE_MILLIS))
    } + NavDisplay.predictivePopTransitionSpec {
        slideIntoContainer(SlideDirection.End, tween(SLIDE_MILLIS)) togetherWith
            slideOutOfContainer(SlideDirection.End, tween(SLIDE_MILLIS))
    }

// room for the rail and a phone-width list, because the rail is drawn inside the tab screen
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private val LIST_PANE =
    ListDetailSceneStrategy.listPane(detailPlaceholder = { NoTaskOpen() }) +
        ListDetailSceneStrategy.preferredPaneSize(width = 440.dp)
