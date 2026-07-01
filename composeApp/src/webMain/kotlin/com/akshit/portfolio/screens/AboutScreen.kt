package com.akshit.portfolio.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
fun AboutMeScreen() {
    SetPageTitle("ABOUT")
    val isSm = isSmallScreen()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Shared engineering-grade background consistency
        EngineeringAboutBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = if (isSm) 120.dp else 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FadeIn {
                Column(
                    modifier = Modifier
                        .widthIn(max = 1400.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ArtisticAboutHero(isSm)
                    
                    Spacer(Modifier.height(if (isSm) 60.dp else 120.dp))
                    
                    WorkExperienceIntroSection(isSm)
                    
                    ExperienceTimelineShowcase(isSm)
                    
                    Footer()
                }
            }
        }
    }
}

@Composable
private fun EngineeringAboutBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize().graphicsLayer { alpha = 0.04f }) {
            val steps = 50.dp.toPx()
            for (i in 0..size.width.toInt() step steps.toInt()) {
                drawLine(Color.White, androidx.compose.ui.geometry.Offset(i.toFloat(), 0f), androidx.compose.ui.geometry.Offset(i.toFloat(), size.height))
            }
            for (i in 0..size.height.toInt() step steps.toInt()) {
                drawLine(Color.White, androidx.compose.ui.geometry.Offset(0f, i.toFloat()), androidx.compose.ui.geometry.Offset(size.width, i.toFloat()))
            }
        }

        Box(
            modifier = Modifier
                .size(if (isSmallScreen()) 600.dp else 1000.dp)
                .offset(x = 200.dp, y = (-200).dp)
                .graphicsLayer { rotationZ = rotation }
                .background(Brush.radialGradient(listOf(GlowPurple.copy(alpha = 0.12f), Color.Transparent)), CircleShape)
                .blur(150.dp)
        )
        Box(
            modifier = Modifier
                .size(if (isSmallScreen()) 500.dp else 800.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 100.dp)
                .graphicsLayer { rotationZ = -rotation }
                .background(Brush.radialGradient(listOf(GlowIndigo.copy(alpha = 0.1f), Color.Transparent)), CircleShape)
                .blur(120.dp)
        )
    }
}

@Composable
private fun ArtisticAboutHero(isSm: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isSm) Alignment.Center else Alignment.TopStart
    ) {
        // IMAGE AS BACKDROP
        Surface(
            modifier = Modifier
                .fillMaxWidth(if (isSm) 0.85f else 0.5f)
                .aspectRatio(1f)
                .align(if (isSm) Alignment.Center else Alignment.TopEnd)
                .graphicsLayer { 
                    rotationZ = if (isSm) 5f else 10f
                    alpha = if (isSm) 0.4f else 0.7f 
                },
            shape = RoundedCornerShape(if (isSm) 32.dp else 64.dp),
            color = Color.White.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(Res.drawable.authCloseImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.85f)
                        .clip(RoundedCornerShape(if (isSm) 24.dp else 48.dp))
                )
            }
        }

        // TEXT OVERLAPPING IMAGE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isSm) 0.dp else 60.dp),
            horizontalAlignment = if (isSm) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                text = "I BUILD",
                style = TextStyle(
                    fontFamily = Syne,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = if (isSm) 32.sp else 80.sp,
                    color = TextPrimary,
                    letterSpacing = if (isSm) 2.sp else 4.sp // REDUCED
                ),
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = "DIGITAL",
                style = TextStyle(
                    fontFamily = Syne,
                    fontWeight = FontWeight.Black,
                    fontSize = if (isSm) 48.sp else 120.sp,
                    color = GlowPink,
                    lineHeight = if (isSm) 44.sp else 110.sp,
                    letterSpacing = (-2).sp // REDUCED
                ),
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = "EXPERIENCES",
                style = TextStyle(
                    fontFamily = Syne,
                    fontWeight = FontWeight.Black,
                    fontSize = if (isSm) 40.sp else 100.sp,
                    color = TextPrimary,
                    lineHeight = if (isSm) 38.sp else 90.sp,
                    letterSpacing = (-1).sp // REDUCED
                ),
                maxLines = 1,
                softWrap = false
            )
            
            Spacer(Modifier.height(40.dp))
            
            Surface(
                color = GlowIndigo.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GlowIndigo.copy(alpha = 0.3f))
            ) {
            Text(
                text = stringResource(Res.string.about_location).uppercase(),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    color = GlowIndigo,
                    letterSpacing = 2.sp,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
            }
        }
    }
}

