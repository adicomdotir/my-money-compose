package ir.adicom.mymoney.ui.theme

import android.app.Activity
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

private val DarkColorScheme = darkColorScheme(
    primary = Emerald80,
    onPrimary = Emerald20,
    primaryContainer = Emerald30,
    onPrimaryContainer = Emerald90,

    secondary = Slate80,
    onSecondary = Slate20,

    tertiary = Teal80,
    onTertiary = Color(0xFF003739),

    background = NeutralDark,
    onBackground = Color(0xFFE2E8F0),
    surface = NeutralDarkSurf,
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Slate30,
    onSurfaceVariant = Slate80,

    error = ErrorRed80,
    onError = Color(0xFF690005),
)

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Emerald40,
    onPrimary = Color.White,
    primaryContainer = Emerald90,
    onPrimaryContainer = Emerald10,

    secondary = Slate40,
    onSecondary = Color.White,
    secondaryContainer = Slate90,
    onSecondaryContainer = Slate10,

    tertiary = Teal40,
    onTertiary = Color.White,

    background = NeutralLight,
    onBackground = Slate10,
    surface = NeutralSurface,
    onSurface = Slate10,
    surfaceVariant = NeutralVariant,
    onSurfaceVariant = Slate30,

    error = ErrorRed,
    onError = Color.White,
)

@Composable
fun MyMoneyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
