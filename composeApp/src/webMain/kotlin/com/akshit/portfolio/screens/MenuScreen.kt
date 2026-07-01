package com.akshit.portfolio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akshit.portfolio.common.FadeIn
import com.akshit.portfolio.common.SetPageTitle
import com.akshit.portfolio.navigation.AboutScreen
import com.akshit.portfolio.navigation.ContactScreen
import com.akshit.portfolio.navigation.ProjectsScreen
import com.akshit.portfolio.theme.*
import isSmallScreen
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
fun MenuScreen() {
    SetPageTitle("MENU")
    val isSm = isSmallScreen()
    val navigator = LocalNavigator.currentOrThrow

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark,
        contentColor = TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = if (isSm) 80.dp else 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FadeIn {
                Column(
                    modifier = Modifier
                        .widthIn(max = 1200.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    MenuItem(
                        number = "01",
                        title = "PROJECTS",
                        onClick = { navigator.push(ProjectsScreen) }
                    )

                    MenuItem(
                        number = "02",
                        title = "ABOUT",
                        onClick = { navigator.push(AboutScreen) }
                    )

                    MenuItem(
                        number = "03",
                        title = "CONNECT",
                        showDivider = false,
                        onClick = { navigator.push(ContactScreen) }
                    )
                    
                    Spacer(Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    number: String,
    title: String,
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    val isSm = isSmallScreen()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = if (isSm) 24.dp else 48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = number,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Black,
                    fontSize = if (isSm) 14.sp else 24.sp,
                    color = GlowIndigo,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.padding(end = 24.dp)
            )
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Syne,
                    fontWeight = FontWeight.Black,
                    fontSize = if (isSm) 40.sp else 100.sp, // Reduced for mobile
                    color = TextPrimary,
                    letterSpacing = if (isSm) (-1).sp else (-4).sp
                ),
                maxLines = 1,
                softWrap = false
            )
        }

        if (showDivider) {
            Spacer(Modifier.height(if (isSm) 24.dp else 48.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(GlassBorder)
            )
        }
    }
}
