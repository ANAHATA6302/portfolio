package com.akshit.portfolio.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.akshit.portfolio.navigation.ContactScreen
import com.akshit.portfolio.navigation.MenuScreen
import com.akshit.portfolio.theme.AppBlack
import com.akshit.portfolio.theme.AppDark
import com.akshit.portfolio.theme.AppGreen
import com.akshit.portfolio.theme.AppWhite
import isSmallScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.*

@Composable
private fun AppHeaderDivider(color: Color = AppBlack.copy(alpha = 0.2f)) {
    HorizontalDivider(
        modifier = Modifier
            .widthIn(max = 955.dp)
            .fillMaxWidth(),
        thickness = 1.dp,
        color = color
    )
}

@Composable
fun Header(navigator: Navigator) {
    val isSm = isSmallScreen()

    Column(
        modifier = Modifier.fillMaxWidth().background(AppWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 1000.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = stringResource(Res.string.header_logo_text),
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = stringResource(Res.string.header_logo_text),
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = AppBlack
                )
            }

            Image(
                painter = painterResource(Res.drawable.menu),
                contentDescription = stringResource(Res.string.header_menu),
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navigator.push(MenuScreen) }
            )

            if (isSm) {
                Spacer(Modifier.size(width = 147.dp, height = 50.dp))
            } else {
                Button(
                    onClick = { navigator.push(ContactScreen) },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppGreen,
                        contentColor = AppWhite
                    ),
                    modifier = Modifier.size(width = 147.dp, height = 50.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.header_lets_talk),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Image(
                        painter = painterResource(Res.drawable.arrow),
                        contentDescription = stringResource(Res.string.header_lets_talk),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        AppHeaderDivider()
    }
}

@Composable
fun MenuHeader(navigator: Navigator) {
    val isSm = isSmallScreen()

    Column(
        modifier = Modifier.fillMaxWidth().background(AppGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 1000.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = stringResource(Res.string.header_logo_text),
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = stringResource(Res.string.header_logo_text),
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = AppWhite
                )
            }

            Text(
                text = stringResource(Res.string.header_home),
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = AppWhite,
                modifier = Modifier.clickable { navigator.popAll() }
            )

            if (isSm) {
                Spacer(Modifier.size(width = 147.dp, height = 50.dp))
            } else {
                Button(
                    onClick = { navigator.push(ContactScreen) },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppWhite,
                        contentColor = AppBlack
                    ),
                    modifier = Modifier.size(width = 147.dp, height = 50.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.header_lets_talk),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Image(
                        painter = painterResource(Res.drawable.arrow2),
                        contentDescription = stringResource(Res.string.header_lets_talk),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        AppHeaderDivider(color = AppWhite.copy(alpha = 0.4f))
    }
}

@Composable
fun SecondHeader(navigator: Navigator) {
    val isSm = isSmallScreen()

    Column(
        modifier = Modifier.fillMaxWidth().background(AppWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 1000.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.header_name),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = AppDark
            )

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!isSm) {
                    Text(
                        text = stringResource(Res.string.header_connect),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = AppBlack,
                    )
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = {  },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppGreen,
                            contentColor = AppWhite
                        ),
                        modifier = Modifier.height(40.dp).widthIn(min = 200.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.email),
                            fontSize = 16.sp,
                        )
                        Image(
                            painter = painterResource(Res.drawable.email),
                            contentDescription = stringResource(Res.string.email),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { navigator.push(MenuScreen) }
            ) {
                if (isSm) {
                    Image(
                        painter = painterResource(Res.drawable.plus),
                        contentDescription = stringResource(Res.string.header_menu)
                    )
                }
                Text(
                    text = stringResource(Res.string.header_menu),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = AppDark
                )
            }
        }
    }
}