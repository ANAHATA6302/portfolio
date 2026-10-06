package com.akshit.portfolio.state

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import com.akshit.portfolio.data.ACTS
import com.akshit.portfolio.data.BOOT
import com.akshit.portfolio.data.BOT
import com.akshit.portfolio.data.COFFEE
import com.akshit.portfolio.data.Links
import com.akshit.portfolio.data.SEEDS
import com.akshit.portfolio.data.WIDGETS
import com.akshit.portfolio.platform.Browser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

enum class Screen { Site, Boot, NotFound }

/** All UI state, mirroring the reference component's state and handlers. */
@Stable
class AppState(private val scope: CoroutineScope, val scroll: ScrollState) {
    var seed by mutableStateOf(Browser.get(K_SEED)?.toFloatOrNull()?.takeIf { h -> SEEDS.any { it.hue == h } } ?: 145f)
    var dark by mutableStateOf(Browser.get(K_DARK)?.let { it == "1" } ?: Browser.media("(prefers-color-scheme: dark)"))
    var whimsy by mutableStateOf(Browser.get(K_WHIMSY)?.let { it == "1" } ?: true)
    var reduceMotion by mutableStateOf(Browser.get(K_MOTION)?.let { it == "1" } ?: Browser.media("(prefers-reduced-motion: reduce)"))

    var qsOpen by mutableStateOf(false)
    var dev by mutableStateOf(false)
    var buildTaps by mutableIntStateOf(0)
    var specOpen by mutableStateOf(false)
    var editMode by mutableStateOf(false)
    var selected by mutableStateOf<String?>(null)
    val order = mutableStateListOf<String>().apply { addAll(WIDGETS.map { it.id }) }
    var careerOpen by mutableIntStateOf(0)
    val carOn = mutableStateMapOf<String, Boolean>()
    var coffee by mutableIntStateOf(3)
    private var coffeeIdx = 0
    private var botIdx = 0
    var sdk by mutableIntStateOf(36)
    var bubble by mutableIntStateOf(0)

    var toastText by mutableStateOf("")
    var toastAction by mutableStateOf<String?>(null)
    var toastOn by mutableStateOf(false)
    private var toastJob: Job? = null

    var screen by mutableStateOf(if (Browser.path.trimEnd('/').isEmpty()) Screen.Site else Screen.NotFound)
    var bootLines by mutableIntStateOf(0)
    var bootProgress by mutableStateOf(0f)
    private var bootJob: Job? = null

    /** Incremented to fire a confetti burst. */
    var confetti by mutableIntStateOf(0)
    /** Incremented every 3.4s to blink every Bit. */
    var blink by mutableIntStateOf(0)
    /** True after Tab navigation, false after any pointer press: the `:focus-visible` heuristic. */
    var keyboardNav by mutableStateOf(false)
    /** Pointer position in root coordinates, for Bit's eyes. */
    var pointer by mutableStateOf<Offset?>(null)

    /** Content Y of each scroll target. */
    val anchors = mutableStateMapOf<String, Float>()

    val pct: Int
        get() {
            val max = scroll.maxValue
            if (max <= 0 || max == Int.MAX_VALUE) return 0
            return (scroll.value.toFloat() / max).coerceAtMost(1f).times(100f).roundToInt()
        }

    fun start() {
        scope.launch { while (isActive) { delay(3400); if (whimsy && !reduceMotion) blink++ } }
        scope.launch { while (isActive) { delay(5000); bubble = (bubble + 1) % 3 } }
        count()
        if (screen == Screen.Site && Browser.get(K_BOOT) != "1") boot()
    }

    // Persistence

    fun persist() {
        Browser.set(K_SEED, seed.toInt().toString())
        Browser.set(K_DARK, if (dark) "1" else "0")
        Browser.set(K_WHIMSY, if (whimsy) "1" else "0")
        Browser.set(K_MOTION, if (reduceMotion) "1" else "0")
    }

    // Toast

    fun toast(text: String, action: String? = null) {
        toastJob?.cancel()
        toastText = text
        toastAction = action
        toastOn = true
        toastJob = scope.launch {
            delay(if (action != null) 4200 else 2800)
            toastOn = false
        }
    }

    fun toastActionTap() {
        if (toastAction == "Undo") toast("Undo failed. It's in your clipboard forever now, like a good commit.")
        else toastOn = false
    }

    // Boot

