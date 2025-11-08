package com.akshit.portfolio.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.akshit.portfolio.theme.*
import isSmallScreen
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.composeapp.generated.resources.Res
import portfolio.composeapp.generated.resources.bigArrow
import portfolio.composeapp.generated.resources.email
import portfolio.composeapp.generated.resources.footer_buy_coffee
import portfolio.composeapp.generated.resources.footer_designed_by
import portfolio.composeapp.generated.resources.footer_eric_web
import portfolio.composeapp.generated.resources.footer_get_in_touch
import portfolio.composeapp.generated.resources.footer_instagram
import portfolio.composeapp.generated.resources.footer_lets_talk
import portfolio.composeapp.generated.resources.footer_linkedin
import portfolio.composeapp.generated.resources.link_buy_coffee
import portfolio.composeapp.generated.resources.link_figma_eric
import portfolio.composeapp.generated.resources.link_instagram
import portfolio.composeapp.generated.resources.link_linkedin

@Composable
private fun ExternalLink(
    text: String,
    url: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = AppBlack,
        textDecoration = TextDecoration.Underline,
        modifier = modifier.clickable {
            window.open(url = url, target = "_blank")
        }
    )
}


/**
 * The Home Page Footer, from HomeFooter.jsx
 */
@Composable
fun HomeFooter() {
    val isSm = isSmallScreen()
    val clipboardManager = LocalClipboardManager.current

    val email = stringResource(Res.string.email)
    val copyToClipboard: (String) -> Unit = { textToCopy ->
        clipboardManager.setText(AnnotatedString(textToCopy))
    }
    Column(
        modifier = Modifier
            .padding(top = 144.dp, bottom = 40.dp)
            .fillMaxWidth()
            .background(AppBBlue, RoundedCornerShape(48.dp))
            .padding(if (isSm) 24.dp else 48.dp)
    ) {
        if (isSm) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.footer_lets_talk),
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    color = AppBlack,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { copyToClipboard(email) },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                    modifier = Modifier
                        .height(67.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        stringResource(Res.string.email),
                        color = AppWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.footer_lets_talk),
                    fontWeight = FontWeight.Bold,
                    fontSize = 80.sp,
                    color = AppBlack
                )
                Button(
                    onClick = { copyToClipboard(email) },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                    modifier = Modifier
                        .height(67.dp)
                        .widthIn(min = 230.dp, max = 331.dp)
                ) {
                    Text(
                        stringResource(Res.string.email),
                        color = AppWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 40.dp),
            thickness = 1.dp,
            color = AppDark
        )

        if (isSm) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ExternalLink(
                    stringResource(Res.string.footer_instagram),
                    stringResource(Res.string.link_instagram),
                )
                ExternalLink(
                    stringResource(Res.string.footer_linkedin),
                    stringResource(Res.string.link_linkedin),
                )
                ExternalLink(
                    stringResource(Res.string.footer_buy_coffee),
                    stringResource(Res.string.link_buy_coffee),
                )

                Spacer(Modifier.height(20.dp))

                ExternalLink(
                    stringResource(Res.string.footer_eric_web),
                    stringResource(Res.string.link_figma_eric),
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    ExternalLink(
                        stringResource(Res.string.footer_instagram),
                        stringResource(Res.string.link_instagram),
                    )
                    ExternalLink(
                        stringResource(Res.string.footer_linkedin),
                        stringResource(Res.string.link_linkedin),
                    )
                    ExternalLink(
                        stringResource(Res.string.footer_buy_coffee),
                        stringResource(Res.string.link_buy_coffee),
                    )
                }
                ExternalLink(
                    stringResource(Res.string.footer_eric_web),
                    stringResource(Res.string.link_figma_eric),
                )
            }
        }
    }
}

/**
 * The main site Footer, from Footer.jsx (Renamed from GenericFooter)
 * @param isContactPage Set to true to hide the "Let's Talk" section.
 */
@Composable
fun Footer(isContactPage: Boolean = false) {
    val isSm = isSmallScreen()
    val clipboardManager = LocalClipboardManager.current

    val email = stringResource(Res.string.email)
    val copyToClipboard: (String) -> Unit = { textToCopy ->
        clipboardManager.setText(AnnotatedString(textToCopy))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isContactPage) {
            Column(
                modifier = Modifier
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth()
                    .padding(top = 250.dp, bottom = 124.dp)
            ) {
                Text(
                    text = stringResource(Res.string.email),
                    fontWeight = FontWeight.Normal,
                    fontSize = if (isSm) 20.sp else 24.sp,
                    color = AppGreen,
                    modifier = Modifier.clickable { copyToClipboard(email) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = stringResource(Res.string.footer_lets_talk),
                        fontWeight = FontWeight.Normal,
                        fontSize = if (isSm) 60.sp else 80.sp,
                        color = AppDark,
                        lineHeight = 0.9.em
                    )
                    Image(
                        painter = painterResource(Res.drawable.bigArrow),
                        contentDescription = "Arrow",
                        modifier = Modifier
                            .size(if (isSm) 32.dp else 40.dp)
                            .padding(bottom = 12.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .widthIn(max = 1000.dp)
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            if (isSm) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column {
                        Text(
                            text = stringResource(Res.string.footer_get_in_touch),
                            fontSize = 18.sp,
                            color = AppDark
                        )
                        Text(
                            text = stringResource(Res.string.email),
                            fontSize = 18.sp,
                            color = AppDark,
                            modifier = Modifier.clickable { copyToClipboard(email) }
                        )
                    }
                    Column {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            ExternalLink(
                                stringResource(Res.string.footer_instagram),
                                stringResource(Res.string.link_instagram),
                            )
                            Text(
                                text = ",",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = AppDark
                            )
                            ExternalLink(
                                stringResource(Res.string.footer_linkedin),
                                stringResource(Res.string.link_linkedin),
                            )
                        }
                        ExternalLink(
                            stringResource(Res.string.footer_buy_coffee),
                            stringResource(Res.string.link_buy_coffee),
                        )
                    }
                    Row {
                        Text(
                            text = "Designed by ",
                            fontSize = 18.sp,
                            color = AppDark
                        )
                        ExternalLink(
                            stringResource(Res.string.footer_eric_web),
                            stringResource(Res.string.link_figma_eric),
                        )
                        Text(
                            text = " ©2024",
                            fontSize = 18.sp,
                            color = AppDark
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.footer_get_in_touch),
                            fontSize = 18.sp,
                            color = AppDark
                        )
                        Text(
                            text = stringResource(Res.string.email),
                            fontSize = 18.sp,
                            color = AppDark,
                            modifier = Modifier.clickable { copyToClipboard(email) }
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            ExternalLink(
                                stringResource(Res.string.footer_instagram),
                                stringResource(Res.string.link_instagram),
                            )
                            Text(
                                text = ",",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = AppDark
                            )
                            ExternalLink(
                                stringResource(Res.string.footer_linkedin),
                                stringResource(Res.string.link_linkedin),
                            )
                        }
                        ExternalLink(
                            stringResource(Res.string.footer_buy_coffee),
                            stringResource(Res.string.link_buy_coffee),
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(Res.string.footer_designed_by),
                            fontSize = 18.sp,
                            color = AppDark
                        )
                        ExternalLink(
                            stringResource(Res.string.footer_eric_web),
                            stringResource(Res.string.link_figma_eric),
                        )
                    }
                }
            }
        }
    }
}