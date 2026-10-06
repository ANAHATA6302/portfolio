package com.akshit.portfolio.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.data.ACTS
import com.akshit.portfolio.data.Links
import com.akshit.portfolio.platform.Browser
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.body
import com.akshit.portfolio.theme.display
import com.akshit.portfolio.theme.mono
import com.akshit.portfolio.ui.Align
import com.akshit.portfolio.ui.Chip
import com.akshit.portfolio.ui.Circle
import com.akshit.portfolio.ui.CenterScrollRow
import com.akshit.portfolio.ui.CssGrid
import com.akshit.portfolio.ui.Fg
import com.akshit.portfolio.ui.FlexWrap
import com.akshit.portfolio.ui.Phone
import com.akshit.portfolio.ui.Pill
import com.akshit.portfolio.ui.PillButton
import com.akshit.portfolio.ui.Section
import com.akshit.portfolio.ui.SectionLabel
import com.akshit.portfolio.ui.Shot
import com.akshit.portfolio.ui.T
import com.akshit.portfolio.ui.animatedColor
import com.akshit.portfolio.ui.css
import com.akshit.portfolio.ui.flex
import com.akshit.portfolio.ui.hoverLift
import com.akshit.portfolio.ui.linkHover
import com.akshit.portfolio.ui.margin
import com.akshit.portfolio.ui.outline
import com.akshit.portfolio.ui.rememberTilt
import com.akshit.portfolio.ui.reveal
import com.akshit.portfolio.ui.tap
import com.akshit.portfolio.ui.tilted
import com.akshit.portfolio.ui.tiltSource
import portfolio.composeapp.generated.resources.Res
import portfolio.composeapp.generated.resources.oneapp_home
import portfolio.composeapp.generated.resources.oneapp_remote
import portfolio.composeapp.generated.resources.roamio_activity
import portfolio.composeapp.generated.resources.roamio_home
import portfolio.composeapp.generated.resources.vantage_create
import portfolio.composeapp.generated.resources.vantage_home
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SectionHeader(label: String, aside: String?, title: String, sub: String, subMax: Dp, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionLabel(label, aside)
        T(title, fonts.display(f.sp(44, 5.6, 80), .92f, -.04f))
        T(sub, fonts.body(f.sp(17, 1.4, 20)), Modifier.widthIn(max = subMax), color = p.osv)
    }
}

@Composable
fun Work() {
    val app = LocalApp.current
    val f = LocalFluid.current
    Section(top = f.sectionTop, modifier = Modifier.anchor(app, "work")) {
        Column(verticalArrangement = Arrangement.spacedBy(f.c(16, 1.8, 24))) {
            SectionHeader(
                "0x02 // SELECTED_REPOS", "(the ones I show my mum)", "Things I've shipped",
                "Four apps of my own on Google Play, and one that lives inside some very nice cars.", 620.dp,
                Modifier.reveal().margin(bottom = f.c(8, 1.5, 20)),
            )
            Vantage()
            RangeRover()
            FlexWrap(gap = f.c(16, 1.8, 24)) {
                Roamio(Modifier.flex(grow = 1f, basis = 520.dp, min = 0.dp))
                ThisPortfolio(Modifier.flex(grow = 1f, basis = 420.dp, min = 0.dp))
            }
            MoreOnPlay()
        }
    }
}

private val cardRadius @Composable get() = LocalFluid.current.c(32, 3.4, 48)

