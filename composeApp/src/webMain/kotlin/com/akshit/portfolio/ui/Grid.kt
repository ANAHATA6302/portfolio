package com.akshit.portfolio.ui

import androidx.compose.runtime.Composable
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
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private data class Span(val cols: Int, val rows: Int)

private class SpanData(val span: Span) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = span
}

fun Modifier.span(cols: Int = 1, rows: Int = 1): Modifier = this.then(SpanData(Span(cols, rows)))

/**
 * CSS grid with `repeat(auto-fit | auto-fill, minmax(min(100%, [minCol]), 1fr))`, optional fixed
 * row height, `grid-auto-flow: dense` placement and col/row spans. With auto-fit, empty tracks
 * collapse so the items share the full width. Items stretch to their area.
 * [fixedCols] gives `repeat(n, minmax(0, 1fr))`.
 */
@Composable
fun CssGrid(
    modifier: Modifier = Modifier,
    minCol: Dp? = null,
    fixedCols: Int? = null,
    gap: Dp,
    rowHeight: Dp? = null,
    autoFit: Boolean = true,
    content: @Composable () -> Unit,
) {
    val policy = remember(minCol, fixedCols, gap, rowHeight, autoFit) { GridPolicy(minCol, fixedCols, gap, rowHeight, autoFit) }
    Layout(content, modifier, policy)
}

private class GridPolicy(
    val minCol: Dp?,
    val fixedCols: Int?,
    val gap: Dp,
    val rowHeight: Dp?,
    val autoFit: Boolean,
) : MeasurePolicy {

    private class Cell(val col: Int, val row: Int, val cs: Int, val rs: Int)

    private fun IntrinsicMeasureScope.cols(width: Int, n: Int, spans: List<Span>): Int {
        fixedCols?.let { return it }
        val g = gap.toPx()
        val mc = min(minCol!!.toPx(), width.toFloat())
        var c = max(1, floor((width + g) / (mc + g)).toInt())
        if (autoFit) {
            // Collapse empty tracks: only as many columns as the items could fill in one row.
            val needed = spans.sumOf { it.cols }
            c = min(c, max(1, needed))
        }
        return c
    }

    private fun place(spans: List<Span>, cols: Int): List<Cell> {
        val taken = mutableListOf<BooleanArray>()
        fun free(r: Int, c: Int, cs: Int, rs: Int): Boolean {
            for (y in r until r + rs) for (x in c until c + cs) {
                if (y < taken.size && taken[y][x]) return false
            }
            return true
        }
        return spans.map { s ->
            val cs = min(s.cols, cols)
            var r = 0
            var found: Cell? = null
            while (found == null) {
                for (c in 0..cols - cs) {
                    if (free(r, c, cs, s.rows)) { found = Cell(c, r, cs, s.rows); break }
                }
                if (found == null) r++
            }
            val f = found!!
            while (taken.size < f.row + f.rs) taken += BooleanArray(cols)
            for (y in f.row until f.row + f.rs) for (x in f.col until f.col + f.cs) taken[y][x] = true
            f
        }
    }

    private fun IntrinsicMeasureScope.layoutPlan(ms: List<IntrinsicMeasurable>, width: Int): Plan {
        val spans = ms.map { (it.parentData as? Span) ?: Span(1, 1) }
        val cols = cols(width, ms.size, spans)
        val g = gap.toPx()
        val colW = (width - g * (cols - 1)) / cols
        val cells = place(spans, cols)
        val rows = cells.maxOfOrNull { it.row + it.rs } ?: 0
        val rh = FloatArray(rows)
        if (rowHeight != null) rh.fill(rowHeight.toPx())
        else {
            cells.forEachIndexed { i, c ->
                if (c.rs == 1) {
                    val w = (colW * c.cs + g * (c.cs - 1)).roundToInt()
                    rh[c.row] = max(rh[c.row], ms[i].maxIntrinsicHeight(w).toFloat())
                }
            }
        }
        return Plan(cols, colW, g, cells, rh)
    }

    private class Plan(val cols: Int, val colW: Float, val g: Float, val cells: List<Cell>, val rh: FloatArray) {
        fun rowY(r: Int): Float { var y = 0f; for (i in 0 until r) y += rh[i] + g; return y }
        val height get() = if (rh.isEmpty()) 0f else rowY(rh.size) - g
    }

    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val width = constraints.maxWidth
        val plan = layoutPlan(measurables, width)
        val ps = measurables.mapIndexed { i, m ->
            val c = plan.cells[i]
            val w = (plan.colW * c.cs + plan.g * (c.cs - 1)).roundToInt()
            val h = (plan.rowY(c.row + c.rs) - plan.g - plan.rowY(c.row)).roundToInt()
            m.measure(Constraints.fixed(w, h))
        }
        val h = plan.height.roundToInt().coerceIn(constraints.minHeight, constraints.maxHeight)
        return layout(width, h) {
            ps.forEachIndexed { i, p ->
                val c = plan.cells[i]
                p.place(((plan.colW + plan.g) * c.col).roundToInt(), plan.rowY(c.row).roundToInt())
            }
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int) =
        layoutPlan(measurables, width).height.roundToInt()

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int) =
        layoutPlan(measurables, width).height.roundToInt()
}
