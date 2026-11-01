package kh.edu.rupp.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kh.edu.rupp.taskmanagement.navigation.TaskNavigation
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                // one activity holds the back stack, and every screen on it is a composable
                TaskNavigation()
            }
        }
    }
}
