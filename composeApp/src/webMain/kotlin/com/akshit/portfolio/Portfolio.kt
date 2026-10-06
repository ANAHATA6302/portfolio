package com.akshit.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.akshit.portfolio.overlays.AnrDialog
import com.akshit.portfolio.overlays.BootIntro
import com.akshit.portfolio.overlays.Confetti
import com.akshit.portfolio.overlays.QuickSettings
import com.akshit.portfolio.overlays.Toast
import com.akshit.portfolio.sections.Career
import com.akshit.portfolio.sections.Contact
import com.akshit.portfolio.sections.Footer
import com.akshit.portfolio.sections.Hero
import com.akshit.portfolio.sections.HowIBuild
import com.akshit.portfolio.sections.StatusBar
import com.akshit.portfolio.sections.Work
import com.akshit.portfolio.state.AppState
import com.akshit.portfolio.state.LocalApp
import com.akshit.portfolio.state.Screen
import com.akshit.portfolio.theme.Fluid
import com.akshit.portfolio.theme.LocalFluid
import com.akshit.portfolio.theme.LocalFonts
import com.akshit.portfolio.theme.LocalMotion
import com.akshit.portfolio.theme.LocalPalette
import com.akshit.portfolio.theme.Motion
import com.akshit.portfolio.theme.PaletteProvider
import com.akshit.portfolio.theme.rememberFonts
import com.akshit.portfolio.ui.Fg

@Composable
fun Portfolio() {
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val app = remember { AppState(scope, scroll) }
    LaunchedEffect(Unit) { app.start() }
    val fonts = rememberFonts()

    PaletteProvider(app.seed, app.dark) {
        val p = LocalPalette.current
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .background(p.s)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val e = awaitPointerEvent(PointerEventPass.Initial)
                            app.pointer = e.changes.firstOrNull()?.position
                        }
                    }
                },
        ) {
            val fluid = Fluid(maxWidth.value, maxHeight.value)
            CompositionLocalProvider(
                LocalApp provides app,
                LocalFonts provides fonts,
                LocalFluid provides fluid,
                LocalMotion provides Motion(app.whimsy, app.reduceMotion),
            ) {
                Fg(p.os) {
                    Column(Modifier.fillMaxWidth().verticalScroll(scroll)) {
                        StatusBar()
                        Hero()
                        Work()
                        HowIBuild()
                        Career()
                        Contact()
                        Footer()
                    }
                    Confetti()
                    QuickSettings()
                    Toast()
                    AnimatedVisibility(
                        app.screen == Screen.Boot,
                        enter = fadeIn(tween(0)),
                        exit = fadeOut(tween(300)),
                    ) { BootIntro() }
                    if (app.screen == Screen.NotFound) AnrDialog()
                }
            }
        }
    }
}
