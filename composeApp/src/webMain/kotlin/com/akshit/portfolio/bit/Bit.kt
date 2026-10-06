package com.akshit.portfolio.bit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt

enum class Pose { Idle, Wave, Loading, Sleep, Anr, Dev }

private const val EYES_CX = 60f
private const val EYES_CY = 55.5f

/**
 * Bit, drawn on a 120x140 base and scaled to [size] wide. Every shape mirrors Bit.dc.html.
 * Blinks with the global tick and, with whimsy on, follows the pointer with its eyes.
 */
@Composable
fun Bit(pose: Pose, size: Int, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    val app = LocalApp.current
    val motion = LocalMotion.current
    val fonts = LocalFonts.current
    val measurer = rememberTextMeasurer()
    val s = size / 120f
    val density = LocalDensity.current.density
    val h = (size * 140f / 120f).roundToInt()

    val blink = remember { Animatable(1f) }
    LaunchedEffect(app.blink) {
        if (app.blink > 0 && motion.playful) {
            blink.animateTo(.1f, tween(100))
            delay(30)
            blink.animateTo(1f, tween(100))
        }
    }

    var origin by remember { mutableStateOf(Offset.Zero) }
    val ex = remember { Animatable(0f) }
    val ey = remember { Animatable(0f) }
    val normalEyes = pose == Pose.Idle || pose == Pose.Wave || pose == Pose.Dev
    val ptr = app.pointer
    LaunchedEffect(ptr, origin, motion.playful, normalEyes) {
        if (!motion.playful || !normalEyes || ptr == null) return@LaunchedEffect
        // The reference works in CSS px, which are dp here.
        val c = origin / density + Offset(EYES_CX * s, EYES_CY * s)
        val dx = ptr.x / density - c.x
        val dy = ptr.y / density - c.y
        val d = hypot(dx, dy).takeIf { it > 0f } ?: 1f
        val m = min(6f, d / 40f)
        launch { ex.animateTo(dx / d * m, tween(150)) }
        launch { ey.animateTo(dy / d * m * .7f, tween(150)) }
    }

    val ink = p.ink
    val glyph = p.tint(.9f, .14f)
    val zStyle = TextStyle(fontFamily = fonts.mono, fontWeight = FontWeight.W700, fontSize = 18.sp)
    val zSmall = zStyle.copy(fontSize = 13.sp)

    Canvas(
        modifier
            .semantics { contentDescription = "Bit, the robot mascot" }
            .onGloballyPositioned { origin = it.positionInRoot() }
            .size(size.dp, h.dp),
    ) {
        val d = density
        // Work in CSS px of the base drawing: 1 base unit = s dp.
        scale(s * d, s * d, pivot = Offset.Zero) {
            drawBit(pose, p.os, p.p, ink, glyph, p.t, p.tc, p.pc, blink.value, ex.value, ey.value)
            if (pose == Pose.Sleep) {
                val z1 = measurer.measure("z", zStyle)
                val z2 = measurer.measure("z", zSmall)
                // Text is laid out in px; undo the canvas scale for the glyph size and place by base units.
                scale(1f / d, 1f / d, pivot = Offset.Zero) {
                    val baseY = (-4f) * d
                    val l1 = z1.firstBaseline
                    drawText(z1, p.osv, Offset(100f * d, baseY + (18f * d - z1.size.height) / 2f))
                    drawText(z2, p.osv, Offset(100f * d + z1.size.width, baseY + (18f * d - z1.size.height) / 2f + l1 - z2.firstBaseline - 10f * d))
                }
            }
        }
    }
}

private fun DrawScope.rr(x: Float, y: Float, w: Float, h: Float, r: Float, c: Color) =
    drawRoundRect(c, Offset(x, y), Size(w, h), CornerRadius(r, r))

