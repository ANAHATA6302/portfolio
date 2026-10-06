package com.akshit.portfolio.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.platform.Browser
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.MAX_CONTENT
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.mono
import kotlin.math.min
import kotlin.math.roundToInt

/** CSS `color` inheritance (currentColor). */
val LocalFg = compositionLocalOf { Color.Black }

@Composable
fun Fg(color: Color, content: @Composable () -> Unit) =
    CompositionLocalProvider(LocalFg provides color, content = content)

/** Text in the inherited colour unless [color] is given. */
@Composable
fun T(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    ellipsis: Boolean = false,
) = T(AnnotatedString(text), style, modifier, color, maxLines, ellipsis)

@Composable
fun T(
    text: AnnotatedString,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    ellipsis: Boolean = false,
) {
    val c = if (color.isSpecified) color else LocalFg.current
    val lines = remember { IntArray(1) }
    BasicText(
        text, modifier.cssText(style, lines, ellipsis), style.copy(color = c),
        onTextLayout = { lines[0] = it.lineCount },
        overflow = if (ellipsis) TextOverflow.Ellipsis else TextOverflow.Clip,
        maxLines = maxLines,
    )
}

/**
 * CSS text box behaviour Compose doesn't have:
 * - Line boxes for line heights below the font's natural height. Compose web keeps the first
 *   line's natural top and the last line's natural bottom, so the box is too tall by
 *   (natural - lineHeight). CSS centres the glyphs in the smaller line box and lets them
 *   overflow, so trim half the excess from the top and half from the bottom.
 * - `overflow-wrap: normal`: a word wider than the box overflows instead of breaking.
 */
private fun Modifier.cssText(style: TextStyle, lines: IntArray, ellipsis: Boolean): Modifier = layout { m, c ->
    val longest = if (!ellipsis && c.hasBoundedWidth) m.minIntrinsicWidth(Constraints.Infinity) else 0
    val p = m.measure(if (longest > c.maxWidth) c.copy(maxWidth = longest) else c)
    val lh = style.lineHeight
    val fs = style.fontSize
    val t = if (lh.isEm && fs.isSp) {
        val box = lh.value * fs.toPx() * lines[0].coerceAtLeast(1)
        ((p.height - box).coerceAtLeast(0f) / 2f).roundToInt()
    } else 0
    val w = p.width.coerceIn(c.minWidth, c.maxWidth)
    val h = (p.height - 2 * t).coerceIn(c.minHeight, c.maxHeight)
    layout(w, h) { p.place(0, -t) }
}

/**
 * `<section style="max-width:1328px;margin:0 auto;padding:top side bottom">`: the 1328 max is the
 * content box, so the side padding sits outside it.
 */
@Composable
fun Section(
    top: Dp,
    bottom: Dp = 0.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val f = LocalFluid.current
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Box(
            modifier
                .widthIn(max = MAX_CONTENT.dp + f.side * 2)
                .fillMaxWidth()
                .padding(start = f.side, end = f.side, top = top, bottom = bottom),
        ) { content() }
    }
}

/** The mono section label: `NAME (aside)`, primary with the aside in onSurfaceVariant. */
@Composable
fun SectionLabel(main: String, aside: String? = null, color: Color? = null) {
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    val f = LocalFluid.current
    val text = buildAnnotatedString {
        append(main)
        if (aside != null) {
            append(" ")
            withStyle(SpanStyle(color = p.osv)) { append(aside) }
        }
    }
    T(text, fonts.mono(f.sp(12, 1, 14)), color = color ?: p.p)
}

/** A CSS box: background + shape + padding. */
fun Modifier.box(bg: Color, shape: Shape, padding: PaddingValues = PaddingValues(0.dp)): Modifier =
    this.background(bg, shape).padding(padding)

fun Modifier.outline(color: Color, shape: Shape, width: Dp = 1.5.dp): Modifier = this.border(width, color, shape)

