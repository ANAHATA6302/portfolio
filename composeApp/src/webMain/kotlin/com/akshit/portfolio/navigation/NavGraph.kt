package com.akshit.portfolio.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.akshit.portfolio.screens.AboutMeScreen
import com.akshit.portfolio.screens.ContactScreen
import com.akshit.portfolio.screens.HomeScreen
import com.akshit.portfolio.screens.MenuScreen
import com.akshit.portfolio.screens.ProjectsScreen

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreen()
    }
}

object MenuScreen : Screen {
    @Composable
    override fun Content() {
        MenuScreen()
    }
}

object ProjectsScreen : Screen {
    @Composable
    override fun Content() {
        ProjectsScreen()
    }
}

object AboutScreen : Screen {
    @Composable
    override fun Content() {
        AboutMeScreen()
    }
}

object ContactScreen : Screen {
    @Composable
    override fun Content() {
        ContactScreen()
    }
}
