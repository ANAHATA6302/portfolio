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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akshit.portfolio.common.FadeIn
import com.akshit.portfolio.common.HomeFooter
import com.akshit.portfolio.common.SetPageTitle
import com.akshit.portfolio.navigation.ProjectsScreen
import com.akshit.portfolio.theme.*
import isSmallScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
fun HomeScreen() {
    SetPageTitle("ENGINEER")
    val isSm = isSmallScreen()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        ArchitecturalBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = if (isSm) 120.dp else 160.dp), // INCREASED PADDING FROM HEADER
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
                    BentoTechnicalHero(isSm)
                    
                    Spacer(Modifier.height(if (isSm) 60.dp else 120.dp))
                    
                    LiveTechConsole(isSm)

                    Spacer(Modifier.height(if (isSm) 100.dp else 200.dp))
                    
                    EngineeringWorkShowcase(isSm)

                    Spacer(Modifier.height(if (isSm) 100.dp else 200.dp))

                    ArchitectureTimeline(isSm)

                    HomeFooter()
                    
                    Spacer(Modifier.height(80.dp)) // ADDED BOTTOM PADDING
                }
            }
        }
    }
}

@Composable
private fun ArchitecturalBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        val infiniteTransition = rememberInfiniteTransition()
        val scanY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Canvas(modifier = Modifier.fillMaxSize().graphicsLayer { alpha = 0.03f }) {
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
                .fillMaxWidth()
                .fillMaxHeight(0.01f)
                .align(Alignment.TopCenter)
                .offset(y = 1000.dp * scanY)
                .background(Brush.verticalGradient(listOf(Color.Transparent, GlowIndigo.copy(alpha = 0.1f), Color.Transparent)))
        )

        Box(
            modifier = Modifier
                .size(800.dp)
                .offset(x = (-300).dp, y = (-200).dp)
                .background(Brush.radialGradient(listOf(GlowIndigo.copy(alpha = 0.08f), Color.Transparent)), CircleShape)
                .blur(100.dp)
        )
    }
}

@Composable
private fun BentoTechnicalHero(isSm: Boolean) {
    if (isSm) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HeroMainCard(isSm)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HeroTechnicalSpecCard("OS_TYPE", "ANDROID_14", GlowCyan, Modifier.weight(1f))
                HeroTechnicalSpecCard("CORE", "KOTLIN", GlowPink, Modifier.weight(1f))
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth().height(600.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HeroMainCard(isSm, Modifier.weight(2.5f))
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                HeroTechnicalSpecCard("TARGET_SDK", "34", GlowCyan, Modifier.weight(1f))
                HeroTechnicalSpecCard("CORE_STACK", "KOTLIN", GlowPink, Modifier.weight(1f))
                HeroTechnicalSpecCard("UI_ENGINE", "COMPOSE", GlowIndigo, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HeroMainCard(isSm: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(if (isSm) 32.dp else 48.dp),
        color = GlassWhite,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Box(modifier = Modifier.padding(if (isSm) 32.dp else 64.dp)) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = "0x01 // SYSTEM_INIT",
                    style = TextStyle(
                        fontFamily = Inter, 
                        fontWeight = FontWeight.Black, 
                        fontSize = if (isSm) 10.sp else 12.sp, 
                        color = GlowIndigo, 
                        letterSpacing = 1.sp // REDUCED
                    ),
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(Modifier.height(if (isSm) 16.dp else 24.dp))
                Text(
                    text = "AKSHIT\nNAHATA",
                    style = TextStyle(
                        fontFamily = Syne,
                        fontWeight = FontWeight.Black,
                        fontSize = if (isSm) 44.sp else 120.sp,
                        lineHeight = if (isSm) 42.sp else 110.sp,
                        color = TextPrimary,
                        letterSpacing = (-2).sp // REDUCED
                    ),
                    maxLines = 2,
                    softWrap = false
                )
                Spacer(Modifier.height(if (isSm) 16.dp else 24.dp))
                Text(
                    text = "ANDROID ENGINEER BUILDING SCALABLE\nARCHITECTURE FOR THE NEXT GENERATION OF MOBILE.",
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Light,
                        fontSize = if (isSm) 13.sp else 20.sp,
                        color = TextSecondary,
                        lineHeight = if (isSm) 20.sp else 32.sp
                    ),
                    maxLines = 3,
                    softWrap = true
                )
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(if (isSm) 80.dp else 200.dp)
                    .graphicsLayer { rotationZ = 45f }
                    .border(1.dp, GlowIndigo.copy(alpha = 0.3f), RoundedCornerShape(if (isSm) 16.dp else 24.dp))
                    .padding(if (isSm) 12.dp else 16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(GlowIndigo.copy(alpha = 0.05f), RoundedCornerShape(if (isSm) 8.dp else 12.dp))
                )
            }
        }
    }
}