/** `box-shadow: x y blur spread color` for floating elements. */
fun Modifier.cssShadow(shape: Shape, y: Dp, blur: Dp, color: Color, spread: Dp = 0.dp): Modifier =
    this.dropShadow(shape, Shadow(radius = blur, spread = spread, color = color, offset = DpOffset(0.dp, y)))

/** Negative CSS margin: lays out as if [top]/[bottom] were taken away (may be negative). */
fun Modifier.margin(top: Dp = 0.dp, bottom: Dp = 0.dp): Modifier = layout { m, c ->
    val p = m.measure(c)
    val t = top.roundToPx()
    val b = bottom.roundToPx()
    layout(p.width, (p.height + t + b).coerceAtLeast(0)) { p.place(0, t) }
}

/**
 * Pill whose corners morph from full to [to] (hover on the hero CTAs): `border-radius: 999px` ->
 * `22px` with the expressive spring.
 */
class MorphPill(private val f: Float, private val to: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val full = min(size.width, size.height) / 2f
        val end = with(density) { to.toPx() }.coerceAtMost(full)
        val r = full + (end - full) * f
        return Outline.Rounded(RoundRect(0f, 0f, size.width, size.height, CornerRadius(r)))
    }

    override fun equals(other: Any?) = other is MorphPill && other.f == f && other.to == to
    override fun hashCode() = f.hashCode() * 31 + to.hashCode()
}

/** A pill button or link: bg, fg, height, horizontal padding, Figtree 700, ripple. */
@Composable
fun PillButton(
    text: String,
    bg: Color,
    fg: Color,
    height: Dp = 56.dp,
    padX: Dp = 28.dp,
    size: TextUnit = 17.sp,
    modifier: Modifier = Modifier,
    border: Color? = null,
    url: String? = null,
    onClick: () -> Unit = {},
) {
    val fonts = LocalFonts.current
    val src = remember { MutableInteractionSource() }
    val m = if (url != null) Modifier.linkHover(src) else Modifier
    Box(
        modifier
            .then(m)
            .height(height)
            .tap(Pill, fg, src) { if (url != null) Browser.open(url) else onClick() }
            .background(bg, Pill)
            .let { if (border != null) it.outline(border, Pill) else it }
            .padding(horizontal = padX),
        contentAlignment = Alignment.Center,
    ) {
        T(text, fonts.body(size, 700), color = fg, maxLines = 1)
    }
}

/** A chip: padding 8x14, radius full. */
@Composable
fun Chip(
    text: String,
    style: TextStyle,
    bg: Color = Color.Transparent,
    fg: Color = Color.Unspecified,
    border: Color? = null,
    padV: Dp = 8.dp,
    padH: Dp = 14.dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .background(bg, Pill)
            .let { if (border != null) it.outline(border, Pill) else it }
            .padding(vertical = padV, horizontal = padH),
    ) { T(text, style, color = fg, maxLines = 1) }
}

/** Hover-driven background colour (nav items). */
@Composable
fun hoverColor(src: MutableInteractionSource, normal: Color, hover: Color): Color {
    val hovered by src.collectIsHoveredAsState()
    return if (hovered) hover else normal
}

@Composable
fun animatedColor(target: Color, ms: Int = 300): Color {
    val motion = LocalMotion.current
    val c by animateColorAsState(target, tween(if (motion.reduce) 150 else ms))
    return c
}

@Composable
fun animatedFloat(target: Float, spec: androidx.compose.animation.core.AnimationSpec<Float>): Float {
    val v by animateFloatAsState(target, spec)
    return v
}

@Composable
fun ColumnGap(gap: Dp, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) =
    Column(modifier, verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(gap), content = content)

@Composable
fun Stack(modifier: Modifier = Modifier, align: Alignment = Alignment.TopStart, content: @Composable BoxScope.() -> Unit) =
    Box(modifier, contentAlignment = align, content = content)

/** A Modifier that only applies when hovered, for consumers that need the hoverable too. */
fun Modifier.hover(src: MutableInteractionSource): Modifier = this.hoverable(src)
