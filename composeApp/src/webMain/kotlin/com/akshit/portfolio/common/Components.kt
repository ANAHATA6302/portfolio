package com.akshit.portfolio.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppHeaderDivider() {
    HorizontalDivider(
        Modifier
            .widthIn(max = 955.dp)
            .fillMaxWidth(),
        DividerDefaults.Thickness,
        Color.Black.copy(0.5f)
    )
}

@Composable
fun FadeIn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = modifier.alpha(alpha)
    ) {
        content()
    }
}
