package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

private data object Home
private data class Details(val value: Int)

@Composable
private fun HomeScreen(onOpen: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Home")
        Button(onClick = onOpen) { Text("Open 42") }
    }
}

@Composable
private fun DetailsScreen(value: Int, onOpenNext: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Details for $value")
        Button(onClick = onOpenNext) { Text("Open next") }
    }
}

@Composable
fun NavDemo() {
    val backStack = remember { mutableStateListOf<Any>(Home) }
    Column {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.weight(1f),
            entryProvider = entryProvider {
                entry<Home> {
                    HomeScreen(onOpen = { backStack.add(Details(42)) })
                }
                entry<Details> { key ->
                    DetailsScreen(
                        value = key.value,
                        onOpenNext = { backStack.add(Details(key.value + 1)) }
                    )
                }
            }
        )
        Text("Back stack: ${backStack.toList()}", Modifier.padding(16.dp))
    }
}
