package kh.edu.rupp.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.ui.TaskListScreen
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                TaskManagerApp()
            }
        }
    }
}

// TopAppBar is still experimental in Material 3, so the caller has to opt in
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Task Manager") })
        }
    ) { innerPadding ->
        TaskListScreen(sampleTasks, Modifier.padding(innerPadding))
    }
}
