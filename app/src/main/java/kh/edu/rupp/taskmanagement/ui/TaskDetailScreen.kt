package kh.edu.rupp.taskmanagement.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.data.sampleTasks
import kh.edu.rupp.taskmanagement.model.Task
import kh.edu.rupp.taskmanagement.notifications.hasNotificationPermission
import kh.edu.rupp.taskmanagement.location.LocationProvider
import kh.edu.rupp.taskmanagement.location.PlaceNamer
import kh.edu.rupp.taskmanagement.notifications.TaskReminders
import kh.edu.rupp.taskmanagement.notifications.needsRuntimeAsk
import kh.edu.rupp.taskmanagement.notifications.shouldShowRationale
import kh.edu.rupp.taskmanagement.ui.components.ConfirmDeleteDialog
import kh.edu.rupp.taskmanagement.ui.theme.TaskManagerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onToggle: () -> Unit,
    onSetPlace: (String?, Double?, Double?) -> Unit,
    distanceMeters: Int? = null,
    onStartWatching: () -> Unit = {},
    onStopWatching: () -> Unit = {},
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // whether the question is on screen is this screen's business and nobody else's
    var isDeleteAsked by rememberSaveable { mutableStateOf(false) }
    var permissionNote by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val location = LocationProvider(context)

    suspend fun savePlaceHere() {
        val fix = location.currentFix()
        if (fix == null) {
            permissionNote = context.getString(R.string.place_not_found)
            return
        }
        val label = PlaceNamer.label(context, fix)
        onSetPlace(label, fix.latitude, fix.longitude)
    }

    fun takeFixAndSavePlace() {
        scope.launch { savePlaceHere() }
    }

    // the ask happens at the moment the user asked for something that needs it
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) scope.launch { TaskReminders.post(context, task) }
        else permissionNote = context.getString(R.string.permission_denied)
    }
    // two permissions asked together: coarse alone is enough to try
    val placeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) takeFixAndSavePlace()
        else permissionNote = context.getString(R.string.permission_denied)
    }

    fun onSetPlacePressed() {
        when {
            location.hasPermission() -> takeFixAndSavePlace()
            else -> placeLauncher.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    fun onRemindMe() {
        when {
            !needsRuntimeAsk || hasNotificationPermission(context) -> scope.launch { TaskReminders.post(context, task) }
            else -> permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    val shareText = stringResource(R.string.share_task_text, task.title, dueDateText(task.dueDate))
    val chooserTitle = stringResource(R.string.share_chooser_title)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.task_detail_title)) },
                navigationIcon = {
                    // Up and system back do the same thing: leave this destination
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = onEdit
                    ) {
                        Text(stringResource(R.string.edit))
                    }
                    TextButton(
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = { isDeleteAsked = true }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                    TextButton(
                        // a text action defaults to 40dp high, under the 48dp target
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = {
                            // nothing here names an app: Android asks the user who can take this
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, chooserTitle))
                        }
                    ) {
                        Text(stringResource(R.string.share))
                    }
                }
            )
        }
    ) { innerPadding ->
        TaskDetailContent(
            task = task,
            onToggle = onToggle,
            onRemindMe = { onRemindMe() },
            onSetPlace = { onSetPlacePressed() },
            distanceMeters = distanceMeters,
            permissionNote = permissionNote,
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
        )
        // the watching runs exactly as long as this screen is on screen
        LaunchedEffect(task.id) { onStartWatching() }
        DisposableEffect(Unit) {
            onDispose { onStopWatching() }
        }
        if (isDeleteAsked) {
            ConfirmDeleteDialog(
                onConfirm = {
                    isDeleteAsked = false
                    onDelete()
                },
                onDismiss = { isDeleteAsked = false }
            )
        }
    }
}

// the body on its own, so a wide screen can show it beside the list
@Composable
fun TaskDetailContent(
    task: Task,
    onToggle: () -> Unit,
    onRemindMe: () -> Unit = {},
    onSetPlace: () -> Unit = {},
    distanceMeters: Int? = null,
    permissionNote: String? = null,
    modifier: Modifier = Modifier
) {
    val doneLabel = stringResource(R.string.task_done)
    Column(modifier.fillMaxSize()) {
        Text(task.title, style = MaterialTheme.typography.headlineSmall)
        task.placeLabel?.let { place ->
            Text(
                stringResource(R.string.task_place, place),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
            distanceMeters?.let { meters ->
                Text(
                    stringResource(R.string.distance_remaining, meters),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                priorityLabel(task.priority),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                "·",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                stringResource(R.string.task_due, dueDateText(task.dueDate)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(16.dp))
        // the detail screen has the room to show the description in full
        Text(
            task.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(max = 640.dp)
        )
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = onSetPlace,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.set_place))
        }
        OutlinedButton(
            onClick = { onRemindMe() },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.remind_me))
        }
        permissionNote?.let { note ->
            Text(
                note,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(top = 4.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        // the caps do nothing on a phone and stop a wide pane stretching the row
        Card(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    doneLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                // the same event the list card sends, so the two screens cannot disagree
                Switch(
                    checked = task.isDone,
                    onCheckedChange = { onToggle() },
                    modifier = Modifier.semantics { contentDescription = doneLabel }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    TaskManagerTheme {
        TaskDetailScreen(sampleTasks.first(), onToggle = {}, onSetPlace = { _, _, _ -> }, onBack = {}, onEdit = {}, onDelete = {})
    }
}
