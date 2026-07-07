package org.dfcoding.theme

import androidx.compose.ui.graphics.Color


// ---------------------------------------------------------------------------
// Raw palette (from Color Palette reference)
// ---------------------------------------------------------------------------

// Primary (blue)
val Primary50 = Color(0xFFEAF2FC)
val Primary100 = Color(0xFFBCD5F7)
val Primary200 = Color(0xFF9CC1F3)
val Primary300 = Color(0xFF6FA5ED)
val Primary400 = Color(0xFF5394E9)
val Primary500 = Color(0xFF2879E4)
val Primary600 = Color(0xFF246ECF)
val Primary700 = Color(0xFF1C58A2)
val Primary800 = Color(0xFF16437D)
val Primary900 = Color(0xFF113360)

// White (light neutrals)
val White50 = Color(0xFFFFFFFF)
val White100 = Color(0xFFFDFDFD)
val White200 = Color(0xFFFDFDFD)
val White300 = Color(0xFFFCFCFC)
val White400 = Color(0xFFFBFBFB)
val White500 = Color(0xFFFAFAFA)
val White600 = Color(0xFFE4E4E4)
val White700 = Color(0xFFB2B2B2)
val White800 = Color(0xFF8A8A8A)
val White900 = Color(0xFF696969)

// Black (dark neutrals)
val Black50 = Color(0xFFE9E9E9)
val Black100 = Color(0xFFB9B9B9)
val Black200 = Color(0xFF989898)
val Black300 = Color(0xFF686868)
val Black400 = Color(0xFF4B4B4B)
val Black500 = Color(0xFF1E1E1E)
val Black600 = Color(0xFF1B1B1B)
val Black700 = Color(0xFF151515)
val Black800 = Color(0xFF111111)
val Black900 = Color(0xFF0D0D0D)

// Green (accent / success)
val Green50 = Color(0xFFEBF6EE)
val Green100 = Color(0xFFC0E4CA)
val Green200 = Color(0xFFA2D7B0)
val Green300 = Color(0xFF77C58C)
val Green400 = Color(0xFF5DB975)
val Green500 = Color(0xFF34A853)
val Green600 = Color(0xFF2F994C)
val Green700 = Color(0xFF25773B)
val Green800 = Color(0xFF1D5C2E)
val Green900 = Color(0xFF164723)

// Error — not defined in the source palette; standard Material3 error red used instead
val Error10 = Color(0xFF410E0B)
val Error20 = Color(0xFF601410)
val Error30 = Color(0xFF8C1D18)
val Error80 = Color(0xFFF2B8B5)
val Error90 = Color(0xFFF9DEDC)
val ErrorMain = Color(0xFFB3261E)

// ---------------------------------------------------------------------------
// Light color scheme
// ---------------------------------------------------------------------------

val PrimaryLight = Primary500
val OnPrimaryLight = White50
val PrimaryContainerLight = Primary100
val OnPrimaryContainerLight = Primary900

val SecondaryLight = Black400
val OnSecondaryLight = White50
val SecondaryContainerLight = Black50
val OnSecondaryContainerLight = Black800

val TertiaryLight = Green500
val OnTertiaryLight = White50
val TertiaryContainerLight = Green100
val OnTertiaryContainerLight = Green900

val ErrorLight = ErrorMain
val OnErrorLight = White50
val ErrorContainerLight = Error90
val OnErrorContainerLight = Error10

val BackgroundLight = White50
val OnBackgroundLight = Black900
val SurfaceLight = White50
val OnSurfaceLight = Black900
val SurfaceVariantLight = White600
val OnSurfaceVariantLight = Black400
val OutlineLight = Black200
val OutlineVariantLight = White600
val ScrimLight = Black900
val InverseSurfaceLight = Black800
val InverseOnSurfaceLight = White50
val InversePrimaryLight = Primary300
val SurfaceDimLight = White600
val SurfaceBrightLight = White50
val SurfaceContainerLowestLight = White50
val SurfaceContainerLowLight = White400
val SurfaceContainerLight = White500
val SurfaceContainerHighLight = White600
val SurfaceContainerHighestLight = White700

// ---------------------------------------------------------------------------
// Dark color scheme
// ---------------------------------------------------------------------------

val PrimaryDark = Primary300
val OnPrimaryDark = Primary900
val PrimaryContainerDark = Primary700
val OnPrimaryContainerDark = Primary100

val SecondaryDark = Black100
val OnSecondaryDark = Black900
val SecondaryContainerDark = Black700
val OnSecondaryContainerDark = White100

val TertiaryDark = Green300
val OnTertiaryDark = Green900
val TertiaryContainerDark = Green700
val OnTertiaryContainerDark = Green100

val ErrorDark = Error80
val OnErrorDark = Error20
val ErrorContainerDark = Error30
val OnErrorContainerDark = Error90

val BackgroundDark = Black900
val OnBackgroundDark = White100
val SurfaceDark = Black900
val OnSurfaceDark = White100
val SurfaceVariantDark = Black600
val OnSurfaceVariantDark = White700
val OutlineDark = Black200
val OutlineVariantDark = Black600
val ScrimDark = Black900
val InverseSurfaceDark = White100
val InverseOnSurfaceDark = Black900
val InversePrimaryDark = Primary600
val SurfaceDimDark = Black900
val SurfaceBrightDark = Black600
val SurfaceContainerLowestDark = Black900
val SurfaceContainerLowDark = Black800
val SurfaceContainerDark = Black700
val SurfaceContainerHighDark = Black600
val SurfaceContainerHighestDark = Black500
