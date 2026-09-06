package kh.edu.rupp.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.ui.TaskDetailScreen
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

class TaskDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                // one hardcoded task: nothing links the two screens yet
                TaskDetailScreen(sampleTasks.first())
            }
        }
    }
}
