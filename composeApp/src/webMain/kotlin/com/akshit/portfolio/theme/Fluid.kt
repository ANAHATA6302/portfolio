package com.akshit.portfolio.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * CSS `clamp(min, x cqw, max)` against the root container width, plus the
 * compact breakpoint (width < 760).
 */
@Immutable
class Fluid(val width: Float, val height: Float) {
    val compact get() = width < 760f

    /** clamp(min, pct% of width, max) in dp. */
    fun c(min: Number, pct: Number, max: Number): Dp =
        (width * pct.toFloat() / 100f).coerceIn(min.toFloat(), max.toFloat()).dp

    /** Same as [c] for font sizes (1sp == 1dp on the web). */
    fun sp(min: Number, pct: Number, max: Number): TextUnit =
        (width * pct.toFloat() / 100f).coerceIn(min.toFloat(), max.toFloat()).sp

    /** Side margin clamp(16, 4cqw, 56). */
    val side get() = c(16, 4, 56)

    /** Section top padding clamp(72, 9cqw, 140). */
    val sectionTop get() = c(72, 9, 140)
}

val LocalFluid = staticCompositionLocalOf { Fluid(1440f, 900f) }

const val MAX_CONTENT = 1328
