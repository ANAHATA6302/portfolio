package com.akshit.portfolio.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.hoverable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalMotion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val Expressive = CubicBezierEasing(.34f, 1.56f, .64f, 1f)

/**
 * WAAPI-style keyframes: evenly spaced, linear between frames, with the timing function applied to
 * the overall progress. When the easing overshoots past 1 or below 0 the end segment is extrapolated,
 * which is how the browser renders `cubic-bezier(.34,1.56,.64,1)` on a keyframe effect.
 */
fun keyframes(frames: FloatArray, p: Float): Float {
    val n = frames.size - 1
    val x = p * n
    val i = x.toInt().coerceIn(0, n - 1)
    val f = x - i
    return frames[i] + (frames[i + 1] - frames[i]) * f
}

/** The two reference bounces: the Bit jump and the tile squash. */
@Stable
class Bounce(private val scope: CoroutineScope) {
    private val t = Animatable(1f)
    var jump by mutableStateOf(false)
        private set

    fun play(isJump: Boolean, reduce: Boolean) {
        if (reduce) return
        jump = isJump
        scope.launch {
            t.snapTo(0f)
            t.animateTo(1f, tween(if (isJump) 650 else 480, easing = LinearEasing))
        }
    }

    val progress get() = Expressive.transform(t.value)
    val running get() = t.value < 1f
}

@Composable
fun rememberBounce(): Bounce {
    val scope = rememberCoroutineScope()
    return remember { Bounce(scope) }
}

private val JumpY = floatArrayOf(0f, -22f, 0f)
private val JumpR = floatArrayOf(0f, -6f, 0f)
private val Squash = floatArrayOf(1f, .93f, 1.05f, 1f)

fun Modifier.bounce(b: Bounce): Modifier = graphicsLayer {
    if (!b.running) return@graphicsLayer
    val p = b.progress
    if (b.jump) {
        translationY = keyframes(JumpY, p) * density
        rotationZ = keyframes(JumpR, p)
    } else {
        val s = keyframes(Squash, p)
        scaleX = s; scaleY = s
    }
}

/**
 * Hover lift used on stat tiles, widgets and car actions: translate, rotate and scale with the
 * expressive spring. Pass the same interaction source the clickable uses.
 */
@Composable
fun Modifier.hoverLift(
    src: MutableInteractionSource,
    dy: Float = 0f,
    rot: Float = 0f,
    scale: Float = 1f,
): Modifier {
    val motion = LocalMotion.current
    val hovered by src.collectIsHoveredAsState()
    val on = hovered && motion.moves
    val f by animateFloatAsState(if (on) 1f else 0f, motion.expressive())
    return this.hoverable(src).graphicsLayer {
        translationY = dy * f * density
        rotationZ = rot * f
        val s = 1f + (scale - 1f) * f
        scaleX = s; scaleY = s
    }
}

/** Scroll reveal: items below the fold at first layout fade in and rise 48dp once 12% is visible. */
@Composable
fun Modifier.reveal(): Modifier {
    val motion = LocalMotion.current
    val fluid = LocalFluid.current
    val scope = rememberCoroutineScope()
    val state = remember { RevealState() }
    if (motion.reduce && state.phase == 0) return this
    return this
        .onGloballyPositioned { c ->
            val top = c.positionInRoot().y
            val h = c.size.height
            if (state.phase == 0) {
                state.phase = if (top > fluid.height) 1 else 3
                if (state.phase == 3) return@onGloballyPositioned
            }
            if (state.phase == 1 && top < fluid.height - h * .12f) {
                state.phase = 2
                scope.launch { state.alpha.animateTo(1f, tween(500, easing = CubicBezierEasing(.25f, .1f, .25f, 1f))) }
                scope.launch { state.rise.animateTo(0f, motion.gentle()) }
            }
        }
        .graphicsLayer {
            if (state.phase == 1 || state.phase == 2) {
                alpha = state.alpha.value
                translationY = state.rise.value * 48.dp.toPx()
            }
        }
}

private class RevealState {
    var phase by mutableStateOf(0)
    val alpha = Animatable(0f)
    val rise = Animatable(1f)
}

/**
 * Cursor tilt: rotateY = px * 10, rotateX = -py * 8, with a .4s overshoot transition both ways.
 * Apply [tiltSource] to the hover area and [tilted] to the element that rotates.
 */
@Stable
class Tilt(private val scope: CoroutineScope) {
    val rx = Animatable(0f)
    val ry = Animatable(0f)
    fun to(x: Float, y: Float) {
        scope.launch { ry.animateTo(x, tween(400, easing = Expressive)) }
        scope.launch { rx.animateTo(y, tween(400, easing = Expressive)) }
    }
}

@Composable
fun rememberTilt(): Tilt {
    val scope = rememberCoroutineScope()
    return remember { Tilt(scope) }
}

@Composable
fun Modifier.tiltSource(t: Tilt): Modifier {
    val motion = LocalMotion.current
    return this.pointerInput(motion.playful) {
        awaitPointerEventScope {
            while (true) {
                val e = awaitPointerEvent()
                when (e.type) {
                    PointerEventType.Move, PointerEventType.Enter -> if (motion.playful) {
                        val p = e.changes.first().position
                        val px = p.x / size.width - .5f
                        val py = p.y / size.height - .5f
                        t.to(px * 10f, -py * 8f)
                    }
                    PointerEventType.Exit -> t.to(0f, 0f)
                }
            }
        }
    }
}

@Composable
fun Modifier.tilted(t: Tilt): Modifier {
    val d = LocalDensity.current.density
    return graphicsLayer {
        rotationX = t.rx.value
        rotationY = t.ry.value
        cameraDistance = 1200f / 72f * d
        transformOrigin = TransformOrigin.Center
    }
}
