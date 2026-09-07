package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemePlayground() {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(
                "Asks the theme",
                Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(
                "Told exact values",
                Modifier.padding(16.dp),
                fontSize = 18.sp,
                color = Color(0xFF103C73),
            )
        }
    }
}
