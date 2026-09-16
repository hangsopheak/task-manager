package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kh.edu.rupp.taskmanagement.R
import kh.edu.rupp.taskmanagement.ui.components.SettingsRadioRow
import kh.edu.rupp.taskmanagement.ui.components.SettingsSectionLabel
import kh.edu.rupp.taskmanagement.ui.components.SwitchRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    sortOrder: SortOrder,
    onSortChange: (SortOrder) -> Unit,
    themeChoice: ThemeChoice,
    onThemeChange: (ThemeChoice) -> Unit,
    remindersEnabled: Boolean,
    onRemindersChange: (Boolean) -> Unit,
    email: String,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SettingsSectionLabel(R.string.settings_sort_order)
            SettingsRadioRow(R.string.settings_sort_due_date, sortOrder == SortOrder.DUE_DATE) { onSortChange(SortOrder.DUE_DATE) }
            SettingsRadioRow(R.string.settings_sort_priority, sortOrder == SortOrder.PRIORITY) { onSortChange(SortOrder.PRIORITY) }
            SettingsRadioRow(R.string.settings_sort_title, sortOrder == SortOrder.TITLE) { onSortChange(SortOrder.TITLE) }
            SettingsSectionLabel(R.string.settings_theme)
            SettingsRadioRow(R.string.settings_theme_system, themeChoice == ThemeChoice.SYSTEM) { onThemeChange(ThemeChoice.SYSTEM) }
            SettingsRadioRow(R.string.settings_theme_light, themeChoice == ThemeChoice.LIGHT) { onThemeChange(ThemeChoice.LIGHT) }
            SettingsRadioRow(R.string.settings_theme_dark, themeChoice == ThemeChoice.DARK) { onThemeChange(ThemeChoice.DARK) }
            SettingsSectionLabel(R.string.settings_reminders)
            SwitchRow(R.string.settings_reminders, remindersEnabled, onRemindersChange)
            SettingsSectionLabel(R.string.settings_account)
            AccountSection(email = email, onSignOut = onSignOut)
        }
    }
}

@Composable
private fun SettingRow(label: Int, session: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(label),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            stringResource(R.string.settings_from_session, session),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// the first real row: who is signed in, and the way out
@Composable
private fun AccountSection(email: String, onSignOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(R.string.settings_account),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedButton(onClick = onSignOut) {
            Text(stringResource(R.string.settings_sign_out))
        }
    }
}
