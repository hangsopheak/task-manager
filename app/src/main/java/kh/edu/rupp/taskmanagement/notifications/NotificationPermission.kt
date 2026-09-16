package kh.edu.rupp.taskmanagement.notifications

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// the permission does not exist below Android 13, so asking there reports denied for ever
val needsRuntimeAsk: Boolean
    get() = Build.VERSION.SDK_INT >= 33

fun hasNotificationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

// two denials means the system stopped asking, so the app sends the user to Settings instead
fun shouldShowRationale(activity: Activity): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.POST_NOTIFICATIONS
    )
