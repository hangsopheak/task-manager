package kh.edu.rupp.taskmanagement.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.ui.AppShell
import kh.edu.rupp.taskmanagement.ui.LoginScreen
import kh.edu.rupp.taskmanagement.ui.SettingsScreen
import kh.edu.rupp.taskmanagement.ui.StatsScreen
import kh.edu.rupp.taskmanagement.ui.TaskDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskFormScreen
import kh.edu.rupp.taskmanagement.ui.LoginViewModel
import kh.edu.rupp.taskmanagement.ui.TaskListDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskListScreen
import kh.edu.rupp.taskmanagement.ui.TaskUiState
import kh.edu.rupp.taskmanagement.ui.TaskViewModel
import kh.edu.rupp.taskmanagement.ui.components.LoadingView

@Composable
fun TaskNavHost() {
    val nav = rememberNavController()
    // one view model above the graph, so every screen reads and changes the same state
    val vm: TaskViewModel = viewModel()
    val loginVm: LoginViewModel = viewModel()
    LaunchedEffect(Unit) { vm.load() }
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
            startDestination = if (loginVm.signedInAtStart) Routes.LIST else Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                // the buttons are real now: both of them end in the same place, signed in
                val openTasks = {
                    nav.navigate(Routes.LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
                LoginScreen(vm = loginVm, onSignIn = openTasks)
            }
            composable(Routes.LIST) {
                // the same state either way: only how much of it fits on screen changes
                if (isWideScreen()) {
                    TaskListDetailScreen(
                        state = vm.state,
                        onRetry = { vm.load() },
                        onToggle = { vm.toggle(it) },
                        onAdd = { nav.navigate(Routes.ADD) },
                        message = vm.message,
                        onMessageShown = { vm.onMessageShown() }
                    )
                } else {
                    TaskListScreen(
                        state = vm.state,
                        onRetry = { vm.load() },
                        onToggle = { vm.toggle(it) },
                        onAdd = { nav.navigate(Routes.ADD) },
                        onTaskClick = { taskId -> nav.navigate(Routes.detail(taskId)) },
                        message = vm.message,
                        onMessageShown = { vm.onMessageShown() }
                    )
                }
            }
            composable(Routes.DETAIL) { backStackEntry ->
                val task = vm.find(backStackEntry.arguments?.getString(Routes.TASK_ID))
                if (task != null) {
                    TaskDetailScreen(
                        task = task,
                        onToggle = { vm.toggle(task) },
                        onBack = { nav.popBackStack() },
                        onEdit = { nav.navigate(Routes.edit(task.id)) },
                        onDelete = {
                            vm.delete(task)
                            nav.popBackStack()
                        }
                    )
                } else {
                    LoadingView()
                }
            }
            composable(Routes.ADD) {
                TaskFormScreen(
                    task = null,
                    otherTitles = vm.taskTitles(excludeId = null),
                    onBack = { nav.popBackStack() },
                    onSave = { title, description, dueDate, priority ->
                        vm.save(null, title, description, dueDate, priority)
                        nav.popBackStack()
                    }
                )
            }
            composable(Routes.EDIT) { backStackEntry ->
                val task = vm.find(backStackEntry.arguments?.getString(Routes.TASK_ID))
                if (task != null) {
                    // the task being edited keeps its own title, so leave it out of the list
                    TaskFormScreen(
                        task = task,
                        otherTitles = vm.taskTitles(excludeId = task.id),
                        onBack = { nav.popBackStack() },
                        onSave = { title, description, dueDate, priority ->
                            vm.save(task.id, title, description, dueDate, priority)
                            nav.popBackStack()
                        }
                    )
                } else {
                    LoadingView()
                }
            }
            composable(Routes.STATS) {
                StatsScreen(vm.state, onRetry = { vm.load() })
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    email = loginVm.userEmail,
                    onSignOut = {
                        loginVm.signOut()
                        nav.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LIST) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun isWideScreen(): Boolean =
    currentWindowAdaptiveInfoV2().windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
