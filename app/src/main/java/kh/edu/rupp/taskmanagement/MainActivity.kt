package kh.edu.rupp.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import kh.edu.rupp.taskmanagement.navigation.TaskNavHost
import kh.edu.rupp.taskmanagement.ui.TaskViewModel
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // a tap on a reminder arrives here, carrying which task it was about
        val openTaskId = intent?.data?.lastPathSegment
        setContent {
            val vm: TaskViewModel = viewModel()
            TaskManagerTheme(themeChoice = vm.themeChoice) {
                // one activity holds the graph, and every screen inside it is a composable
                TaskNavHost(vm = vm, openTaskId = openTaskId)
            }
        }
    }
}