@Composable
private fun HeroTechnicalSpecCard(label: String, value: String, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(if (isSmallScreen()) 24.dp else 32.dp),
        color = GlassWhite,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(if (isSmallScreen()) 16.dp else 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = Inter, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = if (isSmallScreen()) 8.sp else 10.sp, 
                    color = TextTertiary, 
                    letterSpacing = 1.sp // REDUCED
                ),
                maxLines = 1,
                softWrap = false
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = Syne, 
                    fontWeight = FontWeight.Black, 
                    fontSize = if (isSmallScreen()) 18.sp else 28.sp, 
                    color = color,
                    letterSpacing = (-0.5).sp // REDUCED
                ),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun LiveTechConsole(isSm: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = if (isSm) 20.dp else 40.dp),
        shape = RoundedCornerShape(if (isSm) 16.dp else 24.dp),
        color = Color.Black.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Row(
            modifier = Modifier.padding(if (isSm) 16.dp else 24.dp).horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(if (isSm) 24.dp else 48.dp)
        ) {
            listOf("ARCHITECTURE: MVVM/MVI", "DEP_INJECTION: KOTLIN", "ASYNC: COROUTINES", "NETWORK: WIREMOCK/KTOR").forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(GlowCyan, CircleShape))
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = item,
                        style = TextStyle(
                            fontFamily = Inter, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = if (isSm) 12.sp else 14.sp, 
                            color = TextSecondary
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun EngineeringWorkShowcase(isSm: Boolean) {
    val navigator = LocalNavigator.currentOrThrow
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "SELECTED_REPOS",
            style = TextStyle(
                fontFamily = Syne, 
                fontWeight = FontWeight.Black, 
                fontSize = if (isSm) 32.sp else 80.sp, 
                color = TextPrimary,
                letterSpacing = (-1).sp // REDUCED
            ),
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.height(if (isSm) 32.dp else 80.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(if (isSm) 32.dp else 80.dp)) {
            WorkTechnicalCard(
                title = stringResource(Res.string.project_one_app_title),
                tag = "ENTERPRISE",
                desc = stringResource(Res.string.project_one_app_desc),
                img = Res.drawable.jlrLogo,
                accent = GlowIndigo,
                isSm = isSm,
                useHighContrastLogo = true 
            ) { navigator.push(ProjectsScreen) }

            WorkTechnicalCard(
                title = stringResource(Res.string.project_roamio_title),
                tag = "FLAGSHIP",
                desc = stringResource(Res.string.project_roamio_desc),
                img = Res.drawable.projecticon1,
                accent = GlowCyan,
                isSm = isSm,
                useHighContrastLogo = true 
            ) { navigator.push(ProjectsScreen) }

            WorkTechnicalCard(
                title = stringResource(Res.string.project_portfolio_title),
                tag = "COMPOSE_WEB",
                desc = stringResource(Res.string.project_portfolio_desc),
                img = Res.drawable.portfolioIcon,
                accent = GlowPink,
                isSm = isSm,
                useHighContrastLogo = true 
            ) { navigator.push(ProjectsScreen) }
        }
    }
}

