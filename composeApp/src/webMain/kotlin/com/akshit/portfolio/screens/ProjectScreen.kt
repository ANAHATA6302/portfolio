package com.akshit.portfolio.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
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
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
private fun Modifier.rimLight(): Modifier = this.drawWithContent {
    drawContent()
    drawLine(
        brush = Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.2f), Color.Transparent)),
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
        strokeWidth = 1.dp.toPx()
    )
}

@Composable
fun ProjectsScreen() {
    SetPageTitle("WORKS")
    val isSm = isSmallScreen()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // High-end Technical Background
        EngineeringArchitecturalBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FadeIn {
                Column(
                    modifier = Modifier
                        .widthIn(max = 1400.dp)
                        .fillMaxWidth()
                        .padding(horizontal = if (isSm) 20.dp else 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(if (isSm) 100.dp else 180.dp))
                    
                    TechnicalHeroSection(isSm)
                    
                    Spacer(Modifier.height(if (isSm) 60.dp else 140.dp))

                    TechnicalProjectGallery(isSm)

                    Footer()
                }
            }
        }
    }
}

@Composable
private fun EngineeringArchitecturalBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Global Technical Grid
        Canvas(modifier = Modifier.fillMaxSize().graphicsLayer { alpha = 0.05f }) {
            val gridSpacing = 50.dp.toPx()
            for (x in 0..size.width.toInt() step gridSpacing.toInt()) {
                drawLine(Color.White, Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height), strokeWidth = 1f)
            }
            for (y in 0..size.height.toInt() step gridSpacing.toInt()) {
                drawLine(Color.White, Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()), strokeWidth = 1f)
            }
        }

        // Tri-Lighting Setup (Boosted Glows)
        Box(
            modifier = Modifier
                .size(if (isSmallScreen()) 600.dp else 1200.dp)
                .offset(x = (-300).dp, y = (-200).dp)
                .graphicsLayer { rotationZ = rotation }
                .background(Brush.radialGradient(listOf(GlowIndigo.copy(alpha = 0.2f), Color.Transparent)), CircleShape)
                .blur(150.dp)
        )
        Box(
            modifier = Modifier
                .size(if (isSmallScreen()) 500.dp else 1000.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 200.dp, y = 200.dp)
                .graphicsLayer { rotationZ = -rotation }
                .background(Brush.radialGradient(listOf(GlowPink.copy(alpha = 0.15f), Color.Transparent)), CircleShape)
                .blur(120.dp)
        )
        // Third Glow Source (Cyan)
        Box(
            modifier = Modifier
                .size(if (isSmallScreen()) 400.dp else 800.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 100.dp)
                .background(Brush.radialGradient(listOf(GlowCyan.copy(alpha = 0.12f), Color.Transparent)), CircleShape)
                .blur(100.dp)
        )
    }
}

@Composable
private fun TechnicalHeroSection(isSm: Boolean) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        // Background Decorative Text - Reduced letter spacing for better fit
        Text(
            text = "ENGINEER",
            style = TextStyle(
                fontFamily = Syne,
                fontWeight = FontWeight.Black,
                fontSize = if (isSm) 50.sp else 220.sp, 
                color = Color.White.copy(alpha = 0.02f),
                letterSpacing = (-4).sp // Tighter for Syne Black
            ),
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.offset(y = if (isSm) (-15).dp else (-60).dp)
        )

        Column {
            Text(
                text = "01 / ARCHITECTURE",
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Black,
                    fontSize = if (isSm) 10.sp else 16.sp,
                    color = GlowIndigo,
                    letterSpacing = 2.sp // Reduced from 4
                ),
                maxLines = 1,
                softWrap = false
            )
            Spacer(Modifier.height(if (isSm) 8.dp else 16.dp))
            Text(
                text = "SELECTED\nWORKS.",
                style = TextStyle(
                    fontFamily = Syne,
                    fontWeight = FontWeight.Black,
                    fontSize = if (isSm) 36.sp else 140.sp, 
                    lineHeight = if (isSm) 30.sp else 130.sp,
                    color = TextPrimary,
                    letterSpacing = (-2).sp // Tighter gap
                ),
                maxLines = 2,
                softWrap = false
            )
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .width(if (isSm) 80.dp else 400.dp)
                    .height(2.dp)
                    .background(Brush.linearGradient(listOf(GlowIndigo, GlowPink, Color.Transparent)))
            )
        }
    }
}

