package kh.edu.rupp.taskmanagement

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kh.edu.rupp.taskmanagement.navigation.TaskNavHost
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFE", "onCreate")
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                // one activity holds the graph, and every screen inside it is a composable
                TaskNavHost()
            }
        }
    }

    override fun onStart()   { super.onStart();   Log.d("LIFE", "onStart") }
    override fun onResume()  { super.onResume();  Log.d("LIFE", "onResume") }
    override fun onPause()   { super.onPause();   Log.d("LIFE", "onPause") }
    override fun onStop()    { super.onStop();    Log.d("LIFE", "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d("LIFE", "onDestroy") }
}
