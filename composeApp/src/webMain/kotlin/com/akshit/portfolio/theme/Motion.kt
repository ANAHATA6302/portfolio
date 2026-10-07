package com.akshit.portfolio.theme

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

    private fun <T> pick(s: FiniteAnimationSpec<T>): FiniteAnimationSpec<T> = when {
        reduce -> tween(150, easing = LinearEasing)
        !whimsy -> spring(1f, 500f)
        else -> s
    }
}

val LocalMotion = staticCompositionLocalOf { Motion(whimsy = true, reduce = false) }
