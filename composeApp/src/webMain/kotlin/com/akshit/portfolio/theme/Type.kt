package com.akshit.portfolio.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import portfolio.composeapp.generated.resources.*

val Inter: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.`Inter_VariableFont_opsz,wght`, FontWeight.Normal),
        Font(Res.font.`Inter_VariableFont_opsz,wght`, FontWeight.Medium),
        Font(Res.font.`Inter_VariableFont_opsz,wght`, FontWeight.SemiBold),
        Font(Res.font.`Inter_VariableFont_opsz,wght`, FontWeight.Bold),
        Font(Res.font.`Inter_VariableFont_opsz,wght`, FontWeight.ExtraBold),
        Font(Res.font.`Inter_VariableFont_opsz,wght`, FontWeight.Black)
    )

val Syne: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.Syne_VariableFont_wght, FontWeight.Normal),
        Font(Res.font.Syne_VariableFont_wght, FontWeight.Bold),
        Font(Res.font.Syne_VariableFont_wght, FontWeight.ExtraBold)
    )

// Adding a Mono font if available, if not use Inter Medium with spacing for "Tech" feel
val TechMono: FontFamily
    @Composable
    get() = Inter // Fallback to Inter for now, but styled like mono

@Composable
fun getTypography() = Typography(
    displayLarge = TextStyle(
        fontFamily = Syne,
        fontWeight = FontWeight.Black,
        fontSize = 120.sp,
        lineHeight = 110.sp,
        letterSpacing = (-4).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Syne,
        fontWeight = FontWeight.Bold,
        fontSize = 80.sp,
        lineHeight = 84.sp,
        letterSpacing = (-2).sp
    ),
    displaySmall = TextStyle(
        fontFamily = Syne,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 2.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 2.sp
    )
)
