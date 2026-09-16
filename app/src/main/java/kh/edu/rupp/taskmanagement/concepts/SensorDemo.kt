package kh.edu.rupp.taskmanagement.concepts

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SensorDemo(manager: SensorManager) {
    val accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    var axes by remember { mutableStateOf(Triple(0f, 0f, 0f)) }

    DisposableEffect(accelerometer) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(e: SensorEvent) {
                axes = Triple(e.values[0], e.values[1], e.values[2])
            }
            override fun onAccuracyChanged(s: Sensor?, a: Int) {}
        }
        manager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose { manager.unregisterListener(listener) }
    }

    Column(Modifier.padding(16.dp)) {
        for (s in manager.getSensorList(Sensor.TYPE_ALL)) Text(s.name)
        Text("x %.2f  y %.2f  z %.2f".format(axes.first, axes.second, axes.third))
    }
}

@Composable
fun SensorDemo() {
    val context = LocalContext.current
    SensorDemo(context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
}