@Composable
private fun WorkTechnicalCard(
    title: String,
    tag: String,
    desc: String,
    img: DrawableResource,
    accent: Color,
    isSm: Boolean,
    useHighContrastLogo: Boolean = false, 
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isSm) 400.dp else 650.dp)
            .clip(RoundedCornerShape(if (isSm) 32.dp else 48.dp))
            .background(GlassWhite)
            .border(1.dp, GlassBorder, RoundedCornerShape(if (isSm) 32.dp else 48.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(if (isSm) 24.dp else 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Surface(
                    color = accent.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, accent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = tag, 
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), 
                        style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = if (isSm) 9.sp else 10.sp, color = accent),
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(Modifier.height(if (isSm) 16.dp else 24.dp))
                Text(
                    text = title.uppercase(), 
                    style = TextStyle(
                        fontFamily = Syne, 
                        fontWeight = FontWeight.Black, 
                        fontSize = if (isSm) 28.sp else 64.sp, 
                        color = TextPrimary,
                        letterSpacing = (-1).sp // REDUCED
                    ),
                    maxLines = 2,
                    softWrap = true // FIX: Enabled wrapping
                )
                Spacer(Modifier.height(if (isSm) 12.dp else 16.dp))
                Text(
                    text = desc, 
                    style = TextStyle(
                        fontFamily = Inter, 
                        fontWeight = FontWeight.Light, 
                        fontSize = if (isSm) 14.sp else 22.sp, 
                        color = TextSecondary, 
                        lineHeight = if (isSm) 20.sp else 32.sp
                    ),
                    maxLines = 3,
                    softWrap = true, // FIX: Enabled wrapping
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(Modifier.height(if (isSm) 32.dp else 48.dp))
                
                Text(
                    text = "VIEW_SOURCE →", 
                    style = TextStyle(
                        fontFamily = Inter, 
                        fontWeight = FontWeight.Black, 
                        fontSize = if (isSm) 12.sp else 14.sp, 
                        color = accent, 
                        letterSpacing = 1.sp // REDUCED
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
            if (!isSm) {
                // FIX: Enhanced backdrop for portfolio image
                Surface(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxHeight(0.7f) // FIX: Increased size
                        .graphicsLayer { rotationZ = -5f },
                    shape = RoundedCornerShape(40.dp), // FIX: Larger corner radius
                    color = Color.White.copy(alpha = 0.1f), // FIX: More prominent backdrop
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)) // FIX: Brighter border
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(img),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.85f) // FIX: Increased image scale
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchitectureTimeline(isSm: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = if (isSm) 60.dp else 100.dp)) {
        Text(
            text = "RUNTIME_HISTORY",
            style = TextStyle(
                fontFamily = Syne, 
                fontWeight = FontWeight.Black, 
                fontSize = if (isSm) 32.sp else 80.sp, 
                color = TextPrimary,
                letterSpacing = (-1).sp // REDUCED
            ),
            maxLines = 1,
            softWrap = false
        )
        Spacer(Modifier.height(if (isSm) 40.dp else 60.dp))
        
        val history = listOf(
            Triple(stringResource(Res.string.exp_1_company), stringResource(Res.string.exp_1_title), "2023 - NOW"),
            Triple(stringResource(Res.string.exp_2_company), stringResource(Res.string.exp_2_title), "SEP - DEC 2023"),
            Triple(stringResource(Res.string.exp_3_company), stringResource(Res.string.exp_3_title), "2021 - 2022")
        )

        history.forEach { (comp, role, date) ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = if (isSm) 24.dp else 40.dp), verticalAlignment = Alignment.CenterVertically) {
                // FIX: Larger and more prominent years section to align with the high-end theme
                Surface(
                    modifier = Modifier.width(if (isSm) 130.dp else 300.dp), // FIX: Increased width
                    shape = RoundedCornerShape(20.dp), // FIX: Smoother corners
                    color = GlowIndigo.copy(alpha = 0.15f), // FIX: Boosted color
                    border = BorderStroke(1.dp, GlowIndigo.copy(alpha = 0.4f)) // FIX: Stronger border
                ) {
                    Text(
                        text = date,
                        style = TextStyle(
                            fontFamily = Inter, 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = if (isSm) 16.sp else 28.sp, // FIX: Significantly increased size
                            color = GlowIndigo, 
                            letterSpacing = 0.5.sp, // REDUCED
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                        maxLines = 1,
                        softWrap = false
                    )
                }
                
                Column(modifier = Modifier.weight(1f).padding(start = if (isSm) 20.dp else 40.dp)) {
                    Text(
                        text = role.uppercase(), 
                        style = TextStyle(
                            fontFamily = Syne, 
                            fontWeight = FontWeight.Black, 
                            fontSize = if (isSm) 22.sp else 44.sp, // FIX: Increased size
                            color = TextPrimary,
                            lineHeight = if (isSm) 26.sp else 48.sp,
                            letterSpacing = (-0.5).sp // REDUCED
                        ),
                        maxLines = 2,
                        softWrap = true 
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = comp, 
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
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassBorder))
        }
    }
}