@Composable
private fun WorkExperienceIntroSection(isSm: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isSm) 40.dp else 80.dp)
    ) {
        Text(
            text = "CAREER_TIMELINE",
            style = TextStyle(
                fontFamily = Syne,
                fontWeight = FontWeight.Black,
                fontSize = if (isSm) 28.sp else 64.sp,
                color = TextPrimary,
                letterSpacing = (-1).sp // REDUCED
            ),
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.height(if (isSm) 16.dp else 24.dp))
        Text(
            text = stringResource(Res.string.about_work_exp_desc),
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.Light,
                fontSize = if (isSm) 16.sp else 28.sp,
                color = TextSecondary,
                lineHeight = if (isSm) 24.sp else 44.sp
            ),
            softWrap = true
        )
    }
}

@Composable
private fun ExperienceTimelineShowcase(isSm: Boolean) {
    val jlrKey = "jlr"
    var expandedItem by remember { mutableStateOf<String?>(jlrKey) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp)) {
        listOf(
            ExperienceData("01", stringResource(Res.string.exp_1_title), stringResource(Res.string.exp_1_company), stringResource(Res.string.exp_1_date), stringResource(Res.string.exp_desc_jlr), GlowIndigo, jlrKey),
            ExperienceData("02", stringResource(Res.string.exp_2_title), stringResource(Res.string.exp_2_company), stringResource(Res.string.exp_2_date), stringResource(Res.string.exp_desc_ids), GlowPurple, "ids"),
            ExperienceData("03", stringResource(Res.string.exp_3_title), stringResource(Res.string.exp_3_company), stringResource(Res.string.exp_3_date), stringResource(Res.string.exp_desc_freelance), GlowPink, "freelance")
        ).forEach { data ->
            ArtisticTimelineItem(
                isSm = isSm,
                data = data,
                isExpanded = expandedItem == data.key,
                onClick = { expandedItem = if (expandedItem == data.key) null else data.key }
            )
        }
    }
}

@Composable
private fun ArtisticTimelineItem(
    isSm: Boolean,
    data: ExperienceData,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isExpanded) 1.01f else 1f)
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(if (isSm) 24.dp else 32.dp),
        color = if (isExpanded) GlassWhite else Color.Transparent,
        border = BorderStroke(1.dp, if (isExpanded) data.accent.copy(alpha = 0.3f) else GlassBorder),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(if (isSm) 20.dp else 48.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FIXED WIDTH DATE SECTION (Aligned with Home Screen: 130dp/300dp)
                Surface(
                    modifier = Modifier.width(if (isSm) 120.dp else 280.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = data.accent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, data.accent.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = data.date,
                        style = TextStyle(
                            fontFamily = Inter, 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = if (isSm) 14.sp else 24.sp, 
                            color = data.accent, 
                            letterSpacing = 0.5.sp, // REDUCED
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(vertical = if (isSm) 12.dp else 16.dp),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Spacer(Modifier.width(if (isSm) 20.dp else 48.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = data.title.uppercase(),
                        style = TextStyle(
                            fontFamily = Syne,
                            fontWeight = FontWeight.Black,
                            fontSize = if (isSm) 20.sp else 44.sp,
                            color = TextPrimary,
                            lineHeight = if (isSm) 24.sp else 48.sp,
                            letterSpacing = (-0.5).sp // REDUCED
                        ),
                        maxLines = 2,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = data.company,
                        style = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isSm) 14.sp else 24.sp,
                            color = GlowCyan,
                            letterSpacing = 0.5.sp // REDUCED
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 32.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassBorder))
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = data.description,
                        style = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Light,
                            fontSize = if (isSm) 15.sp else 22.sp,
                            lineHeight = if (isSm) 24.sp else 36.sp,
                            color = TextPrimary
                        ),
                        softWrap = true
                    )
                }
            }
        }
    }
}

private data class ExperienceData(
    val id: String,
    val title: String,
    val company: String,
    val date: String,
    val description: String,
    val accent: Color,
    val key: String
)
