package com.akshit.portfolio.overlays

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.bit.Bit
import com.akshit.portfolio.bit.Pose
import com.akshit.portfolio.data.BOOT
import com.akshit.portfolio.data.SEEDS
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.theme.oklch
import com.akshit.portfolio.ui.Align
import com.akshit.portfolio.ui.Circle
import com.akshit.portfolio.ui.CssGrid
import com.akshit.portfolio.ui.FlexWrap
import com.akshit.portfolio.ui.Justify
import com.akshit.portfolio.ui.Pill
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.animatedColor
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.cssShadow
import com.akshit.portfolio.ui.tap
import kotlin.random.Random

/** Swallows clicks so they don't reach the page below. */
@Composable
private fun Modifier.blockClicks(onClick: () -> Unit = {}) =
    this.clickable(remember { MutableInteractionSource() }, null, onClick = onClick)

@Composable
fun Toast() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val y by animateFloatAsState(if (app.toastOn) 0f else 32f, motion.expressive())
    val a by animateFloatAsState(if (app.toastOn) 1f else 0f, tween(250))
    if (a == 0f && !app.toastOn) return
    Box(Modifier.fillMaxSize().padding(bottom = 24.dp), contentAlignment = Alignment.BottomCenter) {
        val shape = css(14.dp)
        Row(
            Modifier
                .graphicsLayer { translationY = y * density; alpha = a }
                .widthIn(max = minOf(560f, f.width - 32f).dp)
                .heightIn(min = 48.dp)
                .cssShadow(shape, 6.dp, 20.dp, Color.Black.copy(alpha = .2f))
                .background(p.inv, shape)
                .padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            T(app.toastText, fonts.body(15.sp, 500), Modifier.weight(1f, fill = false).padding(vertical = 6.dp), color = p.oinv)
            val act = app.toastAction
            if (act != null) {
                Box(
                    Modifier.tap(css(10.dp)) { app.toastActionTap() }.padding(vertical = 10.dp, horizontal = 12.dp),
                ) { T(act, fonts.body(15.sp, 700), color = p.tint(.85f, .13f)) }
            }
        }
    }
}

@Composable
fun QuickSettings() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val scrim by animateFloatAsState(if (app.qsOpen) 1f else 0f, tween(if (motion.reduce) 150 else 300))
    val t by animateFloatAsState(if (app.qsOpen) 1f else 0f, motion.expressive())
    if (!app.qsOpen && t == 0f && scrim == 0f) return

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize().graphicsLayer { alpha = scrim }.background(Color.Black.copy(alpha = .32f))
                .let { if (app.qsOpen) it.blockClicks { app.qsOpen = false } else it },
        )
        var h by remember { mutableStateOf(0) }
        val shape = css(0.dp, 0.dp, 36.dp, 36.dp)
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Column(
                Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .onSizeChanged { h = it.height }
                    .graphicsLayer { translationY = -(h + 60.dp.toPx()) * (1f - t) }
                    .cssShadow(shape, 12.dp, 40.dp, Color.Black.copy(alpha = .18f))
                    .background(p.sc1, shape)
                    .blockClicks()
                    .padding(f.c(18, 2, 28)),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)) {
                    T("9:41", fonts.display(f.sp(32, 3, 40), 1f, -.03f), Modifier.alignByBaseline(), color = p.os)
                    Box(Modifier.weight(1f))
                    T("QUICK_SETTINGS // no root required", fonts.mono(12.sp), Modifier.alignByBaseline(), color = p.osv)
                }
                CssGrid(fixedCols = 2, gap = 10.dp) {
                    QsTile("Dark theme", if (app.dark) "On" else "Off", app.dark) { app.toggleDark() }
                    QsTile("Whimsy mode", if (app.whimsy) "On" else "Off", app.whimsy) { app.toggleWhimsy() }
                    QsTile("Reduce motion", if (app.reduceMotion) "On" else "Off", app.reduceMotion) { app.toggleMotion() }
                    QsTile("Developer options", if (app.dev) "Unlocked" else "Locked", app.dev) { app.devTile() }
                }
                FlexWrap(
                    Modifier.padding(start = 6.dp, end = 6.dp, top = 8.dp, bottom = 2.dp),
                    gap = 12.dp, justify = Justify.SpaceBetween, align = Align.Center,
                ) {
                    T(
                        buildAnnotatedString {
                            append("Wallpaper colour ")
                            withStyle(SpanStyle(color = p.osv, fontWeight = FontWeight.W500)) {
                                append(SEEDS.firstOrNull { it.hue == app.seed }?.name ?: "")
                            }
                        },
                        fonts.body(15.sp, 600), color = p.os,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SEEDS.forEach { s -> Swatch(s.name, s.hue, app.seed == s.hue) { app.pickSeed(s.hue) } }
                    }
                }
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(Modifier.size(40.dp, 4.dp).background(p.ol, css(2.dp)))
                }
            }
        }
    }
}

@Composable
private fun QsTile(title: String, sub: String, on: Boolean, onClick: () -> Unit) {
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    val bg = animatedColor(if (on) p.p else p.sc3)
    val fg = animatedColor(if (on) p.op else p.os)
    Column(
        Modifier
            .fillMaxHeight()
            .heightIn(min = 64.dp)
            .tap(Pill, fg, label = title, onClick = onClick)
            .background(bg, Pill)
            .padding(vertical = 14.dp, horizontal = 20.dp),
    ) {
        T(title, fonts.body(15.sp, 700), color = fg)
        T(sub, fonts.body(13.sp), color = fg.copy(alpha = fg.alpha * .85f))
    }
}

