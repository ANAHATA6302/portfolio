package com.akshit.portfolio.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

enum class Justify { Start, Center, End, SpaceBetween }
enum class Align { Start, Center, End, Stretch }

/** CSS flex item properties. A null basis means `auto` (the content width). */
@Immutable
data class Flex(
    val grow: Float = 0f,
    val shrink: Float = 1f,
    val basis: Dp? = null,
    /** Null is CSS `min-width: auto`, the min-content width. */
    val min: Dp? = null,
    val max: Dp = Dp.Infinity,
    val autoStart: Boolean = false,
    val autoEnd: Boolean = false,
    val alignSelf: Align? = null,
)

private class FlexData(val flex: Flex) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = flex
}

/** Attach flex item properties to a child of [FlexWrap]. */
fun Modifier.flex(
    grow: Float = 0f,
    shrink: Float = 1f,
    basis: Dp? = null,
    min: Dp? = null,
    max: Dp = Dp.Infinity,
    autoStart: Boolean = false,
    autoEnd: Boolean = false,
    alignSelf: Align? = null,
): Modifier = this.then(FlexData(Flex(grow, shrink, basis, min, max, autoStart, autoEnd, alignSelf)))

/**
 * A row that emulates CSS `display:flex; flex-wrap:wrap` (or nowrap): flex-basis, grow and shrink,
 * min and max widths, auto margins, justify-content and align-items (stretch uses intrinsics).
 */
@Composable
fun FlexWrap(
    modifier: Modifier = Modifier,
    gap: Dp = 0.dp,
    rowGap: Dp = gap,
    justify: Justify = Justify.Start,
    align: Align = Align.Stretch,
    wrap: Boolean = true,
    content: @Composable () -> Unit,
) {
    Layout(content, modifier, remember(gap, rowGap, justify, align, wrap) { FlexPolicy(gap, rowGap, justify, align, wrap) })
}

private class Item(val m: IntrinsicMeasurable, val f: Flex) {
    var base = 0f
    var hypo = 0f
    var minPx = 0f
    var maxPx = Float.POSITIVE_INFINITY
    var size = 0f
    var frozen = false
}

private class Line(val items: List<Item>)

