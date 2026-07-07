package com.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.dfcoding.theme.BackgroundDark
import org.dfcoding.theme.BackgroundLight
import org.dfcoding.theme.ErrorContainerDark
import org.dfcoding.theme.ErrorContainerLight
import org.dfcoding.theme.ErrorDark
import org.dfcoding.theme.ErrorLight
import org.dfcoding.theme.InverseOnSurfaceDark
import org.dfcoding.theme.InverseOnSurfaceLight
import org.dfcoding.theme.InversePrimaryDark
import org.dfcoding.theme.InversePrimaryLight
import org.dfcoding.theme.InverseSurfaceDark
import org.dfcoding.theme.InverseSurfaceLight
import org.dfcoding.theme.OnBackgroundDark
import org.dfcoding.theme.OnBackgroundLight
import org.dfcoding.theme.OnErrorContainerDark
import org.dfcoding.theme.OnErrorContainerLight
import org.dfcoding.theme.OnErrorDark
import org.dfcoding.theme.OnErrorLight
import org.dfcoding.theme.OnPrimaryContainerDark
import org.dfcoding.theme.OnPrimaryContainerLight
import org.dfcoding.theme.OnPrimaryDark
import org.dfcoding.theme.OnPrimaryLight
import org.dfcoding.theme.OnSecondaryContainerDark
import org.dfcoding.theme.OnSecondaryContainerLight
import org.dfcoding.theme.OnSecondaryDark
import org.dfcoding.theme.OnSecondaryLight
import org.dfcoding.theme.OnSurfaceDark
import org.dfcoding.theme.OnSurfaceLight
import org.dfcoding.theme.OnSurfaceVariantDark
import org.dfcoding.theme.OnSurfaceVariantLight
import org.dfcoding.theme.OnTertiaryContainerDark
import org.dfcoding.theme.OnTertiaryContainerLight
import org.dfcoding.theme.OnTertiaryDark
import org.dfcoding.theme.OnTertiaryLight
import org.dfcoding.theme.OutlineDark
import org.dfcoding.theme.OutlineLight
import org.dfcoding.theme.OutlineVariantDark
import org.dfcoding.theme.OutlineVariantLight
import org.dfcoding.theme.PrimaryContainerDark
import org.dfcoding.theme.PrimaryContainerLight
import org.dfcoding.theme.PrimaryDark
import org.dfcoding.theme.PrimaryLight
import org.dfcoding.theme.ScrimDark
import org.dfcoding.theme.ScrimLight
import org.dfcoding.theme.SecondaryContainerDark
import org.dfcoding.theme.SecondaryContainerLight
import org.dfcoding.theme.SecondaryDark
import org.dfcoding.theme.SecondaryLight
import org.dfcoding.theme.SurfaceBrightDark
import org.dfcoding.theme.SurfaceBrightLight
import org.dfcoding.theme.SurfaceContainerDark
import org.dfcoding.theme.SurfaceContainerHighDark
import org.dfcoding.theme.SurfaceContainerHighLight
import org.dfcoding.theme.SurfaceContainerHighestDark
import org.dfcoding.theme.SurfaceContainerHighestLight
import org.dfcoding.theme.SurfaceContainerLight
import org.dfcoding.theme.SurfaceContainerLowDark
import org.dfcoding.theme.SurfaceContainerLowLight
import org.dfcoding.theme.SurfaceContainerLowestDark
import org.dfcoding.theme.SurfaceContainerLowestLight
import org.dfcoding.theme.SurfaceDark
import org.dfcoding.theme.SurfaceDimDark
import org.dfcoding.theme.SurfaceDimLight
import org.dfcoding.theme.SurfaceLight
import org.dfcoding.theme.SurfaceVariantDark
import org.dfcoding.theme.SurfaceVariantLight
import org.dfcoding.theme.TertiaryContainerDark
import org.dfcoding.theme.TertiaryContainerLight
import org.dfcoding.theme.TertiaryDark
import org.dfcoding.theme.TertiaryLight

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    scrim = ScrimLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = InversePrimaryLight,
    surfaceDim = SurfaceDimLight,
    surfaceBright = SurfaceBrightLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = ScrimDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = InversePrimaryDark,
    surfaceDim = SurfaceDimDark,
    surfaceBright = SurfaceBrightDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
)

@Composable
internal fun KeepConsistentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
    ){
        MaterialTheme(
            colorScheme = colorScheme,
            typography = appTypography(),
            shapes = Shapes(),
            content = content
        )
    }



}