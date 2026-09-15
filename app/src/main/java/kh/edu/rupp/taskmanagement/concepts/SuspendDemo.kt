package kh.edu.rupp.taskmanagement.concepts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuspendDemoViewModel : ViewModel() {
    var status by mutableStateOf("Idle")
        private set

    fun load() {
        viewModelScope.launch {
            status = "Loading"
            status = slowAnswer()
        }
    }

    // stands in for the server until Retrofit arrives
    private suspend fun slowAnswer(): String {
        delay(1500)
        return "Done"
    }
}

@Composable
fun SuspendDemo(vm: SuspendDemoViewModel = viewModel()) {
    Column(Modifier.padding(16.dp)) {
        Text(vm.status)
        Button(onClick = { vm.load() }) { Text("Load") }
    }
}
