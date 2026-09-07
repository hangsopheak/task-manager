package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme

@Composable
fun LayoutPlayground() {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("left")
            Text("right")
        }
        Box(Modifier.fillMaxWidth().height(80.dp).background(Color.LightGray)) {
            Text("bottom end", Modifier.align(Alignment.BottomEnd))
        }
        Text("padding then background", Modifier.padding(8.dp).background(Color.Yellow))
        Text("background then padding", Modifier.background(Color.Yellow).padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutPlaygroundPreview() {
    TaskManagerTheme {
        LayoutPlayground()
    }
}
