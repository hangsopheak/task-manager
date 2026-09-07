package kh.edu.rupp.taskmanagement.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Blue25,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = Amber40,
    onSecondary = Color.White,
    secondaryContainer = Amber90,
    onSecondaryContainer = Amber10,
    tertiary = Slate40,
    onTertiary = Color.White,
    tertiaryContainer = Slate90,
    onTertiaryContainer = Slate10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Gray98,
    onBackground = Gray10,
    surface = Gray98,
    onSurface = Gray10,
    surfaceVariant = GrayBlue90,
    onSurfaceVariant = GrayBlue30,
    surfaceContainerLow = Gray96,
    surfaceContainer = Gray94,
    surfaceContainerHigh = Gray92,
    surfaceContainerHighest = Gray90,
    outline = GrayBlue50,
    outlineVariant = GrayBlue80
)

@Composable
fun TaskManagerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
