package com.example.kubik.presentation.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kubik.di.SupabaseModule
import com.example.kubik.domain.models.AuthState
import com.example.kubik.presentation.home.HomeScreen
import com.example.kubik.presentation.login.LoginScreen
import com.example.kubik.presentation.theme.KubikTheme
// ДОБАВЛЕНЫ ДВА ВАЖНЫХ ИМПОРТА ДЛЯ СТАТУСА
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth

@Composable
fun KubikApp(mainViewModel: MainViewModel = hiltViewModel()){
    val systemTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemTheme) }
    val authState by mainViewModel.authState.collectAsState(initial = AuthState.Loading)

    KubikTheme(darkTheme = isDarkTheme) {
        val globalNavController = rememberNavController()

        when (authState) {
            is AuthState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val startScreen = if (authState is AuthState.Authenticated) {
                    "home"
                } else {
                    "login"
                }
                NavHost(
                    navController = globalNavController,
                    startDestination = startScreen
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                globalNavController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { isDarkTheme = !isDarkTheme },
                            onLogout = {
                                globalNavController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}