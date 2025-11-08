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

val Antonio: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.Antonio_VariableFont_wght, FontWeight.Normal),
        Font(Res.font.Antonio_VariableFont_wght, FontWeight.Bold)
    )

val BebasNeue: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.BebasNeue_Regular, FontWeight.Normal)
    )

val Syne: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.Syne_VariableFont_wght, FontWeight.Normal),
        Font(Res.font.Syne_VariableFont_wght, FontWeight.Bold)
    )


val Georama: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.`Georama_VariableFont_wdth,wght`, FontWeight.Normal),
        Font(Res.font.`Georama_VariableFont_wdth,wght`, FontWeight.Medium),
        Font(Res.font.`Georama_VariableFont_wdth,wght`, FontWeight.Bold)
    )
