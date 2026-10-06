package com.akshit.portfolio.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Motion tokens.
 * - expressive: spring(0.6, 380). Hover scale, shape morphs, QS shade, snackbar, Bit jump, tilt return.
 * - gentle: spring(0.8, 200). Scroll reveals, spec sheet swap, career expand.
 * - snappy: spring(1, 1500). Toggles, swatch ring, selection.
 * Whimsy off turns every spring into spring(1, 500). Reduce motion turns everything into a 150ms fade
 * and drops translate, scale and rotate.
 */
@Immutable
class Motion(val whimsy: Boolean, val reduce: Boolean) {
    /** Translate, scale and rotate are allowed. */
    val moves get() = !reduce

    /** Wiggle, blink, eye tracking, tilt and confetti. */
    val playful get() = whimsy && !reduce

    fun <T> expressive(): FiniteAnimationSpec<T> = pick(spring(0.6f, 380f))
    fun <T> gentle(): FiniteAnimationSpec<T> = pick(spring(0.8f, 200f))
    fun <T> snappy(): FiniteAnimationSpec<T> = if (reduce) tween(150, easing = LinearEasing) else spring(1f, 1500f)

    /** Plain CSS-like transitions (background .3s etc). */
    fun <T> fade(ms: Int = 300): FiniteAnimationSpec<T> = tween(if (reduce) 150 else ms, easing = CssEase)

    /** The easing the reference uses for keyframed bounces: overshoot with whimsy, none without. */
    val bounceEasing: Easing get() = if (whimsy) ExpressiveBezier else EmphasizedEasing

    private fun <T> pick(s: FiniteAnimationSpec<T>): FiniteAnimationSpec<T> = when {
        reduce -> tween(150, easing = LinearEasing)
        !whimsy -> spring(1f, 500f)
        else -> s
    }
}

val ExpressiveBezier = CubicBezierEasing(.34f, 1.56f, .64f, 1f)
val CssEase = CubicBezierEasing(.25f, .1f, .25f, 1f)

val LocalMotion = staticCompositionLocalOf { Motion(whimsy = true, reduce = false) }