private fun DrawScope.drawBit(
    pose: Pose,
    os: Color, primary: Color, ink: Color, glyph: Color, t: Color, tc: Color, pc: Color,
    blink: Float, ex: Float, ey: Float,
) {
    // Antenna
    rr(58f, 6f, 4f, 20f, 2f, os)
    if (pose == Pose.Dev) {
        val tri = Path().apply { moveTo(60f, -22f); lineTo(78f, 18f); lineTo(42f, 18f); close() }
        drawPath(tri, t)
        drawCircle(pc, 6f, Offset(60f, -24f))
    } else {
        drawCircle(t, 9f, Offset(60f, 9f))
    }
    // Arms
    rr(0f, 56f, 14f, 34f, 7f, os)
    if (pose == Pose.Wave) rotate(-150f, Offset(113f, 63f)) { rr(106f, 56f, 14f, 34f, 7f, os) }
    else rr(106f, 56f, 14f, 34f, 7f, os)
    // Feet: radius 6 6 10 10
    for (x in floatArrayOf(28f, 70f)) {
        val foot = Path().apply {
            addRoundRect(
                RoundRect(
                    x, 112f, x + 22f, 132f,
                    topLeftCornerRadius = CornerRadius(6f), topRightCornerRadius = CornerRadius(6f),
                    bottomRightCornerRadius = CornerRadius(10f), bottomLeftCornerRadius = CornerRadius(10f),
                ),
            )
        }
        drawPath(foot, os)
    }
    // Body with the inset bottom shade
    rr(10f, 22f, 100f, 96f, 36f, primary)
    val body = Path().apply { addRoundRect(RoundRect(10f, 22f, 110f, 118f, CornerRadius(36f))) }
    val up = Path().apply { addRoundRect(RoundRect(10f, 14f, 110f, 110f, CornerRadius(36f))) }
    drawPath(Path.combine(PathOperation.Difference, body, up), Color.Black.copy(alpha = .14f))
    // Screen
    rr(22f, 36f, 76f, 50f, 20f, ink)
    when (pose) {
        Pose.Idle, Pose.Wave, Pose.Dev -> {
            translate(ex, ey) {
                for (x in floatArrayOf(40f, 69f)) {
                    scale(1f, blink, pivot = Offset(x + 5.5f, 55.5f)) { rr(x, 46.5f, 11f, 18f, 6f, glyph) }
                }
            }
            smile(glyph)
        }
        Pose.Sleep -> {
            rr(38f, 53.5f, 14f, 4f, 2f, glyph)
            rr(68f, 53.5f, 14f, 4f, 2f, glyph)
            smile(glyph)
        }
        Pose.Loading -> {
            drawCircle(glyph, 4f, Offset(45f, 57f))
            drawCircle(glyph.copy(alpha = glyph.alpha * .6f), 4f, Offset(60f, 57f))
            drawCircle(glyph.copy(alpha = glyph.alpha * .3f), 4f, Offset(75f, 57f))
            rr(53f, 71f, 14f, 3f, 1.5f, glyph)
        }
        Pose.Anr -> {
            drawCircle(glyph, 5.5f, Offset(45f, 57f), style = Stroke(3f))
            drawCircle(glyph, 5.5f, Offset(75f, 57f), style = Stroke(3f))
            rr(53f, 71f, 14f, 3f, 1.5f, glyph)
        }
    }
    if (pose == Pose.Dev) {
        val shades = Color(0xFF111111)
        for (x in floatArrayOf(18f, 68f)) {
            val lens = Path().apply {
                addRoundRect(
                    RoundRect(
                        x, 45f, x + 34f, 65f,
                        topLeftCornerRadius = CornerRadius(6f), topRightCornerRadius = CornerRadius(6f),
                        bottomRightCornerRadius = CornerRadius(12f), bottomLeftCornerRadius = CornerRadius(12f),
                    ),
                )
            }
            drawPath(lens, shades)
        }
        drawRect(shades, Offset(52f, 53f), Size(16f, 4f))
    }
    // Cheeks
    rr(26f, 96f, 12f, 6f, 3f, tc)
    rr(82f, 96f, 12f, 6f, 3f, tc)
    if (pose == Pose.Anr) {
        // border-radius: 50% 0 50% 50%, rotated -45deg about its centre.
        rotate(-45f, Offset(106f, 26f)) {
            val drop = Path().apply {
                addRoundRect(
                    RoundRect(
                        100f, 20f, 112f, 32f,
                        topLeftCornerRadius = CornerRadius(6f), topRightCornerRadius = CornerRadius.Zero,
                        bottomRightCornerRadius = CornerRadius(6f), bottomLeftCornerRadius = CornerRadius(6f),
                    ),
                )
            }
            drawPath(drop, t)
        }
    }
}

private fun DrawScope.smile(c: Color) {
    val m = Path().apply {
        addRoundRect(
            RoundRect(
                53f, 69.5f, 67f, 75.5f,
                bottomRightCornerRadius = CornerRadius(6f), bottomLeftCornerRadius = CornerRadius(6f),
            ),
        )
    }
    drawPath(m, c)
}
