package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Blue500,
    onPrimary = PureWhite,
    primaryContainer = Blue800,
    onPrimaryContainer = Blue100,
    secondary = Teal500,
    onSecondary = PureWhite,
    secondaryContainer = Teal800,
    onSecondaryContainer = Teal100,
    tertiary = Amber500,
    background = Blue900,
    surface = Neutral800,
    onBackground = Neutral100,
    onSurface = Neutral100,
    surfaceVariant = Neutral700,
    onSurfaceVariant = Neutral300,
    outline = Neutral500
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Blue600,
    onPrimary = PureWhite,
    primaryContainer = Blue50,
    onPrimaryContainer = Blue800,
    secondary = Teal600,
    onSecondary = PureWhite,
    secondaryContainer = Teal50,
    onSecondaryContainer = Teal800,
    tertiary = Amber600,
    background = Neutral50,
    surface = PureWhite,
    onBackground = Neutral900,
    onSurface = Neutral900,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral600,
    outline = Neutral200
  )

@Composable
fun ClaimPilotTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep branded colors consistent
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
