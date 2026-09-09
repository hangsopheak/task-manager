package kh.edu.rupp.taskmanagement.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.navigation.TopLevelDestination

@Composable
fun AppShell(
    currentRoute: String?,
    onSelectTab: (TopLevelDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val tabs = TopLevelDestination.entries
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // every screen brings its own top bar, so the shell only measures the bar at the bottom
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // the bar belongs to the three tabs: anything pushed over them is full screen
            if (tabs.any { it.route == currentRoute }) {
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = tab.route == currentRoute,
                            onClick = { onSelectTab(tab) },
                            icon = {
                                // the label under it already names the tab out loud
                                Icon(painterResource(tab.icon), contentDescription = null)
                            },
                            label = { Text(stringResource(tab.label)) }
                        )
                    }
                }
            }
        },
        content = content
    )
}
