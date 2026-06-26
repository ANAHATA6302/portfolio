package com.akshit.portfolio.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.akshit.portfolio.navigation.ContactScreen
import com.akshit.portfolio.navigation.MenuScreen
import com.akshit.portfolio.theme.*
import isSmallScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
private fun Modifier.rimLight(): Modifier = this.drawWithContent {
    drawContent()
}

@Composable
fun Header(navigator: Navigator) {
    val isSm = isSmallScreen()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (isSm) 12.dp else 24.dp)
            .padding(bottom = if (isSm) 12.dp else 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 1400.dp)
                .fillMaxWidth(if (isSm) 0.95f else 1f)
                .height(if (isSm) 56.dp else 80.dp)
                .rimLight(),
            shape = RoundedCornerShape(if (isSm) 28.dp else 40.dp),
            color = Color(0xFF0F172A).copy(alpha = 0.8f), // Deep dark translucent color
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isSm) 16.dp else 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo + Home
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { navigator.popAll() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isSm) 32.dp else 48.dp)
                            .clip(CircleShape)
                            .background(GlowIndigo.copy(alpha = 0.2f))
                            .border(1.dp, GlowIndigo, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.6f)
                        )
                    }
                    Spacer(Modifier.width(if (isSm) 8.dp else 16.dp))
                    Text(
                        text = if (isSm) "HOME" else stringResource(Res.string.header_logo_text).uppercase(),
                        style = TextStyle(
                            fontFamily = Syne,
                            fontWeight = FontWeight.Black,
                            color = GlowIndigo, 
                            letterSpacing = 2.sp,
                            fontSize = if (isSm) 12.sp else 20.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isSm) {
                        TextButton(
                            onClick = { navigator.push(ContactScreen) },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = "CONNECT",
                                style = TextStyle(
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.Bold,
                                    color = GlowPink,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                    }

                    // Hamburger Menu Button
                    Box(
                        modifier = Modifier
                            .size(if (isSm) 36.dp else 44.dp)
                            .clip(CircleShape)
                            .background(GlowIndigo)
                            .clickable { navigator.push(MenuScreen) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.size(if (isSm) 14.dp else 18.dp),
                            verticalArrangement = Arrangement.spacedBy(if (isSm) 3.dp else 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color.White))
                            Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color.White))
                            Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color.White))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuHeader(navigator: Navigator) {
    val isSm = isSmallScreen()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (isSm) 12.dp else 24.dp)
            .padding(bottom = if (isSm) 12.dp else 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 1400.dp)
                .fillMaxWidth(if (isSm) 0.95f else 1f)
                .height(if (isSm) 56.dp else 80.dp)
                .rimLight(),
            shape = RoundedCornerShape(if (isSm) 28.dp else 40.dp),
            color = BackgroundDark.copy(alpha = 0.95f),
            border = BorderStroke(1.dp, GlowIndigo.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isSm) 20.dp else 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Title is now "HOME" and clickable
                Text(
                    text = "HOME",
                    modifier = Modifier.clickable { navigator.popAll() },
                    style = TextStyle(
                        fontFamily = Syne,
                        fontWeight = FontWeight.Black,
                        color = GlowIndigo, 
                        letterSpacing = if (isSm) 2.sp else 8.sp,
                        fontSize = if (isSm) 14.sp else 24.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )

                // High Visibility Close Button
                Surface(
                    onClick = { navigator.pop() },
                    shape = RoundedCornerShape(22.dp),
                    color = GlowPink,
                    modifier = Modifier.height(if (isSm) 32.dp else 44.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = if (isSm) 12.dp else 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CLOSE ×",
                            style = TextStyle(
                                fontFamily = Inter,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = if (isSm) 11.sp else 13.sp,
                                letterSpacing = 1.sp
                            ),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SecondHeader(navigator: Navigator) {
    Header(navigator) 
}
