package com.akshit.portfolio.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.common.FadeIn
import com.akshit.portfolio.common.Footer
import com.akshit.portfolio.theme.*
import isSmallScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
fun ContactScreen() {
    val isSm = isSmallScreen()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(AppWhite)
            .padding(top = if (isSm) 70.dp else 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FadeIn {
            Column(
                modifier = Modifier
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroContactSection()
                Footer(isContactPage = true)
            }
        }
    }
}

@Composable
private fun HeroContactSection() {
    val isSm = isSmallScreen()

    val sectionModifier = Modifier
        .fillMaxWidth()
        .padding(
            top = if (isSm) 80.dp else 120.dp,
            bottom = 120.dp,
            start = 24.dp,
            end = 24.dp
        )

    if (isSm) {
        Column(
            modifier = sectionModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.blazerAuthor),
                contentDescription = stringResource(Res.string.content_desc_author_image),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .rotate(10f)
            )

            ContactHeroText(isSm = true)

            EmailCtaBlock(
                isSm = true,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    } else {
        Row(
            modifier = sectionModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                ContactHeroText(isSm = false)
                EmailCtaBlock(
                    isSm = false,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(Modifier.width(48.dp))

            Image(
                painter = painterResource(Res.drawable.blazerAuthor),
                contentDescription = stringResource(Res.string.content_desc_author_image),
                modifier = Modifier
                    .weight(0.7f)
                    .rotate(10f)
            )
        }
    }
}

@Composable
private fun ContactHeroText(isSm: Boolean) {
    Text(
        text = stringResource(Res.string.contact_hero_text),
        fontWeight = FontWeight.Normal,
        fontSize = if (isSm) 30.sp else 42.sp,
        color = if (isSm) Color(0xFFFDA5B1) else AppGreen,
        lineHeight = 1.2.em,
        textAlign = if (isSm) TextAlign.Center else TextAlign.Start
    )
}

@Composable
private fun EmailCtaBlock(
    isSm: Boolean,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val emailString = stringResource(Res.string.email)

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (isSm) Alignment.Center else Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = emailString,
                fontSize = if (isSm) 20.sp else 32.sp,
                color = AppBlack,
                modifier = Modifier
                    .clickable {  }
                    .padding(end = if (isSm) 32.dp else 48.dp)
            )

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(emailString))
                },
                shape = CircleShape,
                modifier = Modifier
                    .size(if (isSm) 80.dp else 98.dp)
                    .offset(x = if (isSm) (-32).dp else (-48).dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSm) Color(0xFFD7D7D5) else AppDark,
                    contentColor = if (isSm) AppBlack else AppWhite
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.email), // Use your new icon
                    contentDescription = "",
                    modifier = Modifier.size(if (isSm) 24.dp else 32.dp)
                )
            }
        }
    }
}