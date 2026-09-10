package kh.edu.rupp.taskmanagement.navigation

// a route is a plain string the graph knows, and the task id travels inside it
object Routes {
    const val LOGIN = "login"
    const val LIST = "list"
    const val DETAIL = "detail/{taskId}"
    const val ADD = "add"
    const val EDIT = "edit/{taskId}"
    const val STATS = "stats"
    const val SETTINGS = "settings"
    const val TASK_ID = "taskId"

    fun detail(taskId: String) = "detail/$taskId"
    fun edit(taskId: String) = "edit/$taskId"
}
