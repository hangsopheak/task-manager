package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldDemo() {
    // the field keeps nothing: it shows what it is handed and reports what was pressed
    var name by remember { mutableStateOf("") }
    Column(Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your name") },
            isError = name.isBlank(),
            supportingText = { if (name.isBlank()) Text("Name is required") },
        )
        // the red line, the message and the dead button are three things reading one value
        Button(onClick = {}, enabled = name.isNotBlank()) { Text("Save") }
    }
}
