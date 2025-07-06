package com.example.acts2hymnal.ui.theme

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

private val ColorMap = mapOf(
    "text" to Color(0xFFD5D3E3),        // #d5d3e3
    "background" to Color(0xFF101014),  // #101014
    "primary" to Color(0xFF058484),     // #058484
    "secondary" to Color(0xFFFFFFFF),   // #ffffff
    "accent" to Color(0xFF15C996)       // #15c996
)

fun getColor(key: String, default: Color = Color.Black): Color {
    return ColorMap[key] ?: default
}

private val DarkColorScheme = darkColorScheme(
    primary = getColor("primary"),
    secondary = getColor("secondary"),
    tertiary = getColor("accent"),
    background = getColor("background"),
    onBackground = getColor("text")
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,


/* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/
)

@Composable
fun Acts2HymnalTheme(
//  darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = true,
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
        //else -> LightColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}