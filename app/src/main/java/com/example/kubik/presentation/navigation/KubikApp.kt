package com.example.kubik.presentation.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kubik.presentation.home.HomeScreen
import com.example.kubik.presentation.login.LoginScreen
import com.example.kubik.presentation.queues.QueuesTab
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun KubikApp(){
    val systemTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemTheme) }
    KubikTheme(darkTheme = isDarkTheme) {
        val globalNavController = rememberNavController()             // Создание контроллера

        NavHost(
            navController = globalNavController,
            startDestination = "login"
        ) {
            composable("login") {                      // Описание маршрута для логина
                LoginScreen(
                    onLoginSuccess = {
                        // Когда логин успешен, переходим на глвный экран
                        globalNavController.navigate("home") {
                            // Достаем из стека все предыдущие экраны вплоть до login (inclusive включает сам экран)
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {

                HomeScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = !isDarkTheme }
                )
            }

        }
    }
}