package com.akshit.portfolio.theme

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

/** The README hex table (hue 145, light and dark) is the oracle for the OKLCH conversion. */
class OklchTest {
    private fun channels(hex: String) = listOf(1, 3, 5).map { hex.substring(it, it + 2).toInt(16) }

    private fun check(name: String, actual: String, expected: String) {
        val a = channels(actual)
        val e = channels(expected)
        assertTrue(a.zip(e).all { (x, y) -> abs(x - y) <= 2 }, "$name: got $actual, expected $expected")
    }

    @Test
    fun lightRolesMatchTheTable() {
        val r = roles(145f, dark = false)
        check("p", r.p.toHex(), "#007c00")
        check("op", r.op.toHex(), "#f8fef8")
        check("pc", r.pc.toHex(), "#bcf2bd")
        check("opc", r.opc.toHex(), "#003104")
        check("sec", r.sec.toHex(), "#d5ecd5")
        check("osec", r.osec.toHex(), "#182f19")
        check("t", r.t.toHex(), "#007ca1")
        check("tc", r.tc.toHex(), "#9fecff")
        check("otc", r.otc.toHex(), "#003244")
        check("s", r.s.toHex(), "#f7fcf7")
        check("sc1", r.sc1.toHex(), "#ecf4ec")
        check("sc2", r.sc2.toHex(), "#e2ede1")
        check("sc3", r.sc3.toHex(), "#d2e4d2")
        check("os", r.os.toHex(), "#131b13")
        check("osv", r.osv.toHex(), "#465446")
        check("ol", r.ol.toHex(), "#99aa99")
        check("inv", r.inv.toHex(), "#202920")
        check("oinv", r.oinv.toHex(), "#eaf0ea")
    }

    @Test
    fun darkRolesMatchTheTable() {
        val r = roles(145f, dark = true)
        check("p", r.p.toHex(), "#8cda8f")
        check("op", r.op.toHex(), "#002e02")
        check("pc", r.pc.toHex(), "#005211")
        check("opc", r.opc.toHex(), "#d0f3d0")
        check("sec", r.sec.toHex(), "#213321")
        check("osec", r.osec.toHex(), "#d9ead9")
        check("t", r.t.toHex(), "#4cd1ee")
        check("tc", r.tc.toHex(), "#004e63")
        check("otc", r.otc.toHex(), "#bbf3ff")
        check("s", r.s.toHex(), "#0b100b")
        check("sc1", r.sc1.toHex(), "#121812")
        check("sc2", r.sc2.toHex(), "#182118")
        check("sc3", r.sc3.toHex(), "#212c21")
        check("os", r.os.toHex(), "#e3eae3")
        check("osv", r.osv.toHex(), "#aebcae")
        check("ol", r.ol.toHex(), "#596859")
        check("inv", r.inv.toHex(), "#e0e7e0")
        check("oinv", r.oinv.toHex(), "#151d15")
    }

    @Test
    fun seedPrimariesMatchTheTable() {
        val light = mapOf(295f to "#6f41c1", 45f to "#b32a00", 240f to "#0069c2", 350f to "#ab1d72")
        val dark = mapOf(295f to "#cab3ff", 45f to "#ffa87b", 240f to "#6dcfff", 350f to "#ffa0d0")
        light.forEach { (h, hex) -> check("light p @$h", roles(h, false).p.toHex(), hex) }
        dark.forEach { (h, hex) -> check("dark p @$h", roles(h, true).p.toHex(), hex) }
        val swatch = mapOf(145f to "#1fa836", 295f to "#976ef1", 45f to "#e55b00", 240f to "#0096f2", 350f to "#db509c")
        swatch.forEach { (h, hex) -> check("swatch @$h", oklch(.64f, .19f, h).toHex(), hex) }
    }
}
