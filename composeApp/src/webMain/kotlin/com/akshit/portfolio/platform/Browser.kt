package com.akshit.portfolio.platform

import kotlinx.browser.localStorage
import kotlinx.browser.window

/** Thin, exception-safe wrappers over the browser APIs the site uses. */
@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
object Browser {
    fun get(key: String): String? = try { localStorage.getItem(key) } catch (_: Throwable) { null }

    fun set(key: String, value: String) {
        try { localStorage.setItem(key, value) } catch (_: Throwable) {}
    }

    fun media(query: String): Boolean = try { window.matchMedia(query).matches } catch (_: Throwable) { false }

    fun copy(text: String) {
        try { window.navigator.clipboard.writeText(text).catch { null } } catch (_: Throwable) {}
    }

    fun open(url: String) {
        try { window.open(url, "_blank") } catch (_: Throwable) {}
    }

    val path: String get() = try { window.location.pathname } catch (_: Throwable) { "/" }

    fun goHome() {
        try { window.history.replaceState(null, "", "/") } catch (_: Throwable) {}
    }
}
