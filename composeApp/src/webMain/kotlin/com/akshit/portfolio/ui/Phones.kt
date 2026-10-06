package com.akshit.portfolio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * `display:flex; justify-content:center; overflow-x:auto`: centred when the children fit,
 * horizontally scrollable when they don't.
 */
@Composable
fun CenterScrollRow(
    gap: Dp,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val outer = remember { IntArray(1) }
    Row(
        modifier
            .layout { m, c ->
                if (c.hasBoundedWidth) outer[0] = c.maxWidth
                val p = m.measure(c)
                layout(p.width, p.height) { p.place(0, 0) }
            }
            .horizontalScroll(rememberScrollState())
            .layout { m, c ->
                val min = if (c.hasBoundedWidth) c.minWidth else outer[0]
                val p = m.measure(c.copy(minWidth = min.coerceAtMost(if (c.hasBoundedWidth) c.maxWidth else Constraints.Infinity)))
                layout(p.width, p.height) { p.place(0, 0) }
            }
            .padding(padding),
        horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Top,
        content = content,
    )
}

/** A black phone frame with a screenshot cropped from the top. */
@Composable
fun Phone(
    res: DrawableResource,
    alt: String,
    width: Dp,
    aspect: Float,
    radius: Dp,
    pad: Dp,
    shadowY: Dp,
    shadowBlur: Dp,
    shadowSpread: Dp,
    shadowAlpha: Float,
    modifier: Modifier = Modifier,
) {
    val outer = css(radius)
    Box(
        modifier
            .width(width)
            .aspectRatio(aspect)
            .cssShadow(outer, shadowY, shadowBlur, Color.Black.copy(alpha = shadowAlpha), shadowSpread)
            .background(Color(0xFF0E0F0E), outer)
            .padding(pad),
    ) {
        Image(
            painterResource(res), alt,
            Modifier.fillMaxSize().clip(css(radius - pad)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
        )
    }
}

/** An already framed store image. */
@Composable
fun Shot(res: DrawableResource, alt: String, width: Dp, aspect: Float, radius: Dp, modifier: Modifier = Modifier) {
    Image(
        painterResource(res), alt,
        modifier.width(width).aspectRatio(aspect).clip(css(radius)),
        contentScale = ContentScale.Crop,
    )
}
