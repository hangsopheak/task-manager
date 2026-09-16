package kh.edu.rupp.taskmanagement.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.model.Task

object TaskReminders {
    const val CHANNEL_ID = "task_reminders"
    private const val REMINDER_ID = 1001

    // a channel is created once; the second call is a no-op
    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    // the reminder itself: the task title, on the channel the user controls
    fun post(context: Context, task: Task) {
        if (!hasNotificationPermission(context)) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(task.title)
            .setAutoCancel(true)
            .build()

        context.getSystemService(NotificationManager::class.java).notify(REMINDER_ID, notification)
    }
}
