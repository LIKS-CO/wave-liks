package com.example.liks_sports.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.liks_sports.data.SettingsStore.ThemePalette

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private val KovaDarkColorScheme = darkColorScheme(
    primary = KovaVitality,
    onPrimary = KovaStone,
    primaryContainer = KovaDeepGreen,
    onPrimaryContainer = KovaMist,
    secondary = KovaMist,
    onSecondary = KovaObsidian,
    secondaryContainer = KovaSlate,
    onSecondaryContainer = KovaMist,
    tertiary = KovaTeal,
    onTertiary = KovaObsidian,
    tertiaryContainer = KovaDeepGreen,
    onTertiaryContainer = KovaMist,
    error = KovaSignal,
    onError = KovaObsidian,
    errorContainer = KovaAmberDark,
    onErrorContainer = KovaAmberLight,
    background = KovaObsidian,
    onBackground = KovaStone,
    surface = KovaObsidian,
    onSurface = KovaStone,
    surfaceVariant = KovaSlate,
    onSurfaceVariant = KovaMistMuted,
    outline = KovaOutlineDark,
    outlineVariant = KovaOutlineVariantDark,
)

private val KovaLightColorScheme = lightColorScheme(
    primary = KovaVitality,
    onPrimary = KovaStone,
    primaryContainer = KovaMist,
    onPrimaryContainer = KovaObsidian,
    secondary = KovaTeal,
    onSecondary = KovaObsidian,
    secondaryContainer = KovaMist,
    onSecondaryContainer = KovaObsidian,
    tertiary = KovaTeal,
    onTertiary = KovaObsidian,
    tertiaryContainer = KovaMist,
    onTertiaryContainer = KovaObsidian,
    error = KovaSignal,
    onError = KovaObsidian,
    errorContainer = KovaAmberLight,
    onErrorContainer = KovaAmberDark,
    background = KovaStone,
    onBackground = KovaObsidian,
    surface = KovaStone,
    onSurface = KovaObsidian,
    surfaceVariant = KovaStoneVariant,
    onSurfaceVariant = KovaOnSurfaceVariantLight,
    outline = KovaOutline,
    outlineVariant = KovaOutlineVariantLight,
)

private val LiksDarkColorScheme = darkColorScheme(
    primary = LiksBlueDark,
    onPrimary = LiksOnBlueDark,
    primaryContainer = LiksBlueContainerDark,
    onPrimaryContainer = LiksOnBlueContainerDark,
    secondary = LiksGreenDark,
    onSecondary = LiksOnGreenDark,
    secondaryContainer = LiksGreenContainerDark,
    onSecondaryContainer = LiksOnGreenContainerDark,
    tertiary = LiksYellowDark,
    onTertiary = LiksOnYellowDark,
    tertiaryContainer = LiksYellowContainerDark,
    onTertiaryContainer = LiksOnYellowContainerDark,
    error = LiksRedDark,
    onError = LiksOnRedDark,
    errorContainer = LiksRedContainerDark,
    onErrorContainer = LiksOnRedContainerDark,
    background = LiksBackgroundDark,
    onBackground = LiksOnBackgroundDark,
    surface = LiksBackgroundDark,
    onSurface = LiksOnBackgroundDark,
    surfaceVariant = LiksSurfaceVariantDark,
    onSurfaceVariant = LiksOnSurfaceVariantDark,
    outline = LiksOutlineDark,
    outlineVariant = LiksOutlineVariantDark,
)

private val LiksLightColorScheme = lightColorScheme(
    primary = LiksBlue,
    onPrimary = KovaStone,
    primaryContainer = LiksBlueContainer,
    onPrimaryContainer = LiksOnBlueContainer,
    secondary = LiksGreen,
    onSecondary = KovaStone,
    secondaryContainer = LiksGreenContainer,
    onSecondaryContainer = LiksOnGreenContainer,
    tertiary = LiksYellow,
    onTertiary = LiksYellowOn,
    tertiaryContainer = LiksYellowContainer,
    onTertiaryContainer = LiksOnYellowContainer,
    error = LiksRed,
    onError = KovaStone,
    errorContainer = LiksRedContainer,
    onErrorContainer = LiksOnRedContainer,
    background = LiksSurface,
    onBackground = LiksOnSurface,
    surface = LiksSurface,
    onSurface = LiksOnSurface,
    surfaceVariant = LiksSurfaceVariant,
    onSurfaceVariant = LiksOnSurfaceVariant,
    outline = LiksOutline,
    outlineVariant = LiksOutlineVariant,
)

@Composable
fun LikssportsTheme(
    palette: ThemePalette = ThemePalette.MATERIAL_YOU,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (palette) {
        ThemePalette.MATERIAL_YOU -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (darkTheme) DarkColorScheme else LightColorScheme
            }
        }
        ThemePalette.KOVA -> if (darkTheme) KovaDarkColorScheme else KovaLightColorScheme
        ThemePalette.LIKS -> if (darkTheme) LiksDarkColorScheme else LiksLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
