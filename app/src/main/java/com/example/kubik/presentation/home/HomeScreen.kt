package com.example.kubik.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kubik.R
import com.example.kubik.presentation.files.FilesTab
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.queues.QueuesTab
import com.example.kubik.presentation.theme.KubikTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(isDarkTheme: Boolean, onThemeChange: () -> Unit){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val items = listOf(
        NavigationItem.Calendar,
        NavigationItem.Queues,
        NavigationItem.Home,
        NavigationItem.Requests,
        NavigationItem.Files
    )
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()    // Слушаем переключение экрана
    val currentRoute = navBackStackEntry?.destination?.route // destination - узел в графе. Достаем название пути
    val currentTitle = items.find { it.route == currentRoute }?.title ?: "КУБИК"

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        currentTitle,
                        fontFamily =
                            FontFamily(
                                Font(
                                    R.font.inter_bold,
                                    FontWeight.Bold
                                )
                            ),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onThemeChange()
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isDarkTheme) R.drawable.dark_theme
                                else R.drawable.light_theme
                            ),
                            contentDescription = "Тема",
                            tint = Color.Unspecified
                        )
                    }
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.exit),
                            contentDescription = "Выйход из аккаунта"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Прозрачная, когда мы наверху списка
                    scrolledContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f) // Слегка заливаем цветом фона, когда текст едет под неё
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                    icon =
                        {
                        if (item.icon != null)
                        {
                            Icon(item.icon, contentDescription = item.title)
                        }
                        else if(item.iconId != null)
                        {
                            Icon(painter = painterResource(item.iconId), contentDescription = item.title)
                        }
                        },
                    label = { Text(item.title, fontSize = 10.sp) },
                    selected = currentRoute == item.route,
                    onClick = {
                        tabNavController.navigate(item.route){
                            // При переходе обрезаем стек до стартового экрана
                            popUpTo(tabNavController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        } },
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(NavigationItem.Home.route) {
                HomeTab(tabNavController, innerPadding, isDarkTheme)
            }
            composable(NavigationItem.Queues.route) {
                QueuesTab(innerPadding)
            }
            composable(NavigationItem.Calendar.route){
                CalendarTab()
            }
            composable(NavigationItem.Files.route){
                FilesTab(innerPadding = innerPadding)
            }
            composable(NavigationItem.Requests.route) {
                RequestsTab()
            }

        }
    }
}



@Composable
fun CalendarTab(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text="Calendar")
    }
}

@Composable
fun RequestsTab(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text="Requests")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    KubikTheme() {
        HomeScreen(false, {})
    }
}