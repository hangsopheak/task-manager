package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CounterDemo() {
    // mutableStateOf makes the value worth watching, remember keeps it across the next call
    var count by remember { mutableStateOf(0) }
    Column(Modifier.padding(16.dp)) {
        Text("Tapped $count times")
        Button(onClick = { count++ }) { Text("Tap me") }
    }
}
