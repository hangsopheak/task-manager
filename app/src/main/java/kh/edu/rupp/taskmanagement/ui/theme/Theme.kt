package kh.edu.rupp.taskmanagement.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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

private val DarkColors = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    secondary = Amber70,
    onSecondary = Amber20,
    secondaryContainer = Amber30,
    onSecondaryContainer = Amber90,
    tertiary = Slate80,
    onTertiary = Slate20,
    tertiaryContainer = Slate30,
    onTertiaryContainer = Slate90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Gray06,
    onBackground = Gray90,
    surface = Gray06,
    onSurface = Gray90,
    surfaceVariant = GrayBlue30,
    onSurfaceVariant = GrayBlue80,
    surfaceContainerLow = Gray10,
    surfaceContainer = Gray12,
    surfaceContainerHigh = Gray17,
    surfaceContainerHighest = Gray22,
    outline = GrayBlue60,
    outlineVariant = GrayBlue30
)

@Composable
fun TaskManagerTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val context = LocalContext.current
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
