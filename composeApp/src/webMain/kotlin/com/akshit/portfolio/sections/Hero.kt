package com.akshit.portfolio.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.bit.Bit
import com.akshit.portfolio.bit.Pose
import com.akshit.portfolio.data.BUBBLES
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.ui.Circle
import com.akshit.portfolio.ui.Corner
import com.akshit.portfolio.ui.CssGrid
import com.akshit.portfolio.ui.CssShape
import com.akshit.portfolio.ui.Fg
import com.akshit.portfolio.ui.FlexWrap
import com.akshit.portfolio.ui.Len
import com.akshit.portfolio.ui.MorphPill
import com.akshit.portfolio.ui.Pill
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.bounce
import com.akshit.portfolio.ui.cornerPadding
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.flex
import com.akshit.portfolio.ui.hoverLift
import com.akshit.portfolio.ui.margin
import com.akshit.portfolio.ui.outline
import com.akshit.portfolio.ui.rememberBounce
import com.akshit.portfolio.ui.tap

/** `border-radius: 44% 56% 52% 48% / 50% 44% 56% 50%` */
private val Blob = CssShape(
    Corner(Len.Pct(44f), Len.Pct(50f)),
    Corner(Len.Pct(56f), Len.Pct(44f)),
    Corner(Len.Pct(52f), Len.Pct(56f)),
    Corner(Len.Pct(48f), Len.Pct(50f)),
)

/** A tappable Bit wrapper: jump + toast. */
@Composable
fun BitTap(pose: Pose, size: Int, modifier: Modifier = Modifier, bg: Color? = null, shape: Shape = css(0.dp), pad: Dp = 0.dp) {
    val app = LocalApp.current
    val motion = LocalMotion.current
    val b = rememberBounce()
    Box(
        modifier
            .bounce(b)
            .tap(shape, label = "Poke Bit", clip = false) { b.play(true, motion.reduce); app.botTap() }
            .let { if (bg != null) it.background(bg, shape) else it }
            .padding(pad),
        contentAlignment = Alignment.Center,
    ) { Bit(if (app.dev) Pose.Dev else pose, size) }
}

@Composable
fun Hero() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val m = f.compact

    com.akshit.portfolio.ui.Section(top = f.c(20, 2.5, 32)) {
        Column(verticalArrangement = Arrangement.spacedBy(f.c(28, 3, 44))) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(f.c(16, 1.6, 24))) {
                    // Label row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Box(Modifier.weight(1f)) {
                            com.akshit.portfolio.ui.SectionLabel("BOOT_COMPLETE", "(took one coffee)")
                        }
                        if (m) {
                            BitTap(
                                Pose.Idle, 64,
                                Modifier.margin(bottom = (-30).dp).size(96.dp),
                                bg = p.pc, shape = css(34.dp),
                            )
                        }
                    }
                    // Name
                    T(
                        buildAnnotatedString {
                            append("AKSHIT\n")
                            withStyle(SpanStyle(color = p.p)) { append("NAHATA") }
                        },
                        fonts.display(f.sp(76, 11.2, 164), .84f, -.05f),
                    )
                    // Chips
                    val chip = fonts.body(f.sp(15, 1.2, 17), 600)
                    FlexWrap(gap = 10.dp) {
                        Row(
                            Modifier.background(p.sc2, Pill).padding(vertical = 10.dp, horizontal = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // 10dp dot with a 4dp primaryContainer halo (box-shadow spread)
                            Box(Modifier.size(10.dp).wrapContentSize(unbounded = true)) {
                                Box(Modifier.size(18.dp).background(p.pc, Circle), contentAlignment = Alignment.Center) {
                                    Box(Modifier.size(10.dp).background(p.p, Circle))
                                }
                            }
                            T("Android Engineer at Jaguar Land Rover", chip)
                        }
                        Box(Modifier.outline(p.ol, Pill).padding(vertical = 10.dp, horizontal = 18.dp)) {
                            T("Manchester, UK", chip.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.W500), color = p.osv)
                        }
                    }
                    // Tagline
                    T(
                        "I build Android apps that feel effortless on the outside and are beautifully engineered on the inside.",
                        fonts.body(f.sp(19, 1.8, 26), 500, 1.38f),
                        Modifier.widthIn(max = 660.dp),
                        color = p.osv,
                    )
                    // CTAs
                    FlexWrap(gap = 12.dp) {
                        HeroCta("See my work →", p.p, p.op, Modifier.flex(grow = 1f, max = 260.dp)) { app.go("work") }
                        HeroCta("Get in touch", p.sec, p.osec, Modifier.flex(grow = 1f, max = 260.dp)) { app.go("contact") }
                    }
                }
                if (!m) Stage(f.c(380, 34, 480))
            }
            StatTiles()
        }
    }
}

