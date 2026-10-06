package com.akshit.portfolio.sections

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.data.WIDGETS
import com.akshit.portfolio.data.Widget
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.Palette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.ui.Align
import com.akshit.portfolio.ui.CssGrid
import com.akshit.portfolio.ui.CssShape
import com.akshit.portfolio.ui.Fg
import com.akshit.portfolio.ui.FlexWrap
import com.akshit.portfolio.ui.Justify
import com.akshit.portfolio.ui.PillButton
import com.akshit.portfolio.ui.Section
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.bounce
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.hoverLift
import com.akshit.portfolio.ui.rememberBounce
import com.akshit.portfolio.ui.reveal
import com.akshit.portfolio.ui.span
import com.akshit.portfolio.ui.tap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private class Look(val bg: Color, val fg: Color, val shape: CssShape, val fs: TextUnit)

@Composable
private fun look(w: Widget, p: Palette): Look {
    val f = LocalFluid.current
    val mid = f.sp(24, 2.2, 32)
    return when (w.id) {
        "sec" -> Look(p.ink, p.tint(.95f, .02f), css(40.dp), f.sp(30, 3.4, 46))
        "arch" -> Look(p.pc, p.opc, css(36.dp), mid)
        "flow" -> Look(p.tc, p.otc, css(32.dp, 32.dp, 64.dp, 32.dp), 22.sp)
        "di" -> Look(p.sec, p.osec, css(80.dp, 80.dp, 32.dp, 32.dp), 22.sp)
        "kmp" -> Look(p.sc2, p.os, css(999.dp, 36.dp, 36.dp, 999.dp), mid)
        "ktor" -> Look(p.sc3, p.os, css(32.dp), 26.sp)
        "fb" -> Look(p.pc, p.opc, css(32.dp, 64.dp, 32.dp, 32.dp), 24.sp)
        "test" -> Look(p.inv, p.oinv, css(36.dp), mid)
        else -> Look(p.p, p.op, css(36.dp, 36.dp, 80.dp, 36.dp), mid)
    }
}

@Composable
fun HowIBuild() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val scope = rememberCoroutineScope()
    val press = remember { arrayOfNulls<Job>(1) }

    Section(top = f.sectionTop, modifier = Modifier.anchor(app, "build")) {
        Column(verticalArrangement = Arrangement.spacedBy(f.c(20, 2.4, 32))) {
            FlexWrap(Modifier.reveal(), gap = 16.dp, justify = Justify.SpaceBetween, align = Align.End) {
                SectionHeader(
                    "HOW_I_BUILD", "(long press to rearrange)", "How I build",
                    "The boring bits, done properly, so the fun bits can ship.", 560.dp,
                )
                if (app.editMode) {
                    PillButton("Done", p.p, p.op, height = 52.dp, padX = 26.dp, size = 16.sp) { app.doneEditing() }
                }
            }
            CssGrid(
                Modifier.pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val e = awaitPointerEvent(PointerEventPass.Initial)
                            when (e.type) {
                                PointerEventType.Press -> {
                                    press[0]?.cancel()
                                    press[0] = scope.launch { delay(480); app.longPress() }
                                }
                                PointerEventType.Release, PointerEventType.Exit -> press[0]?.cancel()
                            }
                        }
                    }
                },
                minCol = 150.dp,
                gap = f.c(10, 1.2, 16),
                rowHeight = f.c(150, 12, 176),
                autoFit = false,
            ) {
                app.order.forEachIndexed { i, id ->
                    key(id) {
                        val w = WIDGETS.first { it.id == id }
                        WidgetTile(w, i, Modifier.span(if (f.compact) minOf(w.c, 2) else w.c, w.r))
                    }
                }
            }
        }
    }
}

private val EaseInOut = CubicBezierEasing(.42f, 0f, .58f, 1f)

@Composable
private fun WidgetTile(w: Widget, index: Int, modifier: Modifier) {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val l = look(w, p)
    val src = remember { MutableInteractionSource() }
    val b = rememberBounce()
    val wiggling = app.editMode && motion.playful
    val rot = if (wiggling) {
        val t = rememberInfiniteTransition()
        t.animateFloat(
            -1.4f, 1.4f,
            infiniteRepeatable(tween(180 + (index % 3) * 30, easing = EaseInOut), RepeatMode.Reverse),
        ).value
    } else 0f
    val selected = app.selected == w.id
    val density = LocalDensity.current
    Fg(l.fg) {
        BoxWithConstraints(
            modifier
                .graphicsLayer { rotationZ = rot }
                .hoverLift(src, scale = 1.025f)
                .bounce(b)
                .ring(selected, l.shape, p.s, p.p)
                .tap(l.shape, null, src) {
                    if (!app.widgetTap(w.id)) b.play(false, motion.reduce)
                }
                .background(l.bg, l.shape),
        ) {
            // Keep text clear of big curved corners: the label row follows the top corners,
            // the title and description follow the bottom ones.
            val pad = f.c(16, 1.6, 22)
            val rr = l.shape.roundRect(with(density) { Size(maxWidth.toPx(), maxHeight.toPx()) }, density)
            fun side(r: Float) = maxOf(pad, with(density) { (r * .5f).toDp() })
            Column(
                Modifier.fillMaxSize().padding(vertical = pad),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                T(
                    w.label, fonts.mono(11.sp),
                    Modifier.padding(start = side(rr.topLeftCornerRadius.x), end = side(rr.topRightCornerRadius.x)),
                    color = l.fg.copy(alpha = .85f), maxLines = 1,
                )
                Column(Modifier.padding(start = side(rr.bottomLeftCornerRadius.x), end = side(rr.bottomRightCornerRadius.x))) {
                    T(w.title, fonts.display(l.fs, 1.02f, -.02f))
                    T(w.sub, fonts.body(14.sp, 400, 1.4f), Modifier.padding(top = 6.dp), color = l.fg.copy(alpha = .88f))
                }
            }
        }
    }
}

/** `box-shadow: 0 0 0 4px gap, 0 0 0 7px ring` */
private fun Modifier.ring(on: Boolean, shape: Shape, gap: Color, ring: Color): Modifier = drawBehind {
    if (!on) return@drawBehind
    fun spread(d: Dp, c: Color) {
        val s = d.toPx()
        val o = shape.createOutline(Size(size.width + 2 * s, size.height + 2 * s), layoutDirection, this)
        translate(-s, -s) { drawOutline(o, c) }
    }
    spread(7.dp, ring)
    spread(4.dp, gap)
}
