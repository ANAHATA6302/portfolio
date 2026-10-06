package com.akshit.portfolio.sections

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.data.CAREER
import com.akshit.portfolio.data.Job
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.ui.Align
import com.akshit.portfolio.ui.Circle
import com.akshit.portfolio.ui.FlexWrap
import com.akshit.portfolio.ui.Justify
import com.akshit.portfolio.ui.MorphPill
import com.akshit.portfolio.ui.Section
import com.akshit.portfolio.ui.SectionLabel
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.animatedColor
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.flex
import com.akshit.portfolio.ui.reveal
import com.akshit.portfolio.ui.tap

@Composable
fun Career() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    Section(top = f.sectionTop, modifier = Modifier.anchor(app, "career")) {
        FlexWrap(gap = f.c(24, 4, 64)) {
            Column(Modifier.flex(grow = 1f, basis = 340.dp).reveal(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionLabel("0x04 // RUNTIME_HISTORY")
                T("Career, as an Activity", fonts.display(f.sp(44, 5.6, 80), .92f, -.04f))
                T(
                    "Every good app has a lifecycle. Here's mine, newest first. Lifecycle purists, look away.",
                    fonts.body(f.sp(17, 1.4, 20)), Modifier.widthIn(max = 420.dp), color = p.osv,
                )
            }
            Column(Modifier.flex(grow = 2f, basis = 560.dp, min = 0.dp)) {
                CAREER.forEachIndexed { i, job -> Entry(i, job) }
            }
        }
    }
}

@Composable
private fun Entry(i: Int, job: Job) {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val open = app.careerOpen == i
    val node = f.c(40, 4, 56)
    val t by animateFloatAsState(if (open) 1f else 0f, motion.expressive())
    val nodeBg = animatedColor(if (open) p.p else p.sc2)
    val cardBg = animatedColor(if (open) p.sc2 else p.sc1)
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(f.c(12, 2, 24))) {
        Column(Modifier.width(node).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(node).background(nodeBg, MorphPill(t, 18.dp)), contentAlignment = Alignment.Center) {
                Box(Modifier.size(14.dp).background(if (open) p.op else p.ol, Circle))
            }
            Box(
                Modifier.weight(1f).width(3.dp).alpha(if (i == CAREER.size - 1) 0f else 1f)
                    .background(p.sc3, css(2.dp)),
            )
        }
        Box(Modifier.weight(1f).padding(bottom = 14.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .tap(css(32.dp), p.os, label = job.role) { app.careerOpen = if (open) -1 else i }
                    .background(cardBg, css(32.dp))
                    .animateContentSize(motion.gentle())
                    .padding(vertical = f.c(18, 2, 26), horizontal = f.c(18, 2.2, 30)),
            ) {
                FlexWrap(gap = 14.dp, rowGap = 6.dp, justify = Justify.SpaceBetween, align = Align.Center) {
                    T(job.cb, fonts.mono(13.sp, 700), color = p.p)
                    T(job.whenText, fonts.mono(13.sp), color = p.osv, maxLines = 1)
                }
                T(job.role, fonts.display(f.sp(24, 2.6, 36), 1.1f, -.02f), Modifier.padding(top = 10.dp), color = p.os)
                T(job.org, fonts.body(f.sp(16, 1.3, 18)), Modifier.padding(top = 4.dp), color = p.osv)
                if (open) {
                    T(
                        job.desc, fonts.body(f.sp(16, 1.3, 17), 400, 1.6f),
                        Modifier.padding(top = 16.dp), color = p.os,
                    )
                }
            }
        }
    }
}
