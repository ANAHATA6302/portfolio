package com.akshit.portfolio.screens

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
fun ProjectsScreen() {
    SetPageTitle("PROJECTS")
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
                Spacer(Modifier.height(20.dp))
                HeroSection(isSm)
                ProjectIntroSection(isSm)
            }
        }
        ProjectsHorizontalList(isSm)
        Footer()
    }
}

@Composable
private fun HeroSection(isSm: Boolean) {
    if (isSm) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontFamily = Antonio,
                        text = stringResource(Res.string.projects_hero_android).uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 64.sp,
                        color = AppGreen,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.em,
                        letterSpacing = 4.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        fontFamily = BebasNeue,
                        text = stringResource(Res.string.projects_hero_engineer).uppercase(),
                        fontWeight = FontWeight.Thin,
                        fontSize = 58.sp,
                        color = AppGreen,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.em,
                        letterSpacing = 4.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Image(
                painter = painterResource(Res.drawable.blazerAuthor),
                contentDescription = stringResource(Res.string.content_desc_author_image),
                modifier = Modifier
                    .fillMaxWidth(0.23f)
                    .rotate(350f)
                    .padding(vertical = 16.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontFamily = Antonio,
                        text = stringResource(Res.string.projects_hero_android).uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 200.sp,
                        color = AppGreen,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.em,
                        letterSpacing = 8.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        fontFamily = BebasNeue,
                        text = stringResource(Res.string.projects_hero_engineer).uppercase(),
                        fontWeight = FontWeight(20),
                        fontSize = 170.sp,
                        color = AppGreen,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.em,
                        letterSpacing = 8.sp,
                    )
                }
            }
            Image(
                painter = painterResource(Res.drawable.blazerAuthor),
                contentDescription = stringResource(Res.string.content_desc_author_image),
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.23f)
                    .rotate(350f)
            )
        }
    }
}

@Composable
private fun ProjectIntroSection(isSm: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = if (isSm) 0.dp else 56.dp)
    ) {
        Text(
            text = stringResource(Res.string.projects_section_title),
            fontWeight = FontWeight.Medium,
            fontFamily = Inter,
            fontSize = if (isSm) 60.sp else 150.sp,
            lineHeight = 1.em
        )
        Text(
            text = stringResource(Res.string.projects_section_desc),
            fontWeight = FontWeight.Light,
            fontFamily = Inter,
            fontSize = if (isSm) 24.sp else 32.sp,
            lineHeight = 1.2.em,
            modifier = Modifier.padding(vertical = 40.dp)
        )
    }
}

@Composable
private fun ProjectsHorizontalList(isSm: Boolean) {
    val projectList = listOf(
        ProjectInfo(
            title = stringResource(Res.string.project_one_app_title),
            description = stringResource(Res.string.project_one_app_desc),
            color = AppLGreen
        ),
        ProjectInfo(
            title = stringResource(Res.string.project_level_title),
            description = stringResource(Res.string.project_level_desc),
            author = stringResource(Res.string.project_level_author),
            color = AppYellow
        ),
        ProjectInfo(
            title = stringResource(Res.string.project_portfolio_title),
            description = stringResource(Res.string.project_portfolio_desc),
            author = stringResource(Res.string.project_level_author),
            color = AppLGreen
        ),
        ProjectInfo(
            title = stringResource(Res.string.project_unexpectedly_title),
            description = stringResource(Res.string.project_unexpectedly_desc),
            color = AppYellow
        ),
        ProjectInfo(
            title = stringResource(Res.string.project_potterpedia_title),
            description = stringResource(Res.string.project_potterpedia_desc),
            color = AppLGreen
        ),
        ProjectInfo(
            title = stringResource(Res.string.project_truth_or_dare_title),
            description = stringResource(Res.string.project_truth_or_dare_desc),
            color = AppYellow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBlack)
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 1000.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val listState = rememberLazyListState()

            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(projectList) { project ->
                    ProjectInfoCard(
                        isSm = isSm,
                        title = project.title,
                        description = project.description,
                        author = project.author,
                        bgColor = project.color
                    )
                }
            }

            HorizontalScrollbar(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 24.dp),
                adapter = rememberScrollbarAdapter(listState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4.dp),
                    hoverDurationMillis = 0,
                    unhoverColor = AppWhite.copy(alpha = 0.3f),
                    hoverColor = AppWhite.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun ProjectInfoCard(
    isSm: Boolean,
    title: String,
    description: String,
    author: String? = null,
    bgColor: Color,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .width(if (isSm) 300.dp else 450.dp)
            .height(520.dp)
            .background(bgColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.projecticon1),
            contentDescription = stringResource(Res.string.content_desc_project_icon),
            modifier = Modifier.width(if (isSm) 150.dp else 200.dp)
        )
        Text(
            text = title,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = if (isSm) 40.sp else 50.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 40.dp)
        )
        Text(
            text = description,
            fontFamily = Inter,
            fontWeight = FontWeight.Light,
            fontSize = 24.sp,
            color = Color.Black,
            lineHeight = 1.3.em,
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(top = 24.dp)
        )
        if (author != null) {
            Text(
                text = author,
                fontWeight = FontWeight.Light,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 40.dp)
            )
        }
    }
}

private data class ProjectInfo(
    val title: String,
    val description: String,
    val author: String? = null,
    val color: Color
)