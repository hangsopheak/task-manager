package kh.edu.rupp.taskmanagement.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.MainActivity
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

    // the tap carries the task id, so the app lands on the task the reminder is about
    fun post(context: Context, task: Task) {
        if (!hasNotificationPermission(context)) return

        val openTask = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("task://${task.id}")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            task.id.hashCode(),
            openTask,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(task.title)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        context.getSystemService(NotificationManager::class.java).notify(REMINDER_ID, notification)
    }
}
