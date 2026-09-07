package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LifecycleLog() {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Lifecycle log", style = MaterialTheme.typography.titleLarge)
        Text("Open Logcat and filter it by the tag CONCEPTS", Modifier.padding(top = 16.dp))
        Text("1. Press Home, then come back to the app", Modifier.padding(top = 8.dp))
        Text("2. Rotate the phone", Modifier.padding(top = 8.dp))
        Text("Read the callbacks that run each time", Modifier.padding(top = 16.dp))
    }
}