@Composable
private fun TechnicalProjectGallery(isSm: Boolean) {
    val projects = listOf(
        ProjectArtInfo(
            "01",
            stringResource(Res.string.project_one_app_title),
            "ENTERPRISE_ARCHITECTURE",
            stringResource(Res.string.project_one_app_desc),
            listOf("KOTLIN", "JETPACK COMPOSE", "MVI", "HILT"),
            GlowIndigo
        ),
        ProjectArtInfo(
            "02",
            stringResource(Res.string.project_level_title),
            "E-COMMERCE_ENGINE",
            stringResource(Res.string.project_level_desc),
            listOf("ANDROID SDK", "FIREBASE", "G-PAY"),
            GlowPurple
        ),
        ProjectArtInfo(
            "03",
            stringResource(Res.string.project_portfolio_title),
            "KMP_VISUALS",
            stringResource(Res.string.project_portfolio_desc),
            listOf("KOTLIN MULTIPLATFORM", "COMPOSE WEB", "WASM"),
            GlowPink
        ),
        ProjectArtInfo(
            "04",
            stringResource(Res.string.project_potterpedia_title),
            "CLEAN_ARCHITECTURE",
            stringResource(Res.string.project_potterpedia_desc),
            listOf("OFFLINE-FIRST", "API INTEGRATION", "MVVM"),
            GlowCyan
        ),
        ProjectArtInfo(
            "05",
            stringResource(Res.string.project_unexpectedly_title),
            "SOCIAL_EXPERIENCE",
            stringResource(Res.string.project_unexpectedly_desc),
            listOf("WEBRTC", "EXOPLAYER", "REAL-TIME"),
            GlowAmber
        ),
        ProjectArtInfo(
            "06",
            stringResource(Res.string.project_truth_or_dare_title),
            "INTERACTIVE_SYSTEM",
            stringResource(Res.string.project_truth_or_dare_desc),
            listOf("GAME LOGIC", "UI MOTION", "INTERACTIVE"),
            GlowIndigo
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isSm) 40.dp else 120.dp),
        verticalArrangement = Arrangement.spacedBy(if (isSm) 40.dp else 100.dp)
    ) {
        projects.forEach { project ->
            HighEndWorkCard(project, isSm)
        }
    }
}

@Composable
private fun HighEndWorkCard(project: ProjectArtInfo, isSm: Boolean) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isHovered) 1.01f else 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .hoverable(remember { MutableInteractionSource() })
            .clickable { },
        shape = RoundedCornerShape(if (isSm) 24.dp else 48.dp),
        color = GlassWhite,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Row(
            modifier = Modifier
                .padding(if (isSm) 24.dp else 64.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text(
                    text = project.tag,
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Black,
                        color = project.accent,
                        fontSize = if (isSm) 10.sp else 12.sp,
                        letterSpacing = 1.sp // Reduced from 2
                    ),
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = project.title.uppercase(),
                    style = TextStyle(
                        fontFamily = Syne,
                        fontWeight = FontWeight.Black,
                        fontSize = if (isSm) 24.sp else 54.sp, 
                        color = TextPrimary,
                        lineHeight = if (isSm) 28.sp else 58.sp,
                        letterSpacing = (-1).sp
                    ),
                    maxLines = 2,
                    softWrap = true
                )

                Spacer(Modifier.height(if (isSm) 16.dp else 32.dp))

                Text(
                    text = project.desc,
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Light,
                        fontSize = if (isSm) 14.sp else 22.sp,
                        color = TextSecondary,
                        lineHeight = if (isSm) 20.sp else 36.sp
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(if (isSm) 20.dp else 40.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    project.stack.forEach { tech ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = project.accent.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, project.accent.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = tech,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                style = TextStyle(
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isSm) 8.sp else 10.sp,
                                    color = project.accent,
                                    letterSpacing = 0.5.sp // Reduced from 1
                                )
                            )
                        }
                    }
                }
            }

            if (!isSm) {
                // LARGE NUMBER ON THE RIGHT
                Text(
                    text = project.id,
                    style = TextStyle(
                        fontFamily = Syne,
                        fontWeight = FontWeight.Black,
                        fontSize = 180.sp,
                        color = project.accent.copy(alpha = 0.1f),
                        letterSpacing = (-10).sp // Tighter for display IDs
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 40.dp),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

private data class ProjectArtInfo(
    val id: String,
    val title: String,
    val tag: String,
    val desc: String,
    val stack: List<String>,
    val accent: Color
)
