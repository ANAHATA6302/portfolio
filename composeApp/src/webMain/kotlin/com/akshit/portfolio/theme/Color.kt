package com.akshit.portfolio.theme

import androidx.compose.ui.graphics.Color

/**
 * Engineering-Grade Vibrant Dark Theme
 * Refined for Atmospheric Depth and Contrast
 */
val BackgroundDark = Color(0xFF0D1117) // Midnight Navy Abyss
val SurfaceDark = Color(0xFF161B22)
val SurfaceVariantDark = Color(0xFF21262D)

// Neon Signal Colors
val GlowIndigo = Color(0xFF6366F1)
val GlowPurple = Color(0xFF8B5CF6)
val GlowPink = Color(0xFFEC4899)
val GlowCyan = Color(0xFF06B6D4)
val GlowAmber = Color(0xFFF59E0B)

// Text Hierarchy
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF475569)

// Refined Glass System
val GlassWhite = Color(0xFFFFFFFF).copy(alpha = 0.05f) 
val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.12f)

// Core Brand Logic
val Primary = GlowIndigo
val Secondary = GlowPink
val Accent = GlowPink
val Background = BackgroundDark
val Surface = SurfaceDark

// Legacy compatibility
val AppWhite = Color(0xFFFFFFFF)
val AppBlack = Color(0xFF000000)
val AppGreen = GlowIndigo
val AppLGreen = GlowCyan
val AppYellow = GlowAmber
val AppBBlue = SurfaceDark
val AppBPink = GlowPink
val AppBRed = Color(0xFFFFE5E5)
val AppDark = SurfaceDark
val AppDBlue = Color(0xFFABADDD)
val AppGray = Color(0xFFC7C7C7)
val AppBorderGray = Color(0xFF81ADA7)
