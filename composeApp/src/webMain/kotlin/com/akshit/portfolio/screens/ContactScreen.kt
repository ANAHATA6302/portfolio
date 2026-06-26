package com.akshit.portfolio.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.common.FadeIn
import com.akshit.portfolio.common.Footer
import com.akshit.portfolio.common.SetPageTitle
import com.akshit.portfolio.theme.*
import isSmallScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
fun ContactScreen() {
    SetPageTitle("CONNECT")
    val isSm = isSmallScreen()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Unique Contact Glows
        ContactBackgroundGlows()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = if (isSm) 60.dp else 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FadeIn {
                Column(
                    modifier = Modifier
                        .widthIn(max = 1200.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ArtisticContactHero(isSm)
                    Footer(isContactPage = true)
                }
            }
        }
    }
}

@Composable
private fun ContactBackgroundGlows() {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(700.dp)
                .align(Alignment.Center)
                .graphicsLayer { scaleX = pulse; scaleY = pulse }
                .background(Brush.radialGradient(listOf(GlowPink.copy(alpha = 0.08f), Color.Transparent)), CircleShape)
                .blur(100.dp)
        )
    }
}

@Composable
private fun ArtisticContactHero(isSm: Boolean) {
    val sectionModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = if (isSm) 60.dp else 120.dp)

    if (isSm) {
        Column(
            modifier = sectionModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(40.dp))
                    .background(GlassWhite)
                    .border(1.dp, GlassBorder, RoundedCornerShape(40.dp))
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.blazerAuthor),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)).rotate(5f)
                )
            }

            ContactHeroTypography(isSm = true)

            ArtisticEmailCta(isSm = true)
        }
    } else {
        Row(
            modifier = sectionModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1.2f),
                verticalArrangement = Arrangement.spacedBy(60.dp)
            ) {
                ContactHeroTypography(isSm = false)
                ArtisticEmailCta(isSm = false)
            }

            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .aspectRatio(0.8f)
                    .graphicsLayer { rotationZ = 5f }
                    .clip(RoundedCornerShape(60.dp))
                    .background(GlassWhite)
                    .border(1.dp, GlassBorder, RoundedCornerShape(60.dp))
                    .padding(24.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.blazerAuthor),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(36.dp))
                )
            }
        }
    }
}

@Composable
private fun ContactHeroTypography(isSm: Boolean) {
    Column {
        Text(
            text = "LET'S BUILD",
            style = TextStyle(
                fontFamily = Syne,
                fontWeight = FontWeight.Black,
                fontSize = if (isSm) 56.sp else 110.sp,
                color = TextPrimary,
                lineHeight = if (isSm) 52.sp else 100.sp
            )
        )
        Text(
            text = "SOMETHING",
            style = TextStyle(
                fontFamily = Syne,
                fontWeight = FontWeight.ExtraLight,
                fontSize = if (isSm) 48.sp else 100.sp,
                color = GlowIndigo,
                letterSpacing = if (isSm) 4.sp else 12.sp
            ),
            modifier = Modifier.offset(y = if (isSm) (-10).dp else (-20).dp)
        )
        Text(
            text = "ICONIC.",
            style = TextStyle(
                fontFamily = Syne,
                fontWeight = FontWeight.Black,
                fontSize = if (isSm) 56.sp else 110.sp,
                color = GlowPink,
                lineHeight = if (isSm) 52.sp else 100.sp
            ),
            modifier = Modifier.offset(y = if (isSm) (-20).dp else (-40).dp)
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = stringResource(Res.string.contact_hero_text),
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.Light,
                fontSize = if (isSm) 18.sp else 28.sp,
                color = TextSecondary,
                lineHeight = if (isSm) 28.sp else 42.sp
            ),
            textAlign = if (isSm) TextAlign.Center else TextAlign.Start,
            modifier = Modifier.widthIn(max = 600.dp)
        )
    }
}

@Composable
private fun ArtisticEmailCta(isSm: Boolean) {
    val clipboardManager = LocalClipboardManager.current
    val emailString = stringResource(Res.string.email)
    var isCopied by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .clickable { 
                clipboardManager.setText(AnnotatedString(emailString))
                isCopied = true
            },
        shape = RoundedCornerShape(24.dp),
        color = GlassWhite,
        border = BorderStroke(1.dp, GlowIndigo)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.email),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isCopied) GlowPink else GlowIndigo
            )
            Column {
                Text(
                    text = if (isCopied) "COPIED TO CLIPBOARD" else "GET IN TOUCH",
                    style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GlowPink, letterSpacing = 2.sp)
                )
                Text(
                    text = emailString,
                    style = TextStyle(
                        fontFamily = Syne, 
                        fontWeight = FontWeight.Bold, 
                        fontSize = if (isSm) 18.sp else 24.sp, 
                        color = TextPrimary
                    )
                )
            }
        }
    }
}
