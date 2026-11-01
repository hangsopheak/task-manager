package kh.edu.rupp.taskmanagement.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.navigation.TopLevelDestination

@Composable
fun AppScaffold(
    selected: TopLevelDestination,
    onSelectTab: (TopLevelDestination) -> Unit,
    content: @Composable () -> Unit
) {
    // the scaffold reads the window: a bar on a phone, a rail down the side on a wide window
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { tab ->
                item(
                    selected = tab == selected,
                    onClick = { onSelectTab(tab) },
                    icon = {
                        // the label under it already names the tab out loud
                        Icon(painterResource(tab.icon), contentDescription = null)
                    },
                    label = { Text(stringResource(tab.label)) }
                )
            }
        },
        content = content
    )
}