private class FlexPolicy(
    val gap: Dp,
    val rowGap: Dp,
    val justify: Justify,
    val align: Align,
    val wrap: Boolean,
) : MeasurePolicy {

    private fun IntrinsicMeasureScope.items(ms: List<IntrinsicMeasurable>): List<Item> = ms.map { m ->
        val f = (m.parentData as? Flex) ?: Flex()
        Item(m, f).apply {
            minPx = f.min?.toPx() ?: m.minIntrinsicWidth(Constraints.Infinity).toFloat()
            maxPx = if (f.max == Dp.Infinity) Float.POSITIVE_INFINITY else f.max.toPx()
            base = f.basis?.toPx() ?: m.maxIntrinsicWidth(Constraints.Infinity).toFloat()
            hypo = base.coerceIn(minPx, max(minPx, maxPx))
        }
    }

    private fun IntrinsicMeasureScope.lines(items: List<Item>, width: Float): List<Line> {
        val g = gap.toPx()
        if (!wrap || width == Float.POSITIVE_INFINITY) return listOf(Line(items))
        val out = mutableListOf<Line>()
        var cur = mutableListOf<Item>()
        var used = 0f
        for (it in items) {
            val add = if (cur.isEmpty()) it.hypo else used + g + it.hypo
            if (cur.isNotEmpty() && add > width + 0.5f) {
                out += Line(cur); cur = mutableListOf(it); used = it.hypo
            } else {
                cur += it; used = add
            }
        }
        if (cur.isNotEmpty()) out += Line(cur)
        return out
    }

    /** CSS "resolve flexible lengths", with min/max freezing. */
    private fun IntrinsicMeasureScope.resolve(line: Line, width: Float) {
        val g = gap.toPx() * (line.items.size - 1)
        val items = line.items
        items.forEach { it.size = it.hypo; it.frozen = false }
        if (width == Float.POSITIVE_INFINITY) return
        val growing = items.sumOf { it.hypo.toDouble() }.toFloat() + g < width
        items.forEach {
            if ((growing && it.f.grow == 0f) || (!growing && it.f.shrink == 0f) ||
                (growing && it.base > it.hypo) || (!growing && it.base < it.hypo)
            ) it.frozen = true
        }
        repeat(items.size + 1) {
            val active = items.filter { !it.frozen }
            if (active.isEmpty()) return
            val used = items.sumOf { (if (it.frozen) it.size else it.base).toDouble() }.toFloat() + g
            val free = width - used
            if (growing) {
                val sum = active.sumOf { it.f.grow.toDouble() }.toFloat()
                val share = if (sum < 1f) free * sum else free
                active.forEach { it.size = it.base + share * it.f.grow / sum }
            } else {
                val sum = active.sumOf { (it.f.shrink * it.base).toDouble() }.toFloat()
                active.forEach { it.size = if (sum > 0f) it.base + free * (it.f.shrink * it.base) / sum else it.base }
            }
            var violation = 0f
            active.forEach {
                val c = it.size.coerceIn(it.minPx, max(it.minPx, it.maxPx))
                violation += c - it.size
            }
            if (violation == 0f) { active.forEach { it.frozen = true }; return }
            active.forEach {
                val c = it.size.coerceIn(it.minPx, max(it.minPx, it.maxPx))
                if ((violation > 0 && c > it.size) || (violation < 0 && c < it.size)) { it.size = c; it.frozen = true }
            }
        }
    }

    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val width = if (constraints.hasBoundedWidth) constraints.maxWidth.toFloat() else Float.POSITIVE_INFINITY
        val items = items(measurables)
        val lines = lines(items, width)
        val g = gap.toPx()
        val rg = rowGap.toPx().roundToInt()
        class Placed(val p: androidx.compose.ui.layout.Placeable, val x: Int)
        val placedLines = mutableListOf<Pair<List<Placed>, Int>>()
        var contentW = 0
        for (line in lines) {
            resolve(line, width)
            val sizes = line.items.map { it.size.roundToInt().coerceAtLeast(0) }
            val stretchH = if (line.items.any { (it.f.alignSelf ?: align) == Align.Stretch }) {
                line.items.mapIndexed { i, it -> it.m.maxIntrinsicHeight(sizes[i]) }.maxOrNull() ?: 0
            } else 0
            val ps = line.items.mapIndexed { i, it ->
                val a = it.f.alignSelf ?: align
                val c = if (a == Align.Stretch) Constraints.fixed(sizes[i], stretchH)
                else Constraints(minWidth = sizes[i], maxWidth = sizes[i])
                (it.m as Measurable).measure(c)
            }
            val usedW = ps.sumOf { it.width } + g * (ps.size - 1)
            val free = if (width == Float.POSITIVE_INFINITY) 0f else max(0f, width - usedW)
            val autos = line.items.sumOf { (if (it.f.autoStart) 1 else 0) + (if (it.f.autoEnd) 1 else 0) }
            var x: Float
            var between = g
            val autoShare = if (autos > 0) free / autos else 0f
            if (autos > 0) x = 0f else when (justify) {
                Justify.Start -> x = 0f
                Justify.Center -> x = free / 2f
                Justify.End -> x = free
                Justify.SpaceBetween -> { x = 0f; if (ps.size > 1) between = g + free / (ps.size - 1) }
            }
            val placed = mutableListOf<Placed>()
            line.items.forEachIndexed { i, it ->
                if (it.f.autoStart) x += autoShare
                placed += Placed(ps[i], x.roundToInt())
                x += ps[i].width
                if (it.f.autoEnd) x += autoShare
                if (i < ps.size - 1) x += between
            }
            contentW = max(contentW, x.roundToInt())
            placedLines += placed to (ps.maxOfOrNull { it.height } ?: 0)
        }
        val w = if (constraints.hasBoundedWidth) constraints.maxWidth else contentW.coerceAtLeast(constraints.minWidth)
        val h = (placedLines.sumOf { it.second } + rg * (placedLines.size - 1).coerceAtLeast(0))
            .coerceIn(constraints.minHeight, constraints.maxHeight)
        return layout(w, h) {
            var y = 0
            placedLines.forEachIndexed { li, (placed, lh) ->
                lines[li].items.forEachIndexed { i, it ->
                    val p = placed[i]
                    val dy = when (it.f.alignSelf ?: align) {
                        Align.Start, Align.Stretch -> 0
                        Align.Center -> (lh - p.p.height) / 2
                        Align.End -> lh - p.p.height
                    }
                    p.p.place(p.x, y + dy)
                }
                y += lh + rg
            }
        }
    }

    private fun IntrinsicMeasureScope.heightFor(ms: List<IntrinsicMeasurable>, width: Int): Int {
        val items = items(ms)
        val w = if (width == Constraints.Infinity) Float.POSITIVE_INFINITY else width.toFloat()
        val lines = lines(items, w)
        var h = 0
        lines.forEach { line ->
            resolve(line, w)
            h += line.items.maxOf { it.m.maxIntrinsicHeight(it.size.roundToInt().coerceAtLeast(0)) }
        }
        return h + rowGap.roundToPx() * (lines.size - 1).coerceAtLeast(0)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int) =
        if (measurables.isEmpty()) 0 else heightFor(measurables, width)

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int) =
        if (measurables.isEmpty()) 0 else heightFor(measurables, width)

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int {
        val items = items(measurables)
        return (items.sumOf { it.hypo.toDouble() } + gap.toPx() * (items.size - 1).coerceAtLeast(0)).roundToInt()
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int =
        if (!wrap) maxIntrinsicWidth(measurables, height)
        else measurables.maxOfOrNull { m ->
            val f = (m.parentData as? Flex) ?: Flex()
            f.min?.roundToPx() ?: m.minIntrinsicWidth(height)
        } ?: 0
}
