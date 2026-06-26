package com.akshit.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.akshit.portfolio.common.Header
import com.akshit.portfolio.common.MenuHeader
import com.akshit.portfolio.common.SecondHeader
import com.akshit.portfolio.navigation.AboutScreen
import com.akshit.portfolio.navigation.ContactScreen
import com.akshit.portfolio.navigation.HomeScreen
import com.akshit.portfolio.navigation.MenuScreen
import com.akshit.portfolio.navigation.ProjectsScreen
import com.akshit.portfolio.theme.BackgroundDark
import com.akshit.portfolio.theme.getTypography

@Composable
fun App() {
    MaterialTheme(
        typography = getTypography()
    ) {
        // FIX: Ensure the entire application has the dark background
        Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
            Navigator(screen = HomeScreen) { navigator ->
                val currentScreen = navigator.lastItem

                val showSecondHeader = currentScreen is ProjectsScreen ||
                        currentScreen is AboutScreen ||
                        currentScreen is ContactScreen

                val showMenuHeader = currentScreen is MenuScreen

                Column(modifier = Modifier.fillMaxSize()) {
                    when {
                        showSecondHeader -> SecondHeader(navigator)
                        showMenuHeader -> MenuHeader(navigator)
                        else -> Header(navigator)
                    }
                    navigator.lastItem.Content()
                }
            }
        }
    }
}
