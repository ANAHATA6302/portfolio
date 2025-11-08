package com.akshit.portfolio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(AppGreen)
            .padding(top = if (isSm) 70.dp else 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FadeIn {
            Column(
                modifier = Modifier
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                MenuItem(
                    number = stringResource(Res.string.menu_01),
                    title = stringResource(Res.string.menu_projects),
                    onClick = { navigator.push(ProjectsScreen) }
                )

                MenuItem(
                    number = stringResource(Res.string.menu_02),
                    title = stringResource(Res.string.menu_about),
                    onClick = { navigator.push(AboutScreen) }
                )

                MenuItem(
                    number = stringResource(Res.string.menu_03),
                    title = stringResource(Res.string.menu_contact),
                    showDivider = false,
                    onClick = { navigator.push(ContactScreen) }
                )
                Spacer(Modifier.height(200.dp))
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
        modifier = Modifier.fillMaxWidth()
    ) {
        if (number != stringResource(Res.string.menu_01)) {
            Spacer(Modifier.height(78.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    fontFamily = Inter,
                    text = number,
                    fontWeight = FontWeight.Normal,
                    fontSize = if (isSm) 20.sp else 32.sp,
                    color = AppWhite,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    fontFamily = Antonio,
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isSm) 60.sp else 120.sp,
                    color = AppWhite,
                    lineHeight = 0.9.em
                )
            }

            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (isSm) 32.dp else 48.dp),
                    thickness = 1.dp,
                    color = AppBorderGray
                )
            }
        }
    }
}