    private fun boot() {
        screen = Screen.Boot
        bootLines = 0
        bootProgress = 0f
        bootJob = scope.launch {
            launch {
                delay(200)
                for (i in BOOT.indices) {
                    bootLines = i + 1
                    if (i < BOOT.size - 1) delay(330)
                }
            }
            launch {
                val t0 = TimeSource.Monotonic.markNow()
                while (bootProgress < 1f) {
                    delay(16)
                    bootProgress = (t0.elapsedNow().inWholeMilliseconds / 1500f).coerceAtMost(1f)
                }
            }
            delay(1750)
            skipBoot()
        }
    }

    fun skipBoot() {
        bootJob?.cancel()
        Browser.set(K_BOOT, "1")
        screen = Screen.Site
    }

    // Hero

    fun count() {
        if (reduceMotion) { sdk = 36; return }
        scope.launch {
            val t0 = TimeSource.Monotonic.markNow()
            while (true) {
                val p = (t0.elapsedNow().inWholeMilliseconds / 1100f).coerceAtMost(1f)
                sdk = (36 * (1 - (1 - p).pow(3))).roundToInt()
                if (p >= 1f) break
                delay(16)
            }
        }
    }

    fun coffeeTap() {
        coffee++
        toast(COFFEE[coffeeIdx % COFFEE.size])
        coffeeIdx++
    }

    fun botTap() {
        toast(if (dev) "Nice shades, right? Developer perks." else BOT[botIdx % BOT.size])
        botIdx++
    }

    // Scrolling

    fun go(id: String) {
        val y = anchors[id] ?: return
        val target = (y - 12f).roundToInt().coerceIn(0, scroll.maxValue)
        scope.launch {
            if (reduceMotion) scroll.scrollTo(target)
            else scroll.animateScrollTo(target, tween(650, easing = FastOutSlowInEasing))
        }
    }

    // Work

    fun carTap(id: String) {
        val on = carOn[id] == true
        carOn[id] = !on
        if (!on) toast(ACTS.first { it.id == id }.toast)
    }

    // How I build

    fun longPress() {
        if (!editMode) {
            editMode = true
            toast("Edit mode. Tap two widgets to swap them.")
        }
    }

    fun doneEditing() {
        editMode = false
        selected = null
    }

    /** Returns true when the tap was handled by edit mode (no squash). */
    fun widgetTap(id: String): Boolean {
        if (!editMode) return false
        val sel = selected
        when (sel) {
            null -> selected = id
            id -> selected = null
            else -> {
                val i = order.indexOf(sel)
                val j = order.indexOf(id)
                order[i] = id
                order[j] = sel
                selected = null
            }
        }
        return true
    }

    // Contact

    fun copyEmail() {
        Browser.copy(Links.EMAIL)
        toast("Email copied to clipboard", "Undo")
    }

    // Footer

    fun buildTap() {
        if (dev) { toast("No need, you are already a developer."); return }
        val n = ++buildTaps
        if (n >= 7) {
            dev = true
            toast("You are now a developer!")
            fireConfetti()
        } else if (n >= 3) {
            val left = 7 - n
            toast("You are now $left step${if (left == 1) "" else "s"} away from being a developer.")
        }
    }

    private fun fireConfetti() {
        if (whimsy && !reduceMotion) confetti++
    }

    // Quick settings

    fun toggleDark() {
        toast(if (dark) "Light theme. Hello, sunshine." else "Dark theme on. Easy on the eyes.")
        dark = !dark; persist()
    }

    fun toggleWhimsy() {
        toast(if (whimsy) "Whimsy off. Calm, professional, slightly sad." else "Whimsy back on. Springs restored.")
        whimsy = !whimsy; persist()
    }

    fun toggleMotion() {
        toast(if (reduceMotion) "Motion back to normal." else "Reduce motion on. Everything just fades now.")
        reduceMotion = !reduceMotion; persist()
    }

    fun devTile() {
        if (dev) fireConfetti()
        toast(if (dev) "Developer options: confetti on demand." else "Locked. Tap Build number below 7 times. You know the drill.")
    }

    fun pickSeed(h: Float) {
        seed = h; persist()
        toast("Wallpaper set to ${SEEDS.first { it.hue == h }.name}. Re-theming everything.")
    }

    // 404

    fun anrWait() = toast("Still waiting. Bit is checking again.")

    fun anrClose() {
        Browser.goHome()
        screen = Screen.Site
    }

    companion object {
        const val K_SEED = "akshit-seed"
        const val K_DARK = "akshit-dark"
        const val K_WHIMSY = "akshit-whimsy"
        const val K_MOTION = "akshit-reduce-motion"
        const val K_BOOT = "akshit-boot-seen"
    }
}

val LocalApp = staticCompositionLocalOf<AppState> { error("AppState not provided") }
