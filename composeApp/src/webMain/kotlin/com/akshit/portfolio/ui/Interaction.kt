package com.akshit.portfolio.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.akshit.portfolio.state.LocalApp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.Modifier.Node
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.akshit.portfolio.theme.EmphasizedEasing
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * The reference ripple: a circle at the press point, 2.2x the longest side, scale 0 to 1 and
 * alpha .24 to 0 over 650ms (300ms with reduce motion), in the content colour.
 * Drawn over the content; the caller clips to the shape.
 */
class CssRipple(private val color: ColorProducer, private val reduce: Boolean) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        RippleNode(interactionSource, color, reduce)

    override fun equals(other: Any?) = other is CssRipple && other.color == color && other.reduce == reduce
    override fun hashCode() = color.hashCode() * 31 + reduce.hashCode()
}

private class RippleNode(
    private val source: InteractionSource,
    private val color: ColorProducer,
    private val reduce: Boolean,
) : Node(), DrawModifierNode {
    private class Wave(val at: Offset, val t: Animatable<Float, *>)

    private val waves = mutableListOf<Wave>()

    override fun onAttach() {
        coroutineScope.launch {
            source.interactions.collect { i ->
                if (i is PressInteraction.Press) {
                    val w = Wave(i.pressPosition, Animatable(0f))
                    waves += w
                    coroutineScope.launch {
                        w.t.animateTo(1f, tween(if (reduce) 300 else 650, easing = EmphasizedEasing)) { invalidateDraw() }
                        waves -= w
                        invalidateDraw()
                    }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (waves.isEmpty()) return
        val c = color()
        val dia = max(size.width, size.height) * 2.2f
        for (w in waves) {
            val p = w.t.value
            drawCircle(c.copy(alpha = .24f * (1f - p)), radius = dia / 2f * p, center = w.at)
        }
    }
}

/** A 3dp primary ring shown while the element has keyboard focus. */
fun Modifier.focusRing(focused: State<Boolean>, shape: Shape, color: Color): Modifier = drawWithContent {
    drawContent()
    if (focused.value) {
        val o = shape.createOutline(size, layoutDirection, this)
        drawOutline(o, color, style = Stroke(3.dp.toPx()))
    }
}

/**
 * A clickable element: hand cursor, focus ring, optional ripple, clipped to [shape].
 * Pass an [interaction] source to drive hover effects from the outside.
 */
@Composable
fun Modifier.tap(
    shape: Shape,
    rippleColor: Color? = null,
    interaction: MutableInteractionSource? = null,
    role: Role = Role.Button,
    label: String? = null,
    clip: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val src = interaction ?: remember { MutableInteractionSource() }
    val isFocused by src.collectIsFocusedAsState()
    val app = LocalApp.current
    val focused = rememberUpdatedState(isFocused && app.keyboardNav)
    val p = LocalPalette.current
    val reduce = LocalMotion.current.reduce
    val ripple = rippleColor?.let { c -> remember(c, reduce) { CssRipple({ c }, reduce) } }
    return this
        .focusRing(focused, shape, p.p)
        .let { if (clip) it.clip(shape) else it }
        .pointerHoverIcon(PointerIcon.Hand)
        .clickable(src, ripple, role = role, onClickLabel = label, onClick = onClick)
}

/** `a:hover { opacity: .85 }` */
@Composable
fun Modifier.linkHover(src: MutableInteractionSource): Modifier {
    val hovered by src.collectIsHoveredAsState()
    return this.hoverable(src).alpha(if (hovered) .85f else 1f)
}
