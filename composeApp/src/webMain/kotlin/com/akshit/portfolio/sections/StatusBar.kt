package com.akshit.portfolio.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.state.AppState
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.ui.Pill
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.hoverColor
import com.akshit.portfolio.ui.tap

/** Records the content Y of a scroll target. */
fun Modifier.anchor(app: AppState, id: String): Modifier = onGloballyPositioned {
    app.anchors[id] = it.positionInRoot().y + app.scroll.value
}

private val NAV = listOf("work" to "Work", "build" to "How I build", "career" to "Career", "contact" to "Contact")

@Composable
fun StatusBar() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val m = f.compact
    val pct = app.pct
    val mono12 = fonts.mono(12.sp)

    Column(Modifier.fillMaxWidth()) {
        // Status strip: clock on the left, the quick settings pill centred on the page.
        Box(
            Modifier.fillMaxWidth().height(40.dp).padding(horizontal = f.c(16, 2, 28)),
            contentAlignment = Alignment.Center,
        ) {
            T(app.clock, mono12, Modifier.align(Alignment.CenterStart), color = p.osv)
            Row(
                Modifier
                    .tap(Pill, p.osv, label = "Open quick settings") { app.qsOpen = !app.qsOpen }
                    .background(p.sc2, Pill)
                    .padding(vertical = 6.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(28.dp, 4.dp).background(p.ol, css(2.dp)))
                T(if (m) "pull" else "pull for quick settings", mono12, color = p.osv, maxLines = 1)
            }
        }
        // Scroll progress
        val w by animateFloatAsState(pct / 100f, tween(200))
        Box(Modifier.fillMaxWidth().height(4.dp).background(p.sc2)) {
            Box(Modifier.fillMaxWidth(w).height(4.dp).background(p.p, css(0.dp, 2.dp, 2.dp, 0.dp)))
        }
        // App bar
        Row(
            Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = f.side),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                // A tiny Bit: green head, dark screen, two eyes.
                Box(Modifier.size(34.dp).background(p.p, css(12.dp)), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier.size(24.dp, 16.dp).background(p.ink, css(6.dp)),
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.size(4.dp, 7.dp).background(p.tint(.9f, .14f), css(2.dp)))
                        Box(Modifier.size(4.dp, 7.dp).background(p.tint(.9f, .14f), css(2.dp)))
                    }
                }
                T(
                    buildAnnotatedString {
                        append("akshit")
                        withStyle(SpanStyle(color = p.p)) { append(".kt") }
                    },
                    fonts.display(22.sp, 1.2f, -.02f, 700),
                    color = p.os,
                )
            }
            if (!m) {
                Row(
                    Modifier.background(p.sc1, Pill).padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    NAV.forEachIndexed { i, (id, label) ->
                        val src = remember { MutableInteractionSource() }
                        val sel = i == 0
                        val bg = hoverColor(src, if (sel) p.sec else androidx.compose.ui.graphics.Color.Transparent, if (sel) p.sc3 else p.sc2)
                        Box(
                            Modifier
                                .tap(Pill, null, src) { app.go(id) }
                                .background(bg, Pill)
                                .padding(vertical = 10.dp, horizontal = 20.dp),
                        ) { T(label, fonts.body(15.sp, 600), color = if (sel) p.osec else p.osv) }
                    }
                }
            }
            QsButton { app.qsOpen = !app.qsOpen }
        }
        if (m) {
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NAV.forEachIndexed { i, (id, label) ->
                    val sel = i == 0
                    Box(
                        Modifier
                            .tap(Pill) { app.go(id) }
                            .background(if (sel) p.sec else p.sc1, Pill)
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                    ) { T(label, fonts.body(14.sp, 600), color = if (sel) p.osec else p.osv, maxLines = 1) }
                }
            }
        }
    }
}

@Composable
private fun QsButton(onClick: () -> Unit) {
    val p = LocalPalette.current
    Box(
        Modifier.size(52.dp).tap(css(18.dp), p.os, label = "Quick settings", onClick = onClick).background(p.sc2, css(18.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.size(12.dp).background(p.p, css(4.dp)))
                Box(Modifier.size(12.dp).background(p.osv, css(6.dp)))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.size(12.dp).background(p.osv, css(6.dp)))
                Box(Modifier.size(12.dp).background(p.osv, css(4.dp)))
            }
        }
    }
}