@Composable
private fun HeroCta(text: String, bg: Color, fg: Color, modifier: Modifier, onClick: () -> Unit) {
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val src = remember { MutableInteractionSource() }
    val hovered by src.collectIsHoveredAsState()
    val t by animateFloatAsState(if (hovered) 1f else 0f, motion.expressive())
    val shape = MorphPill(t, 22.dp)
    val sc = if (motion.moves) 1f + .04f * t else 1f
    Box(
        modifier
            .graphicsLayer { scaleX = sc; scaleY = sc }
            .height(f.c(56, 4.4, 64))
            .tap(shape, fg, src, onClick = onClick)
            .background(bg, shape)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center,
    ) { T(text, fonts.body(18.sp, 700), color = fg, maxLines = 1) }
}

@Composable
private fun Stage(size: Dp) {
    val app = LocalApp.current
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    BoxWithConstraints(Modifier.size(size)) {
        val w = maxWidth
        // Blob: inset 4% 2% 6% 6%
        Box(
            Modifier
                .offset(w * .06f, w * .04f)
                .size(w * .92f, w * .90f)
                .background(p.pc, Blob),
        )
        // Rotated square: right 4%, top 2%, width 22%
        Box(
            Modifier
                .offset(w * (1f - .04f - .22f), w * .02f)
                .size(w * .22f)
                .rotate(14f)
                .background(p.tc, css(36.dp)),
        )
        // Circle: left 2%, bottom 6%, width 14%
        Box(
            Modifier
                .offset(w * .02f, w * (1f - .06f - .14f))
                .size(w * .14f)
                .background(p.t, Circle),
        )
        // Bit, centred
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { BitTap(Pose.Idle, 250) }
        // Bubble: left 2%, top 3%, shadow 0 2 0 sc3
        val bubbleShape = css(20.dp, 20.dp, 20.dp, 6.dp)
        Box(
            Modifier
                .offset(w * .02f, w * .03f)
                .drawSolidShadow(bubbleShape, p.sc3, 2.dp)
                .background(p.s, bubbleShape)
                .padding(vertical = 10.dp, horizontal = 16.dp),
        ) { T(BUBBLES[app.bubble], fonts.mono(13.sp), color = p.os, maxLines = 1) }
    }
}

/** `box-shadow: 0 y 0 color` (a hard offset shadow). */
fun Modifier.drawSolidShadow(shape: Shape, color: Color, y: Dp): Modifier = this.drawBehind {
    val o = shape.createOutline(size, layoutDirection, this)
    translate(0f, y.toPx()) { drawOutline(o, color) }
}

private class TileSpec(
    val label: String,
    val value: String,
    val note: String,
    val bg: Color,
    val fg: Color?,
    val shape: CssShape,
    val rot: Float,
    val big: Boolean,
    val padStart: Dp? = null,
    val onTap: (() -> Unit)? = null,
    val ripple: Boolean = false,
    val squash: Boolean = false,
)

@Composable
private fun StatTiles() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val tiles = listOf(
        TileSpec("TARGET_SDK", app.sdk.toString(), "Always the latest.", p.sc2, null, css(28.dp), -1f, true, onTap = { app.count() }),
        TileSpec("CORE_STACK", "Kotlin", "Fluent. No accent.", p.pc, p.opc, css(999.dp, 28.dp, 28.dp, 999.dp), 1f, false, padStart = f.c(22, 2.6, 38)),
        TileSpec("UI_ENGINE", "Compose", "Zero XML harmed.", p.tc, p.otc, css(28.dp, 28.dp, 64.dp, 28.dp), -1f, false),
        TileSpec("COFFEE_PER_RELEASE", app.coffee.toString(), "Tap to refill.", p.inv, p.oinv, css(28.dp), 1f, true, onTap = { app.coffeeTap() }, ripple = true, squash = true),
        TileSpec("APPS_ON_PLAY", "4", "Personal, and counting.", p.sec, p.osec, css(28.dp, 64.dp, 28.dp, 28.dp), -1f, true, onTap = { app.go("more") }),
    )
    CssGrid(minCol = 150.dp, gap = f.c(10, 1.2, 16)) {
        tiles.forEach { StatTile(it) }
    }
}

@Composable
private fun StatTile(t: TileSpec) {
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val src = remember { MutableInteractionSource() }
    val b = rememberBounce()
    val fg = t.fg ?: p.os
    val pad = f.c(16, 1.6, 22)
    val valueSize: TextUnit = if (t.big) f.sp(34, 3.3, 48) else f.sp(30, 3.3, 48)
    Fg(fg) {
        Column(
            Modifier
                .hoverLift(src, dy = -6f, rot = t.rot)
                .bounce(b)
                .let { m ->
                    if (t.onTap != null) m.tap(t.shape, if (t.ripple) fg else null, src) {
                        if (t.squash) b.play(false, motion.reduce)
                        t.onTap()
                    } else m
                }
                .background(t.bg, t.shape)
                .cornerPadding(t.shape, t.padStart ?: pad, pad, pad, pad),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            T(t.label, fonts.mono(11.sp), color = if (t.fg == null) p.osv else fg, maxLines = 1)
            T(t.value, fonts.display(valueSize, 1f, -.03f))
            T(t.note, fonts.body(14.sp), color = if (t.fg == null) p.osv else fg)
        }
    }
}