@Composable
private fun Swatch(name: String, hue: Float, selected: Boolean, onClick: () -> Unit) {
    val p = LocalPalette.current
    val ring by animateFloatAsState(if (selected) 1f else 0f, tween(200))
    Box(
        Modifier
            .size(44.dp)
            .drawBehind {
                if (ring > 0f) {
                    val c = Offset(size.width / 2, size.height / 2)
                    val r = size.minDimension / 2
                    drawCircle(p.os.copy(alpha = ring), r + 6.dp.toPx(), c)
                    drawCircle(p.sc1.copy(alpha = ring), r + 3.dp.toPx(), c)
                }
            }
            .tap(Circle, label = name, onClick = onClick)
            .background(oklch(.64f, .19f, hue), Circle),
    )
}

private class Particle(
    val x: Float, val w: Float, val h: Float, val round: Boolean, val color: Int,
    val dx: Float, val rot: Float, val dur: Float, val delay: Float,
)

private val ConfettiEase = CubicBezierEasing(.25f, .6f, .4f, 1f)

@Composable
fun Confetti() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    var parts by remember { mutableStateOf<List<Particle>>(emptyList()) }
    var t by remember { mutableStateOf(0f) }
    LaunchedEffect(app.confetti) {
        if (app.confetti == 0) return@LaunchedEffect
        parts = List(90) { i ->
            val w = 6f + Random.nextFloat() * 8f
            Particle(
                Random.nextFloat() * f.width, w, w * (if (Random.nextFloat() > .5f) 1f else 2.2f), Random.nextFloat() > .5f,
                i % 6, (Random.nextFloat() - .5f) * 200f, Random.nextFloat() * 720f,
                1400f + Random.nextFloat() * 1400f, Random.nextFloat() * 300f,
            )
        }
        val t0 = withFrameMillis { it }
        while (true) {
            val now = withFrameMillis { it }
            t = (now - t0).toFloat()
            if (t > 3100f) break
        }
        parts = emptyList()
    }
    if (parts.isEmpty()) return
    val cols = listOf(p.p, p.t, p.tc, p.pc, p.sec, p.inv)
    Canvas(Modifier.fillMaxSize()) {
        val d = density
        val fall = f.height + 40f
        parts.forEach { q ->
            val raw = ((t - q.delay) / q.dur).coerceIn(0f, 1f)
            if (raw >= 1f) return@forEach
            val e = ConfettiEase.transform(raw)
            val cx = (q.x + q.dx * e + q.w / 2f) * d
            val cy = (-20f + fall * e + q.h / 2f) * d
            rotate(q.rot * e, Offset(cx, cy)) {
                val tl = Offset(cx - q.w * d / 2f, cy - q.h * d / 2f)
                val sz = Size(q.w * d, q.h * d)
                val r = if (q.round) CornerRadius(sz.width / 2f, sz.height / 2f) else CornerRadius(3f * d)
                drawRoundRect(cols[q.color], tl, sz, r)
            }
        }
    }
}

@Composable
fun BootIntro() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    Box(Modifier.fillMaxSize().background(p.s).blockClicks()) {
        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(28.dp)) {
                Box(Modifier.size(180.dp).background(p.pc, css(64.dp)), contentAlignment = Alignment.Center) {
                    Bit(Pose.Loading, 110)
                }
                Column(Modifier.widthIn(max = 320.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BOOT.take(app.bootLines).forEach { T(it, fonts.mono(13.sp, 500, 1.5f), color = p.osv) }
                    Box(Modifier.padding(top = 10.dp).fillMaxWidth().height(8.dp).background(p.sc3, css(4.dp))) {
                        Box(Modifier.fillMaxWidth(app.bootProgress).height(8.dp).background(p.p, css(4.dp)))
                    }
                }
            }
        }
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp, end = 20.dp)
                .tap(Pill, label = "Skip intro") { app.skipBoot() }
                .background(p.sc2, Pill)
                .padding(vertical = 12.dp, horizontal = 20.dp),
        ) { T("Skip", fonts.body(16.sp, 700), color = p.os) }
    }
}

@Composable
fun AnrDialog() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    Box(
        Modifier.fillMaxSize().background(Color.Black.copy(alpha = .42f)).blockClicks().padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        val shape = css(28.dp)
        // width: min(440px, 100%) is the content box; the 24dp padding sits outside it.
        Column(
            Modifier
                .widthIn(max = 488.dp)
                .fillMaxWidth()
                .cssShadow(shape, 20.dp, 60.dp, Color.Black.copy(alpha = .3f))
                .background(p.sc2, shape)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(Modifier.align(Alignment.CenterHorizontally).size(120.dp).background(p.pc, css(44.dp)), contentAlignment = Alignment.Center) {
                Bit(Pose.Anr, 74)
            }
            T(
                "ERROR 404 // PAGE_NOT_FOUND", fonts.mono(12.sp).copy(textAlign = TextAlign.Center),
                Modifier.fillMaxWidth(), color = p.osv,
            )
            T("Akshit's Portfolio isn't responding", fonts.display(28.sp, 1.1f, -.02f), color = p.os)
            T(
                "This page doesn't exist. Bit checked everywhere, even behind the sofa.",
                fonts.body(16.sp, 400, 1.5f), color = p.osv,
            )
            Row(
                Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            ) {
                Box(Modifier.tap(Pill, p.p) { app.anrWait() }.padding(vertical = 12.dp, horizontal = 18.dp)) {
                    T("Wait", fonts.body(15.sp, 700), color = p.p)
                }
                Box(Modifier.tap(Pill, p.op) { app.anrClose() }.background(p.p, Pill).padding(vertical = 12.dp, horizontal = 22.dp)) {
                    T("Close app", fonts.body(15.sp, 700), color = p.op)
                }
            }
        }
    }
}
