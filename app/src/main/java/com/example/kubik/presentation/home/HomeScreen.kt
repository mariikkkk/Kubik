package com.example.kubik.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kubik.presentation.files.FilesTab
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.queues.QueuesTab
import com.example.kubik.presentation.theme.KubikTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
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

    Scaffold(
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
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Home.route) {
                HomeTab(tabNavController)
            }
            composable(NavigationItem.Queues.route) {
                QueuesTab()
            }
            composable(NavigationItem.Calendar.route){
                CalendarTab()
            }
            composable(NavigationItem.Files.route){
                FilesTab()
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
        HomeScreen()
    }
}