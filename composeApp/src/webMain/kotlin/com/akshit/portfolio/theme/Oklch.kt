package com.akshit.portfolio.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * CSS `oklch(l c h)` to sRGB, clipped per channel the same way browsers render
 * out-of-gamut values in the design reference.
 */
fun oklch(l: Float, c: Float, h: Float, alpha: Float = 1f): Color {
    val hr = h * PI / 180.0
    val a = c * cos(hr)
    val b = c * sin(hr)

    val l_ = l + 0.3963377774 * a + 0.2158037573 * b
    val m_ = l - 0.1055613458 * a - 0.0638541728 * b
    val s_ = l - 0.0894841775 * a - 1.2914855480 * b

    val l3 = l_ * l_ * l_
    val m3 = m_ * m_ * m_
    val s3 = s_ * s_ * s_

    val r = 4.0767416621 * l3 - 3.3077115913 * m3 + 0.2309699292 * s3
    val g = -1.2684380046 * l3 + 2.6097574011 * m3 - 0.3413193965 * s3
    val bl = -0.0041960863 * l3 - 0.7034186147 * m3 + 1.7076147010 * s3

    return Color(gamma(r), gamma(g), gamma(bl), alpha)
}

private fun gamma(x: Double): Float {
    val v = if (x <= 0.0031308) 12.92 * x else 1.055 * x.pow(1 / 2.4) - 0.055
    return v.coerceIn(0.0, 1.0).toFloat()
}

fun Color.toHex(): String {
    fun ch(f: Float) = (f * 255f + 0.5f).toInt().coerceIn(0, 255).toString(16).padStart(2, '0')
    return "#" + ch(red) + ch(green) + ch(blue)
}
