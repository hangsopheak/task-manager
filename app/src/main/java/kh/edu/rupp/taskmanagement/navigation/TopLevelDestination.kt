package kh.edu.rupp.taskmanagement.navigation

import kh.edu.rupp.taskmanagement.R

// the three tabs of the shell, in the order they sit in the bar
enum class TopLevelDestination(
    val route: String,
    val label: Int,
    val icon: Int
) {
    TASKS(Routes.LIST, R.string.tab_tasks, R.drawable.ic_tab_tasks),
    STATS(Routes.STATS, R.string.tab_stats, R.drawable.ic_tab_stats),
    SETTINGS(Routes.SETTINGS, R.string.tab_settings, R.drawable.ic_tab_settings)
}
