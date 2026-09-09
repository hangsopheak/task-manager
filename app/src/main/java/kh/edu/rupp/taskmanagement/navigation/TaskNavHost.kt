package kh.edu.rupp.taskmanagement.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import kh.edu.rupp.taskmanagement.data.rememberTaskStore
import kh.edu.rupp.taskmanagement.ui.AppShell
import kh.edu.rupp.taskmanagement.ui.SettingsScreen
import kh.edu.rupp.taskmanagement.ui.StatsScreen
import kh.edu.rupp.taskmanagement.ui.TaskDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskListDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskListScreen

@Composable
fun TaskNavHost() {
    val nav = rememberNavController()
    val store = rememberTaskStore()
    val entry by nav.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    AppShell(
        currentRoute = currentRoute,
        onSelectTab = { tab ->
            nav.navigate(tab.route) {
                // the selected tab is state, not a pile of screens, so keep one entry per tab
                popUpTo(Routes.LIST)
                launchSingleTop = true
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = Routes.LIST,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LIST) {
                // the same tasks either way: only how much of them fits on screen changes
                if (isWideScreen()) {
                    TaskListDetailScreen(
                        tasks = store.tasks,
                        onToggle = { store.toggle(it) },
                        onAdd = { title, description -> store.add(title, description) }
                    )
                } else {
                    TaskListScreen(
                        tasks = store.tasks,
                        onToggle = { store.toggle(it) },
                        onAdd = { title, description -> store.add(title, description) },
                        onTaskClick = { taskId -> nav.navigate(Routes.detail(taskId)) }
                    )
                }
            }
            composable(Routes.DETAIL) { backStackEntry ->
                val task = store.find(backStackEntry.arguments?.getString(Routes.TASK_ID))
                if (task != null) {
                    TaskDetailScreen(
                        task = task,
                        onToggle = { store.toggle(task) },
                        onBack = { nav.popBackStack() }
                    )
                }
            }
            composable(Routes.STATS) {
                StatsScreen(store.tasks)
            }
            composable(Routes.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}

@Composable
private fun isWideScreen(): Boolean =
    currentWindowAdaptiveInfoV2().windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
