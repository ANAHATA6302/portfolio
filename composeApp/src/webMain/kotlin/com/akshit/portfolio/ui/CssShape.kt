package com.akshit.portfolio.ui

import androidx.compose.runtime.Immutable
import kotlin.math.roundToInt
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.layout.layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.min

/** A CSS border-radius length: absolute or a percentage of the box side. */
@Immutable
sealed interface Len {
    data class Abs(val dp: Dp) : Len
    data class Pct(val pct: Float) : Len
}

@Immutable
data class Corner(val x: Len, val y: Len = x)

/**
 * A shape that follows CSS `border-radius` exactly, including elliptical corners, percentages and
 * the spec's proportional scale-down when adjacent radii overflow a side (so `999px 28px 28px 999px`
 * on a short tile renders the way the browser renders it).
 */
@Immutable
data class CssShape(val tl: Corner, val tr: Corner, val br: Corner, val bl: Corner) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Rounded(roundRect(size, density))

    /** The resolved corners for a box of [size], as the browser would draw them. */
    fun roundRect(size: Size, density: Density): RoundRect {
        val w = size.width
        val h = size.height
        fun r(l: Len, side: Float) = when (l) {
            is Len.Abs -> with(density) { l.dp.toPx() }
            is Len.Pct -> side * l.pct / 100f
        }.coerceAtLeast(0f)

        var tlx = r(tl.x, w); var tly = r(tl.y, h)
        var trx = r(tr.x, w); var try_ = r(tr.y, h)
        var brx = r(br.x, w); var bry = r(br.y, h)
        var blx = r(bl.x, w); var bly = r(bl.y, h)

        fun ratio(side: Float, sum: Float) = if (sum > 0f) side / sum else Float.MAX_VALUE
        val f = min(
            1f,
            minOf(ratio(w, tlx + trx), ratio(w, blx + brx), ratio(h, tly + bly), ratio(h, try_ + bry)),
        )
        if (f < 1f) {
            tlx *= f; tly *= f; trx *= f; try_ *= f; brx *= f; bry *= f; blx *= f; bly *= f
        }
        return RoundRect(
            left = 0f, top = 0f, right = w, bottom = h,
            topLeftCornerRadius = CornerRadius(tlx, tly),
            topRightCornerRadius = CornerRadius(trx, try_),
            bottomRightCornerRadius = CornerRadius(brx, bry),
            bottomLeftCornerRadius = CornerRadius(blx, bly),
        )
    }
}

fun css(all: Dp) = Corner(Len.Abs(all)).let { CssShape(it, it, it, it) }

fun css(tl: Dp, tr: Dp, br: Dp, bl: Dp) =
    CssShape(Corner(Len.Abs(tl)), Corner(Len.Abs(tr)), Corner(Len.Abs(br)), Corner(Len.Abs(bl)))

fun cssPct(all: Float) = Corner(Len.Pct(all)).let { CssShape(it, it, it, it) }

/** `border-radius: 999px`. */
val Pill = css(999.dp)
val Circle = cssPct(50f)

fun Dp.abs(): Len = Len.Abs(this)
fun pct(v: Float): Len = Len.Pct(v)

/**
 * Padding that keeps content clear of big curved corners: each horizontal side gets at least
 * [start]/[end], or half of the largest corner radius on that side, whichever is bigger.
 * Vertical padding stays as given. Corners are resolved against the box's final size, so a
 * `999px` side on a short tile counts as the half-height radius it really is.
 */
fun Modifier.cornerPadding(shape: CssShape, start: Dp, top: Dp, end: Dp, bottom: Dp, k: Float = .5f): Modifier =
    layout { m, c ->
        val w = if (c.hasBoundedWidth) c.maxWidth else c.minWidth
        val h = if (c.hasBoundedHeight) c.maxHeight else c.minHeight
        val r = shape.roundRect(Size(w.toFloat(), h.toFloat()), this)
        val l = maxOf(start.toPx(), k * maxOf(r.topLeftCornerRadius.x, r.bottomLeftCornerRadius.x)).roundToInt()
        val rt = maxOf(end.toPx(), k * maxOf(r.topRightCornerRadius.x, r.bottomRightCornerRadius.x)).roundToInt()
        val t = top.roundToPx()
        val b = bottom.roundToPx()
        val inner = Constraints(
            minWidth = (c.minWidth - l - rt).coerceAtLeast(0),
            maxWidth = if (c.hasBoundedWidth) (c.maxWidth - l - rt).coerceAtLeast(0) else Constraints.Infinity,
            minHeight = (c.minHeight - t - b).coerceAtLeast(0),
            maxHeight = if (c.hasBoundedHeight) (c.maxHeight - t - b).coerceAtLeast(0) else Constraints.Infinity,
        )
        val p = m.measure(inner)
        layout(
            (p.width + l + rt).coerceIn(c.minWidth, c.maxWidth),
            (p.height + t + b).coerceIn(c.minHeight, c.maxHeight),
        ) { p.place(l, t) }
    }
