package com.akshit.portfolio.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.theme.*
import isSmallScreen
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.Res
import portfolio.composeapp.generated.resources.email
import portfolio.composeapp.generated.resources.link_instagram
import portfolio.composeapp.generated.resources.link_linkedin
import portfolio.composeapp.generated.resources.link_buy_coffee

private const val LINKEDIN_LINK = "https://www.linkedin.com/in/akshit-nahata-06948a13b/"

@Composable
fun HomeFooter() {
    Footer()
}

@Composable
fun Footer(isContactPage: Boolean = false) {
    val isSm = isSmallScreen()
    val clipboardManager = LocalClipboardManager.current
    val email = stringResource(Res.string.email)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isContactPage) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 1400.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(48.dp),
                color = GlassWhite,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(if (isSm) 40.dp else 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "READY TO START?",
                        style = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            color = GlowPink,
                            letterSpacing = 4.sp
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "LET'S COLLABORATE",
                        style = TextStyle(
                            fontFamily = Syne,
                            fontWeight = FontWeight.Black,
                            fontSize = if (isSm) 32.sp else 72.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(48.dp))
                    Button(
                        onClick = { clipboardManager.setText(AnnotatedString(email)) },
                        colors = ButtonDefaults.buttonColors(containerColor = GlowIndigo),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(60.dp).widthIn(min = 200.dp)
                    ) {
                        Text(
                            text = "COPY EMAIL",
                            style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(80.dp))

        // Bottom Links
        Row(
            modifier = Modifier
                .widthIn(max = 1400.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "©2026 AKSHIT NAHATA",
                    style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextTertiary)
                )
                Text(
                    text = "BUILT WITH COMPOSE MULTIPLATFORM",
                    style = TextStyle(fontFamily = Inter, fontSize = 10.sp, color = GlowIndigo)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(if (isSm) 12.dp else 24.dp)) {
                SocialLink("LI", LINKEDIN_LINK)
                SocialLink("IG", stringResource(Res.string.link_instagram))
            }
        }
    }
}

@Composable
private fun SocialLink(label: String, url: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(GlassWhite)
            .border(1.dp, GlassBorder, CircleShape)
            .clickable { window.open(url, "_blank") },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Syne, 
                fontWeight = FontWeight.Black, 
                fontSize = if (label == "☕") 18.sp else 14.sp, 
                color = TextPrimary
            )
        )
    }
}
