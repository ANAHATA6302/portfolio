package com.akshit.portfolio.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    SetPageTitle(stringResource(Res.string.header_home))
    val isSm = isSmallScreen()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(AppWhite)
            .padding(top = if (isSm) 70.dp else 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FadeIn {
            Column(
                modifier = Modifier
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (isSm) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val fontSize = 60.sp
                        Text(
                            text = stringResource(Res.string.home_name_first),
                            fontWeight = FontWeight.Black,
                            fontSize = fontSize,
                            color = AppBlack
                        )
                        Spacer(Modifier.height(12.dp))
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = stringResource(Res.string.home_name_first),
                            modifier = Modifier.size(60.dp).clip(CircleShape).background(AppGray)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(Res.string.home_name_last),
                            fontWeight = FontWeight.Black,
                            fontSize = fontSize,
                            color = AppBlack
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 70.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val fontSize = 90.sp
                        Text(
                            text = stringResource(Res.string.home_name_first),
                            fontWeight = FontWeight.Black,
                            fontSize = fontSize,
                            color = AppBlack
                        )
                        Spacer(Modifier.width(12.dp))
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = stringResource(Res.string.home_name_first),
                            modifier = Modifier.size(90.dp).clip(CircleShape).background(AppGray)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = stringResource(Res.string.home_name_last),
                            fontWeight = FontWeight.Black,
                            fontSize = fontSize,
                            color = AppBlack
                        )
                    }
                }

                val bannerModifier = Modifier
                    .fillMaxWidth()
                    .background(AppBBlue, RoundedCornerShape(16.dp))
                    .padding(horizontal = 40.dp, vertical = 40.dp)

                if (isSm) {
                    Column(modifier = bannerModifier) {
                        Text(
                            text = stringResource(Res.string.home_banner_title),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 48.sp,
                            color = AppGreen,
                            modifier = Modifier.padding(bottom = 24.dp),
                            lineHeight = 52.sp
                        )
                        Text(
                            text = stringResource(Res.string.home_banner_subtitle),
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            color = AppBlack,
                            lineHeight = 24.sp
                        )
                    }
                } else {
                    Row(
                        modifier = bannerModifier,
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.home_banner_title),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 76.sp,
                            color = AppGreen,
                            modifier = Modifier.weight(0.8f),
                            lineHeight = 80.sp
                        )
                        Text(
                            text = stringResource(Res.string.home_banner_subtitle),
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp,
                            color = AppBlack,
                            modifier = Modifier.weight(1f),
                            lineHeight = 36.sp
                        )
                    }
                }
                Column(modifier = Modifier.padding(top = if (isSm) 80.dp else 144.dp)) {
                    Text(
                        text = stringResource(Res.string.home_projects_title),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (isSm) 60.sp else 80.sp,
                        color = AppGreen,
                        modifier = Modifier.padding(bottom = if (isSm) 32.dp else 64.dp)
                    )

                    if (isSm) {
                        ProjectCard(
                            Res.drawable.jlrLogo,
                            stringResource(Res.string.project_one_app_title),
                            AppBBlue,
                        )
                        Spacer(Modifier.height(28.dp))
                        ProjectCard(
                            Res.drawable.homeprojectlevel,
                            stringResource(Res.string.project_level_shoes),
                            AppBRed
                        )
                        Spacer(Modifier.height(28.dp))
                        ProjectCard(
                            Res.drawable.portfolioIcon,
                            stringResource(Res.string.project_portfolio),
                            AppBRed,
                        )
                        Spacer(Modifier.height(28.dp))
                        ProjectCard(
                            Res.drawable.homeprojectunexpectedly,
                            stringResource(Res.string.project_unexpectedly),
                            AppBBlue
                        )
                    } else {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                            ProjectCard(
                                Res.drawable.jlrLogo,
                                stringResource(Res.string.project_one_app_title),
                                AppBBlue,
                                Modifier.weight(1f)
                            )
                            ProjectCard(
                                Res.drawable.homeprojectlevel,
                                stringResource(Res.string.project_level_shoes),
                                AppBRed,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(Modifier.height(28.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                            ProjectCard(
                                Res.drawable.portfolioIcon,
                                stringResource(Res.string.project_portfolio),
                                AppBRed,
                                Modifier.weight(1f)
                            )
                            ProjectCard(
                                Res.drawable.homeprojectunexpectedly,
                                stringResource(Res.string.project_unexpectedly),
                                AppBBlue,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                ExperienceSection()

                HomeFooter()
            }
        }
    }
}


@Composable
fun ProjectCard(
    image: DrawableResource,
    title: String,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val isSm = isSmallScreen()
    Column(
        modifier = modifier
            .clickable(onClick = { navigator.push(ProjectsScreen) })
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(48.dp))
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = title,
            modifier = Modifier.height(282.dp)
        )
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = if (isSm) 32.sp else 40.sp,
            modifier = Modifier.padding(top = 40.dp)
        )
    }
}

@Composable
private fun ExperienceSection() {
    val isSm = isSmallScreen()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (isSm) 80.dp else 144.dp)
    ) {
        Text(
            text = stringResource(Res.string.home_experience_title),
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (isSm) 60.sp else 80.sp,
            color = AppGreen,
            modifier = Modifier.padding(bottom = if (isSm) 32.dp else 64.dp)
        )

        ExperienceItem(
            title = stringResource(Res.string.exp_1_title),
            date = stringResource(Res.string.exp_1_date),
            company = stringResource(Res.string.exp_1_company)
        )
        ExperienceItem(
            title = stringResource(Res.string.exp_2_title),
            date = stringResource(Res.string.exp_2_date),
            company = stringResource(Res.string.exp_2_company)
        )
        ExperienceItem(
            title = stringResource(Res.string.exp_3_title),
            date = stringResource(Res.string.exp_3_date),
            company = stringResource(Res.string.exp_3_company)
        )
    }
}

@Composable
private fun ExperienceItem(title: String, date: String, company: String) {
    val isSm = isSmallScreen()

    val itemModifier = if (isSm) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.fillMaxWidth().wrapContentHeight()
    }

    Column(
        modifier = itemModifier.padding(bottom = if (isSm) 24.dp else 0.dp)
    ) {
        if (isSm) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = date,
                        fontSize = 18.sp,
                        color = AppBlack,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(AppBBlue, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .weight(1f)
                    )
                    Text(
                        text = company,
                        fontSize = 18.sp,
                        color = AppBlack,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .weight(1f)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(230.dp)
                            .background(AppBBlue, RoundedCornerShape(16.dp))
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date,
                            fontSize = 24.sp,
                            color = AppBlack,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = company,
                        fontSize = 24.sp,
                        color = AppBlack,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .width(200.dp)
                    )
                }
            }
        }

        if (company != stringResource(Res.string.exp_3_company)) {
            HorizontalDivider(
                modifier = Modifier.padding(top = if (isSm) 24.dp else 0.dp),
                thickness = 1.dp,
                color = Color.Black
            )
        }
    }
}