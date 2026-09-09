package kh.edu.rupp.taskmanagement.navigation

// a route is a plain string the graph knows, and the task id travels inside it
object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{taskId}"
    const val TASK_ID = "taskId"

    fun detail(taskId: String) = "detail/$taskId"
}
