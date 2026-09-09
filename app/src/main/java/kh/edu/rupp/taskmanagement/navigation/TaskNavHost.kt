package kh.edu.rupp.taskmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kh.edu.rupp.taskmanagement.data.rememberTaskStore
import kh.edu.rupp.taskmanagement.ui.TaskDetailScreen
import kh.edu.rupp.taskmanagement.ui.TaskListScreen

@Composable
fun TaskNavHost() {
    val nav = rememberNavController()
    val store = rememberTaskStore()
    NavHost(navController = nav, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            TaskListScreen(
                tasks = store.tasks,
                onToggle = { store.toggle(it) },
                onAdd = { title, description -> store.add(title, description) },
                onTaskClick = { taskId -> nav.navigate(Routes.detail(taskId)) }
            )
        }
        composable(Routes.DETAIL) { entry ->
            val task = store.find(entry.arguments?.getString(Routes.TASK_ID))
            if (task != null) {
                TaskDetailScreen(
                    task = task,
                    onToggle = { store.toggle(task) },
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}
