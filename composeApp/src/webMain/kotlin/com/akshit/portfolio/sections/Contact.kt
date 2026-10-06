package com.akshit.portfolio.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.bit.Bit
import com.akshit.portfolio.bit.Pose
import com.akshit.portfolio.data.Links
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.ui.Align
import com.akshit.portfolio.ui.Corner
import com.akshit.portfolio.ui.CssShape
import com.akshit.portfolio.ui.Fg
import com.akshit.portfolio.ui.FlexWrap
import com.akshit.portfolio.ui.Justify
import com.akshit.portfolio.ui.Len
import com.akshit.portfolio.ui.Pill
import com.akshit.portfolio.ui.PillButton
import com.akshit.portfolio.ui.Section
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.flex
import com.akshit.portfolio.ui.hoverLift
import com.akshit.portfolio.ui.reveal
import com.akshit.portfolio.ui.tap
import org.jetbrains.compose.resources.painterResource
import portfolio.composeapp.generated.resources.Res
import portfolio.composeapp.generated.resources.akshit

/** `border-radius: 48% 48% 40px 40px` */
private val Portrait = CssShape(
    Corner(Len.Pct(48f)), Corner(Len.Pct(48f)),
    Corner(Len.Abs(40.dp)), Corner(Len.Abs(40.dp)),
)

@Composable
fun Contact() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    Section(top = f.sectionTop, modifier = Modifier.anchor(app, "contact")) {
        Fg(p.opc) {
            FlexWrap(
                Modifier.reveal().fillMaxWidth().background(p.pc, css(f.c(36, 4, 56))).padding(f.c(24, 4.4, 64)),
                gap = f.c(28, 4, 56),
                align = Align.Center,
            ) {
                Column(Modifier.flex(grow = 1f, basis = 520.dp, min = 0.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    T("0x05 // SAY_HELLO", fonts.mono(f.sp(12, 1, 14)))
                    T("Inbox open. Coffee ready. Let's build something.", fonts.display(f.sp(48, 6.4, 96), .9f, -.045f))
                    T(
                        "Hiring, collaborating, or just want to talk Compose? I reply faster than a cold Gradle build.",
                        fonts.body(f.sp(17, 1.5, 21), 400, 1.45f), Modifier.widthIn(max = 560.dp),
                    )
                    EmailPill()
                    FlexWrap(gap = 10.dp) {
                        PillButton("LinkedIn", androidx.compose.ui.graphics.Color.Transparent, p.opc, 52.dp, 22.dp, 16.sp, border = p.opc, url = Links.LINKEDIN)
                        PillButton("GitHub", androidx.compose.ui.graphics.Color.Transparent, p.opc, 52.dp, 22.dp, 16.sp, border = p.opc, url = Links.GITHUB)
                        PillButton("Instagram", androidx.compose.ui.graphics.Color.Transparent, p.opc, 52.dp, 22.dp, 16.sp, border = p.opc, url = Links.INSTAGRAM)
                    }
                    T("Based in Manchester, UK. Open to relocating.", fonts.mono(13.sp))
                }
                // flex: 0 1 380px; min-width: 240px; margin: 0 auto
                Box(Modifier.flex(grow = 0f, basis = 380.dp, min = 240.dp, autoStart = true, autoEnd = true)) {
                    Image(
                        painterResource(Res.drawable.akshit), "Akshit Nahata",
                        Modifier.fillMaxWidth().aspectRatio(4f / 5f).clip(Portrait),
                        contentScale = ContentScale.Crop,
                        alignment = BiasAlignment(0f, -.4f),
                    )
                    BitTap(
                        Pose.Wave, 84,
                        Modifier.align(Alignment.BottomEnd).offset(6.dp, 14.dp),
                        bg = p.s, shape = css(36.dp), pad = 14.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmailPill() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val src = remember { MutableInteractionSource() }
    Row(
        Modifier
            .hoverLift(src, scale = 1.03f)
            .tap(Pill, p.os, src, label = "Copy email address") { app.copyEmail() }
            .background(p.s, Pill)
            .padding(start = f.c(18, 2, 26), top = 10.dp, bottom = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        T(Links.EMAIL, fonts.body(f.sp(16, 1.5, 20), 600), Modifier.weight(1f, fill = false), color = p.os, maxLines = 1, ellipsis = true)
        Box(Modifier.background(p.p, Pill).padding(vertical = 12.dp, horizontal = 18.dp)) {
            T("Copy", fonts.body(15.sp, 700), color = p.op)
        }
    }
}

@Composable
fun Footer() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    Section(top = f.c(48, 6, 80), bottom = f.c(28, 3, 40)) {
        FlexWrap(gap = 28.dp, rowGap = 16.dp, justify = Justify.SpaceBetween, align = Align.Center) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Bit(if (app.dev) Pose.Dev else Pose.Sleep, 44)
                T("© 2026 Akshit Nahata. Built with Compose Multiplatform.", fonts.body(15.sp), color = p.osv)
            }
            Column(
                Modifier
                    .tap(css(16.dp), p.osv, label = "Build number") { app.buildTap() }
                    .background(p.sc1, css(16.dp))
                    .padding(vertical = 12.dp, horizontal = 18.dp),
            ) {
                T("Build number", fonts.mono(13.sp, 700), color = p.os)
                T("2026.10.06 (release)", fonts.mono(13.sp).copy(textAlign = TextAlign.Start), color = p.osv)
            }
        }
    }
}
