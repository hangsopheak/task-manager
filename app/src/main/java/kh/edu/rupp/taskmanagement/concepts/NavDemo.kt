package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavDemo() {
    // one composable that swaps its own content for whichever name it is given
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "one") {
        composable("one") {
            Column(Modifier.padding(16.dp)) {
                Text("Screen one")
                Button(onClick = { nav.navigate("two/42") }) { Text("Open two with 42") }
            }
        }
        composable("two/{value}") { entry ->
            Column(Modifier.padding(16.dp)) {
                Text("Screen two got " + entry.arguments?.getString("value"))
                Button(onClick = { nav.popBackStack() }) { Text("Back") }
            }
        }
    }
}