/** Draws "★" (no bundled font has the glyph) followed by the text. */
@Composable
private fun StarChip(text: String, bg: Color, fg: Color) {
    val fonts = LocalFonts.current
    Row(
        Modifier.background(bg, Pill).padding(vertical = 8.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(Modifier.size(10.dp)) {
            val r = size.minDimension / 2f
            val c = Offset(size.width / 2f, size.height / 2f + r * .08f)
            val path = Path()
            for (i in 0 until 10) {
                val rad = if (i % 2 == 0) r else r * .42f
                val a = -PI / 2 + i * PI / 5
                val x = c.x + (rad * cos(a)).toFloat()
                val y = c.y + (rad * sin(a)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, fg)
        }
        T(text, fonts.mono(13.sp, 600), color = fg, maxLines = 1)
    }
}

@Composable
private fun Vantage() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val chip = fonts.mono(13.sp, 600)
    FlexWrap(
        Modifier.reveal().background(p.sc1, css(cardRadius)).padding(f.c(22, 3.4, 48)),
        gap = f.c(24, 3, 48),
        align = Align.Center,
    ) {
        Column(Modifier.flex(grow = 1f, basis = 440.dp, min = 0.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            FlexWrap(gap = 8.dp) {
                Chip("FLAGSHIP", chip, p.p, p.op)
                Chip("Live on Google Play", chip, p.sc3, p.os)
                StarChip("5.0 rating", p.tc, p.otc)
            }
            T("Vantage Point", fonts.display(f.sp(48, 6, 88), .9f, -.045f))
            T(
                "Bury a memory at a real place. A time capsule you find, not scroll.",
                fonts.body(f.sp(19, 1.7, 24), 500, 1.4f), color = p.osv,
            )
            CssGrid(minCol = 220.dp, gap = 10.dp) {
                Highlight("On-device E2E encryption", "Per-capsule keys in Android Keystore, with recovery phrase escrow.", p.pc, p.opc, p.opc)
                Highlight("Scan an object to unlock", "ML Kit recognition with a composite scoring model.", p.sc2, p.os, p.osv)
                Highlight("No background location", "A custom geometry engine, while-in-use only.", p.sc2, p.os, p.osv)
                Highlight("Server-enforced time locks", "Cloud Functions decide when a capsule opens. Not your phone's clock.", p.sec, p.osec, p.osec)
            }
            FlexWrap(gap = 12.dp) {
                PillButton("View on Google Play", p.p, p.op, url = Links.VANTAGE)
                PillButton(if (app.specOpen) "Back to screenshots" else "Flip to spec sheet", p.sec, p.osec) {
                    app.specOpen = !app.specOpen
                }
            }
        }
        AnimatedContent(
            app.specOpen,
            Modifier.flex(grow = 1f, basis = 420.dp, min = 0.dp),
            transitionSpec = {
                if (motion.moves) (fadeIn(tween(300)) + scaleIn(motion.gentle(), initialScale = .96f)) togetherWith fadeOut(tween(150))
                else fadeIn(tween(150)) togetherWith fadeOut(tween(150))
            },
        ) { spec ->
            if (spec) SpecSheet()
            else {
                val tilt = rememberTilt()
                Box(Modifier.fillMaxWidth().tiltSource(tilt)) {
                    CenterScrollRow(
                        f.c(14, 1.6, 22), PaddingValues(start = 4.dp, end = 4.dp, top = 10.dp, bottom = 24.dp),
                        Modifier.tilted(tilt),
                    ) {
                        val w = f.c(190, 18, 260)
                        Phone(
                            Res.drawable.vantage_home, "Vantage Point map of capsules", w, 9f / 19.5f, 38.dp, 8.dp,
                            30.dp, 60.dp, (-24).dp, .45f,
                            Modifier.graphicsLayer { rotationZ = -3f; translationY = 10.dp.toPx() },
                        )
                        Phone(
                            Res.drawable.vantage_create, "Vantage Point new capsule screen", w, 9f / 19.5f, 38.dp, 8.dp,
                            30.dp, 60.dp, (-24).dp, .45f,
                            Modifier.graphicsLayer { rotationZ = 3f; translationY = (-10).dp.toPx() },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Highlight(title: String, sub: String, bg: Color, fg: Color, subFg: Color) {
    val fonts = LocalFonts.current
    Column(Modifier.background(bg, css(24.dp)).padding(vertical = 16.dp, horizontal = 18.dp)) {
        T(title, fonts.body(16.sp, 700), color = fg)
        T(sub, fonts.body(14.sp, 400, 1.4f), Modifier.padding(top = 4.dp), color = subFg)
    }
}

private fun bold(head: String, tail: String = "", br: Boolean = false) = buildAnnotatedString {
    withStyle(SpanStyle(fontWeight = FontWeight.W700)) { append(head) }
    if (tail.isNotEmpty()) append(if (br) "\n$tail" else " $tail")
}

@Composable
private fun SpecSheet() {
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val mono = fonts.mono(13.sp)
    val r16 = css(16.dp)
    Fg(p.tint(.95f, .02f)) {
        Column(
            Modifier.fillMaxWidth().background(p.ink, css(32.dp)).padding(f.c(18, 2.2, 28)),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                T("SPEC_SHEET // architecture", mono, color = p.tint(.8f, .12f))
                T("multi-module", mono, color = p.tint(.8f, .12f))
            }
            SpecBox(bold(":app", "Compose UI, navigation, Koin graph"), Modifier.background(p.tint(.32f, .06f), r16).padding(14.dp), center = true)
            CssGrid(fixedCols = 3, gap = 8.dp) {
                for (m in listOf(":feature:home", ":feature:recovery", ":feature:sharing")) {
                    SpecBox(
                        bold(m), Modifier.background(p.tint(.85f, .12f), r16).padding(vertical = 12.dp, horizontal = 8.dp),
                        center = true, color = p.tint(.22f, .06f),
                    )
                }
            }
            SpecBox(
                bold(":core:domain", "use cases, pure Kotlin, zero Android imports"),
                Modifier.outline(p.tint(.5f, .06f), r16).padding(12.dp), center = true,
            )
            CssGrid(fixedCols = 2, gap = 8.dp) {
                listOf(
                    ":core:data" to "Firestore, Storage, Auth",
                    ":core:crypto" to "Keystore, per-capsule keys, timed key erasure",
                    ":core:geo" to "in-process proximity engine",
                    ":core:billing" to "RevenueCat headless, custom paywall",
                ).forEach { (a, b) ->
                    SpecBox(bold(a, b, br = true), Modifier.background(p.tint(.28f, .04f), r16).padding(12.dp))
                }
            }
            SpecBox(
                bold("Cloud Functions (TypeScript)", "time locks, webhook entitlements, server authoritative"),
                Modifier.background(p.tint(.78f, .1f, 70f), r16).padding(12.dp),
                color = p.tint(.22f, .06f, 70f),
            )
            T(
                "CI: GitHub Actions running ktlint, detekt, JVM unit tests, Koin wiring tests and Firestore emulator tests. Red builds don't merge.",
                fonts.mono(13.sp, 500, 1.5f), color = p.tint(.8f, .03f),
            )
        }
    }
}

@Composable
private fun SpecBox(text: AnnotatedString, modifier: Modifier, center: Boolean = false, color: Color = Color.Unspecified) {
    val fonts = LocalFonts.current
    Box(modifier.fillMaxWidth()) {
        T(
            text, fonts.mono(13.sp).copy(textAlign = if (center) TextAlign.Center else TextAlign.Start),
            Modifier.fillMaxWidth(), color = color,
        )
    }
}

@Composable
private fun RangeRover() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val fg = p.tint(.95f, .01f)
    val bg = p.tint(.17f, .012f)
    Fg(fg) {
        FlexWrap(
            Modifier.reveal().background(bg, css(cardRadius)).padding(f.c(22, 3.4, 48)),
            gap = f.c(24, 3, 48),
            align = Align.Center,
        ) {
            Column(Modifier.flex(grow = 1f, basis = 440.dp, min = 0.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                val chip = fonts.mono(13.sp, 600)
                FlexWrap(gap = 8.dp) {
                    Chip("ENTERPRISE", chip, fg, bg)
                    Chip("Jaguar Land Rover", chip, border = p.tint(.45f, .02f), fg = fg)
                }
                Column {
                    T("Range Rover App", fonts.display(f.sp(44, 5.4, 80), .92f, -.045f))
                    T("known internally as OneApp", fonts.mono(13.sp), color = p.tint(.75f, .02f))
                }
                T(
                    "The companion app for Range Rover Electric and Range Rover Sport Electric. Lock it, warm it up, find it, charge it, and track your order, all from your phone.",
                    fonts.body(f.sp(18, 1.5, 22), 500, 1.45f), color = p.tint(.85f, .015f),
                )
                Column(
                    Modifier.fillMaxWidth().background(p.tint(.24f, .015f), css(24.dp)).padding(vertical = 18.dp, horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    T("MY_ROLE", fonts.mono(12.sp), color = p.tint(.8f, .12f))
                    T(
                        "I own remote connectivity: every action sent to the car and every bit of data coming back. I'm also the team's security champion, running threat models, API reviews and dependency scans.",
                        fonts.body(16.sp, 400, 1.5f),
                    )
                }
                CssGrid(fixedCols = 4, gap = 8.dp) {
                    ACTS.forEach { a -> CarAction(a.id, if (app.carOn[a.id] == true) a.on else a.label, app.carOn[a.id] == true) }
                }
                PillButton("View on Google Play", fg, bg, url = Links.RANGE_ROVER)
            }
            val tilt = rememberTilt()
            Box(Modifier.flex(grow = 1f, basis = 420.dp, min = 0.dp).tiltSource(tilt)) {
                CenterScrollRow(16.dp, PaddingValues(start = 4.dp, end = 4.dp, top = 6.dp, bottom = 12.dp), Modifier.tilted(tilt)) {
                    val w = f.c(180, 17, 250)
                    Shot(Res.drawable.oneapp_home, "Range Rover App home screen", w, 620f / 1344f, 28.dp)
                    Shot(Res.drawable.oneapp_remote, "Range Rover App remote actions", w, 620f / 1344f, 28.dp)
                }
            }
        }
    }
}

@Composable
private fun CarAction(id: String, label: String, on: Boolean) {
    val app = LocalApp.current
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    val motion = LocalMotion.current
    val src = remember { MutableInteractionSource() }
    val rad by animateDpAsState(if (on) 28.dp else 20.dp, motion.expressive())
    val dot by animateFloatAsState(if (on) 1f else 0f, motion.expressive())
    val bg = animatedColor(if (on) p.tint(.85f, .12f) else p.tint(.24f, .015f))
    val fg = if (on) p.tint(.2f, .05f) else p.tint(.95f, .01f)
    val dotC = if (on) p.tint(.3f, .08f) else p.tint(.8f, .12f)
    val shape = css(rad)
    Column(
        Modifier
            .hoverLift(src, dy = -4f)
            .tap(shape, fg, src) { app.carTap(id) }
            .background(bg, shape)
            .padding(vertical = 14.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Dot morphs from a 4dp-rounded square to a circle.
        Box(Modifier.size(14.dp).background(dotC, css((4f + 3f * dot).dp)))
        T(label, fonts.body(13.sp, 600), color = fg, maxLines = 1)
    }
}

@Composable
private fun Roamio(modifier: Modifier) {
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    Fg(p.otc) {
        Column(
            modifier.reveal().background(p.tc, css(cardRadius)).padding(f.c(22, 3, 40)),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Chip("PERSONAL", fonts.mono(13.sp, 600), p.s, p.os)
            T("Roamio", fonts.display(f.sp(40, 4.4, 64), .92f, -.04f))
            T(
                "An offline-first travel planner. Your itinerary still works on the plane, in the tunnel, and in that one café with no signal.",
                fonts.body(f.sp(17, 1.4, 20), 400, 1.45f),
            )
            FlexWrap(gap = 8.dp) {
                listOf("Firebase Auth", "DataStore", "Build flavours", "Daily automated Play releases").forEach {
                    Chip(it, fonts.body(14.sp, 600), border = p.otc)
                }
            }
            val tilt = rememberTilt()
            // margin-top: auto
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.BottomCenter) {
              Box(Modifier.fillMaxWidth().tiltSource(tilt)) {
                CenterScrollRow(14.dp, PaddingValues(vertical = 12.dp, horizontal = 4.dp), Modifier.tilted(tilt)) {
                    val w = f.c(170, 14, 210)
                    Phone(Res.drawable.roamio_home, "Roamio home", w, 600f / 1150f, 34.dp, 7.dp, 24.dp, 50.dp, (-24).dp, .4f)
                    Phone(Res.drawable.roamio_activity, "Roamio activity", w, 600f / 1150f, 34.dp, 7.dp, 24.dp, 50.dp, (-24).dp, .4f)
                }
              }
            }
            PillButton("View on Google Play", p.otc, p.tc, height = 52.dp, padX = 24.dp, size = 16.sp, url = Links.ROAMIO)
        }
    }
}

@Composable
private fun ThisPortfolio(modifier: Modifier) {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    val r = cardRadius
    val pad = f.c(22, 3, 40)
    // The button sits in the 120dp bottom-left curve: keep its bottom-left corner 12dp inside it.
    val curve = 120f
    val dy = curve - pad.value
    val clear = (curve - kotlin.math.sqrt(curve * curve - dy * dy) + 12f - pad.value).coerceAtLeast(0f).dp
    Fg(p.op) {
        Column(
            modifier.reveal().background(p.p, css(r, r, r, 120.dp)).padding(pad),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Chip("META", fonts.mono(13.sp, 600), p.op, p.p)
            T("This portfolio", fonts.display(f.sp(40, 4.4, 64), .92f, -.04f))
            T("You're looking at it. Yes, this is Kotlin.", fonts.body(f.sp(20, 1.8, 26), 600, 1.3f))
            T(
                "Kotlin Multiplatform and Compose for Web, rendered to a canvas, hosted on Netlify. Every bounce you've felt so far is a Compose spring().",
                fonts.body(17.sp, 400, 1.5f), color = p.op.copy(alpha = .9f),
            )
            Box(
                Modifier.fillMaxWidth().background(p.ink, css(20.dp)).horizontalScroll(rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 18.dp),
            ) {
                T("fun main() = ComposeViewport {\n    Portfolio(whimsy = true)\n}", fonts.mono(14.sp, 500, 1.6f), color = p.tint(.9f, .1f), maxLines = 3)
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.BottomStart) {
                PillButton(
                    "Open Quick Settings", p.op, p.p, height = 52.dp, padX = 24.dp, size = 16.sp,
                    modifier = Modifier.padding(start = clear),
                ) { app.qsOpen = !app.qsOpen }
            }
        }
    }
}

@Composable
private fun MoreOnPlay() {
    val app = LocalApp.current
    val p = LocalPalette.current
    val f = LocalFluid.current
    val fonts = LocalFonts.current
    FlexWrap(
        Modifier.anchor(app, "more").reveal().fillMaxWidth().background(p.sc1, css(32.dp))
            .padding(vertical = 18.dp, horizontal = f.c(18, 2, 28)),
        gap = 12.dp,
        align = Align.Center,
    ) {
        T("MORE_ON_PLAY", fonts.mono(13.sp), Modifier.padding(end = 8.dp), color = p.osv)
        AppLink("PotterPedia", Links.POTTERPEDIA, p.t, css(10.dp))
        AppLink("HangMan", Links.DEVELOPER, p.p, Circle)
        AppLink(
            "Find My Colors", Links.DEVELOPER, p.tc,
            com.akshit.portfolio.ui.CssShape(
                com.akshit.portfolio.ui.Corner(com.akshit.portfolio.ui.Len.Abs(10.dp)),
                com.akshit.portfolio.ui.Corner(com.akshit.portfolio.ui.Len.Pct(50f)),
                com.akshit.portfolio.ui.Corner(com.akshit.portfolio.ui.Len.Pct(50f)),
                com.akshit.portfolio.ui.Corner(com.akshit.portfolio.ui.Len.Pct(50f)),
            ),
        )
        val src = remember { MutableInteractionSource() }
        Box(
            Modifier.flex(autoStart = true).linkHover(src).tap(css(8.dp), interaction = src, role = androidx.compose.ui.semantics.Role.Button) {
                Browser.open(Links.DEVELOPER)
            }.padding(vertical = 10.dp, horizontal = 4.dp),
        ) { T("See all on Google Play →", fonts.body(16.sp, 700), color = p.p, maxLines = 1) }
    }
}

@Composable
private fun AppLink(name: String, url: String, color: Color, shape: Shape) {
    val p = LocalPalette.current
    val fonts = LocalFonts.current
    val src = remember { MutableInteractionSource() }
    Row(
        Modifier.linkHover(src).tap(Pill, interaction = src) { Browser.open(url) }
            .background(p.sc3, Pill).padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(28.dp).background(color, shape))
        T(name, fonts.body(16.sp, 700), color = p.os, maxLines = 1)
    }
}
