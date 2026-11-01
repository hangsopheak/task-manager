package kh.edu.rupp.taskmanagement.navigation

import androidx.navigation3.runtime.NavKey
import kh.edu.rupp.taskmanagement.R

// the three tabs of AppScaffold, in the order they sit in the bar
enum class TopLevelDestination(
    val key: NavKey,
    val label: Int,
    val icon: Int
) {
    TASKS(TaskList, R.string.tab_tasks, R.drawable.ic_tab_tasks),
    CALENDAR(Calendar, R.string.tab_calendar, R.drawable.ic_tab_calendar),
    STATS(Stats, R.string.tab_stats, R.drawable.ic_tab_stats)
}
