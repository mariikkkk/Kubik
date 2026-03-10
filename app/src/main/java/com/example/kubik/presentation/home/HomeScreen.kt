package com.example.kubik.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.kubik.presentation.files.FilesTab
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.navigation.components.CustomBottomNavBar
import com.example.kubik.presentation.queues.QueuesTab
import com.example.kubik.presentation.theme.AppBrushes
import com.example.kubik.presentation.theme.KubikTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Queues,
        NavigationItem.Calendar,
        NavigationItem.Requests,
        NavigationItem.Files
    )

    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = "КУБИК",
                    style = MaterialTheme.typography.headlineLarge.copy(
                    brush = AppBrushes.titleGradient)
                    )
                },
                actions = {
                    IconButton(onClick = {  })
                    {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(40.dp)
                        ){
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "ЦМ",
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            CustomBottomNavBar(
                items,
                currentRoute,
                onItemClick = { newRoute ->
                    currentRoute = newRoute
                }
            )
//            NavigationBar(
//                containerColor = MaterialTheme.colorScheme.surface,
//                tonalElevation = 8.dp
//            ) {
//                items.forEachIndexed { index, item ->
//                    NavigationBarItem(
//                    icon =
//                        {
//                        if (item.icon != null)
//                        {
//                            Icon(item.icon, contentDescription = item.title)
//                        }
//                        else if(item.iconId != null)
//                        {
//                            Icon(painter = painterResource(item.iconId), contentDescription = item.title)
//                        }
//                        },
//                    label = { Text(item.title, fontSize = 10.sp) },
//                    selected = selectedItem == index,
//                    onClick = { selectedItem = index },
//
//                    )
//                }
//            }
        }
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center){
            when(currentRoute) {
                NavigationItem.Home.route -> HomeTab()
                NavigationItem.Queues.route -> QueuesTab()
                NavigationItem.Calendar.route -> CalendarTab()
                NavigationItem.Requests.route -> RequestsTab()
                NavigationItem.Files.route -> FilesTab()
            }
        }
    }
}

@Composable
fun HomeTab(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "Home")
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
    KubikTheme() {
        HomeScreen()
    }
}