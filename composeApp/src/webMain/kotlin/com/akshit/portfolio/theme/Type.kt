package com.akshit.portfolio.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import portfolio.composeapp.generated.resources.Res
import portfolio.composeapp.generated.resources.bricolage_700_opsz24
import portfolio.composeapp.generated.resources.bricolage_800_opsz36
import portfolio.composeapp.generated.resources.bricolage_800_opsz96
import portfolio.composeapp.generated.resources.figtree_400
import portfolio.composeapp.generated.resources.figtree_500
import portfolio.composeapp.generated.resources.figtree_600
import portfolio.composeapp.generated.resources.figtree_700
import portfolio.composeapp.generated.resources.jetbrainsmono_500
import portfolio.composeapp.generated.resources.jetbrainsmono_600
import portfolio.composeapp.generated.resources.jetbrainsmono_700

/**
 * Bricolage Grotesque is an optical-size font. Browsers pick opsz from the font size,
 * so we bundle two cuts: opsz 36 for text under 60px and opsz 96 above.
 */
@Immutable
class Fonts(
    val displayLarge: FontFamily,
    val displaySmall: FontFamily,
    val body: FontFamily,
    val mono: FontFamily,
)

val LocalFonts = staticCompositionLocalOf<Fonts> { error("Fonts not provided") }

@Composable
fun rememberFonts(): Fonts {
    val small = FontFamily(
        Font(Res.font.bricolage_700_opsz24, FontWeight.W700),
        Font(Res.font.bricolage_800_opsz36, FontWeight.W800),
    )
    return Fonts(
        displayLarge = FontFamily(
            Font(Res.font.bricolage_700_opsz24, FontWeight.W700),
            Font(Res.font.bricolage_800_opsz96, FontWeight.W800),
        ),
        displaySmall = small,
        body = FontFamily(
            Font(Res.font.figtree_400, FontWeight.W400),
            Font(Res.font.figtree_500, FontWeight.W500),
            Font(Res.font.figtree_600, FontWeight.W600),
            Font(Res.font.figtree_700, FontWeight.W700),
        ),
        mono = FontFamily(
            Font(Res.font.jetbrainsmono_500, FontWeight.W500),
            Font(Res.font.jetbrainsmono_600, FontWeight.W600),
            Font(Res.font.jetbrainsmono_700, FontWeight.W700),
        ),
    )
}

/** CSS-like half leading: extra line height is split evenly above and below. */
private val CssLeading = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)

fun Fonts.display(size: TextUnit, lineHeight: Float, tracking: Float, weight: Int = 800) = TextStyle(
    fontFamily = if (size.value >= 60f) displayLarge else displaySmall,
    fontWeight = FontWeight(weight),
    fontSize = size,
    lineHeight = lineHeight.em,
    letterSpacing = tracking.em,
    lineHeightStyle = CssLeading,
)

fun Fonts.body(size: TextUnit = 16.sp, weight: Int = 400, lineHeight: Float? = null) = TextStyle(
    fontFamily = body,
    fontWeight = FontWeight(weight),
    fontSize = size,
    lineHeight = lineHeight?.em ?: TextUnit.Unspecified,
    lineHeightStyle = if (lineHeight != null) CssLeading else null,
)

fun Fonts.mono(size: TextUnit = 13.sp, weight: Int = 500, lineHeight: Float? = null) = TextStyle(
    fontFamily = mono,
    fontWeight = FontWeight(weight),
    fontSize = size,
    lineHeight = lineHeight?.em ?: TextUnit.Unspecified,
    lineHeightStyle = if (lineHeight != null) CssLeading else null,
)
