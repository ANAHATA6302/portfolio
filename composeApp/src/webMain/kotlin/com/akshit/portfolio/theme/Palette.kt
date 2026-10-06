package com.akshit.portfolio.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/** Every role has a fixed OKLCH lightness and chroma, only the hue comes from the seed. */
@Immutable
data class Roles(
    val hue: Float,
    val p: Color, val op: Color, val pc: Color, val opc: Color,
    val s: Color, val sc1: Color, val sc2: Color, val sc3: Color,
    val os: Color, val osv: Color, val ol: Color,
    val tc: Color, val otc: Color, val sec: Color, val osec: Color,
    val inv: Color, val oinv: Color, val t: Color,
)

fun roles(h: Float, dark: Boolean): Roles {
    val t = (h + 70f) % 360f
    fun o(l: Float, c: Float, x: Float = h) = oklch(l, c, x)
    return if (dark) Roles(
        hue = h,
        p = o(.82f, .13f), op = o(.26f, .09f), pc = o(.38f, .12f), opc = o(.93f, .06f),
        s = o(.165f, .012f), sc1 = o(.2f, .015f), sc2 = o(.235f, .02f), sc3 = o(.28f, .025f),
        os = o(.93f, .012f), osv = o(.78f, .025f), ol = o(.5f, .03f),
        tc = o(.38f, .1f, t), otc = o(.93f, .06f, t), sec = o(.3f, .04f), osec = o(.92f, .03f),
        inv = o(.92f, .012f), oinv = o(.22f, .02f), t = o(.8f, .12f, t),
    ) else Roles(
        hue = h,
        p = o(.5f, .19f), op = o(.99f, .01f), pc = o(.91f, .09f), opc = o(.27f, .09f),
        s = o(.985f, .008f), sc1 = o(.96f, .014f), sc2 = o(.935f, .02f), sc3 = o(.9f, .03f),
        os = o(.21f, .02f), osv = o(.43f, .03f), ol = o(.72f, .03f),
        tc = o(.9f, .08f, t), otc = o(.28f, .09f, t), sec = o(.92f, .04f), osec = o(.28f, .05f),
        inv = o(.27f, .02f), oinv = o(.95f, .01f), t = o(.52f, .16f, t),
    )
}

/**
 * The live palette. During a re-theme it blends from the previous roles to the new ones
 * (tween 400ms, emphasized easing). [tint] gives the inline hue-locked tones the design uses,
 * like `oklch(.2 .03 var(--h))`.
 */
@Immutable
class Palette(private val a: Roles, private val b: Roles, private val f: Float) {
    private fun m(x: Color, y: Color) = if (f >= 1f) y else lerp(x, y, f)
    val p = m(a.p, b.p); val op = m(a.op, b.op); val pc = m(a.pc, b.pc); val opc = m(a.opc, b.opc)
    val s = m(a.s, b.s); val sc1 = m(a.sc1, b.sc1); val sc2 = m(a.sc2, b.sc2); val sc3 = m(a.sc3, b.sc3)
    val os = m(a.os, b.os); val osv = m(a.osv, b.osv); val ol = m(a.ol, b.ol)
    val tc = m(a.tc, b.tc); val otc = m(a.otc, b.otc); val sec = m(a.sec, b.sec); val osec = m(a.osec, b.osec)
    val inv = m(a.inv, b.inv); val oinv = m(a.oinv, b.oinv); val t = m(a.t, b.t)

    /** `oklch(l c calc(var(--h) + dh))`, theme independent. */
    fun tint(l: Float, c: Float, dh: Float = 0f): Color {
        val to = oklch(l, c, (b.hue + dh) % 360f)
        return if (f >= 1f || a.hue == b.hue) to else lerp(oklch(l, c, (a.hue + dh) % 360f), to, f)
    }

    /** Dark, hue-tinted panel used by Bit's screen, the logo, the spec sheet and the code block. */
    val ink get() = tint(.2f, .03f)
}

val EmphasizedEasing = CubicBezierEasing(.2f, 0f, 0f, 1f)

val LocalPalette = staticCompositionLocalOf { Palette(roles(145f, false), roles(145f, false), 1f) }

@Composable
fun PaletteProvider(hue: Float, dark: Boolean, content: @Composable () -> Unit) {
    val target = remember(hue, dark) { roles(hue, dark) }
    var from by remember { mutableStateOf(target) }
    var to by remember { mutableStateOf(target) }
    val progress = remember { Animatable(1f) }
    LaunchedEffect(target) {
        if (target != to) {
            from = to
            to = target
            progress.snapTo(0f)
            progress.animateTo(1f, tween(400, easing = EmphasizedEasing))
        }
    }
    val palette = Palette(from, to, progress.value)
    CompositionLocalProvider(LocalPalette provides palette, content = content)
}
