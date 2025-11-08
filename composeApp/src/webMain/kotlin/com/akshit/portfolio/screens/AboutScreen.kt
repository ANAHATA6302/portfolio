package com.akshit.portfolio.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroAboutSection(isSm)
                WorkExperienceIntroSection(isSm)
                ExperienceListSection(isSm)
                Footer()
            }
        }
    }
}

@Composable
private fun HeroAboutSection(isSm: Boolean) {
    Column(
        modifier = Modifier
            .widthIn(max = 800.dp)
    ) {
        Text(
            fontFamily = Georama,
            text = stringResource(Res.string.about_location),
            color = AppGreen,
            fontSize = 20.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(if (isSm) 16.dp else 24.dp)
        ) {
            Text(
                fontFamily = Antonio,
                text = stringResource(Res.string.about_hero_title),
                fontWeight = FontWeight.Bold,
                fontSize = if (isSm) 40.sp else 64.sp,
                color = AppGreen,
                textAlign = TextAlign.End,
                lineHeight = 1.1.em,
                modifier = Modifier.weight(if (isSm) 0.5f else 0.6f)
            )
            Image(
                painter = painterResource(Res.drawable.authCloseImage),
                contentDescription = stringResource(Res.string.content_desc_author_close),
                modifier = Modifier.weight(if (isSm) 0.5f else 0.4f)
            )
        }
    }
}

@Composable
private fun WorkExperienceIntroSection(isSm: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 176.dp)
    ) {
        Text(
            fontFamily = BebasNeue,
            text = stringResource(Res.string.about_work_exp_title),
            fontWeight = FontWeight.Bold,
            fontSize = if (isSm) 48.sp else 84.sp,
            color = AppGreen,
            lineHeight = 1.em
        )
        Text(
            fontFamily = Georama,
            text = stringResource(Res.string.about_work_exp_desc),
            color = AppDark,
            fontSize = if (isSm) 18.sp else 32.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 1.2.em,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun ExperienceListSection(isSm: Boolean) {
    val jlrKey = "jlr"
    var expandedItem by remember { mutableStateOf<String?>(jlrKey) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 166.dp)
    ) {

        ExperienceItem(
            isSm = isSm,
            title = stringResource(Res.string.exp_1_title),
            company = stringResource(Res.string.exp_1_company),
            date = stringResource(Res.string.exp_1_date),
            description = stringResource(Res.string.exp_desc_jlr),
            isExpanded = expandedItem == jlrKey,
            onClick = { expandedItem = if (expandedItem == jlrKey) null else jlrKey }
        )

        val idsKey = "ids"
        ExperienceItem(
            isSm = isSm,
            title = stringResource(Res.string.exp_2_title),
            company = stringResource(Res.string.exp_2_company),
            date = stringResource(Res.string.exp_2_date),
            description = stringResource(Res.string.exp_desc_ids),
            isExpanded = expandedItem == idsKey,
            onClick = { expandedItem = if (expandedItem == idsKey) null else idsKey }
        )

        val freelanceKey = "freelance"
        ExperienceItem(
            isSm = isSm,
            title = stringResource(Res.string.exp_3_title),
            company = stringResource(Res.string.exp_3_company),
            date = stringResource(Res.string.exp_3_date),
            description = stringResource(Res.string.exp_desc_freelance),
            isExpanded = expandedItem == freelanceKey,
            onClick = { expandedItem = if (expandedItem == freelanceKey) null else freelanceKey }
        )
    }
}

@Composable
private fun ExperienceItem(
    isSm: Boolean,
    title: String,
    company: String,
    date: String,
    description: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
            .clickable { onClick() }
    ) {
        if (isSm) {
            Column {
                Text(
                    fontFamily = Antonio,
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 32.sp,
                    color = AppBlack,
                    lineHeight = 1.1.em
                )
                Text(
                    text = "$company \n $date",
                    color = AppGreen,
                    fontFamily = Georama,
                    fontSize = 18.sp,
                    lineHeight = 1.2.em,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    fontFamily = Antonio,
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 48.sp,
                    color = AppBlack,
                    lineHeight = 1.1.em,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    text = "$company \n $date",
                    fontFamily = Georama,
                    color = AppGreen,
                    fontSize = 20.sp,
                    lineHeight = 1.2.em,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .background(AppBBlue, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Text(
                    fontFamily = Inter,
                    text = description,
                    color = AppDark,
                    fontSize = 18.sp,
                    lineHeight = 1.4.em
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 28.dp),
            thickness = 1.dp,
            color = AppBlack
        )
    }
}