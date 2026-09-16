package kh.edu.rupp.taskmanagement

import android.app.Application
import kh.edu.rupp.taskmanagement.notifications.TaskReminders

class TaskManagementApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskReminders.createChannel(this)
    }
}
