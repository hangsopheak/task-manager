package kh.edu.rupp.taskmanagement.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import kh.edu.rupp.taskmanagement.R

object TaskReminders {
    const val CHANNEL_ID = "task_reminders"

    // a channel is created once; the second call is a no-op
    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
