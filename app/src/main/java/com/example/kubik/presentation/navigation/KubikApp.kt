package com.example.kubik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kubik.presentation.home.HomeScreen
import com.example.kubik.presentation.login.LoginScreen
import com.example.kubik.presentation.queues.QueuesTab

@Composable
fun KubikApp(){
    val globalNavController = rememberNavController()             // Создание контроллера

    NavHost(
        navController = globalNavController,
        startDestination = "login"
    ){
        composable ("login") {                      // Описание маршрута для логина
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
        composable ("home"){
            HomeScreen()
        }

    }
}