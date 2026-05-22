package com.example.kubik.presentation.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kubik.R
import com.example.kubik.di.SupabaseModule
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.presentation.files.FilesTab
import com.example.kubik.presentation.home.components.CustomDrawerContent
import com.example.kubik.presentation.home.components.CustomRenameProfileDialog
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.queues.QueueDetailsScreen
import com.example.kubik.presentation.queues.QueuesListScreen
//import com.example.kubik.presentation.queues.QueuesTab
import com.example.kubik.presentation.theme.KubikTheme
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    onLogout: () -> Unit,
    onNavigateToGroup: () -> Unit
){
    val isDarkTheme = when(currentTheme){
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val scope = rememberCoroutineScope()
    var showEditDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val items = listOf(
        NavigationItem.Calendar,
        NavigationItem.Queues,
        NavigationItem.Home,
        NavigationItem.Requests,
        NavigationItem.Files
    )
    val pendingUsersCount by viewModel.pendingUsersCount.collectAsStateWithLifecycle()
    val user by viewModel.userState.collectAsStateWithLifecycle()
    val groupName by viewModel.groupName.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()    // Слушаем переключение экрана
    val currentRoute = navBackStackEntry?.destination?.route // destination - узел в графе. Достаем название пути
    val currentTitle = items.find { it.route == currentRoute }?.title ?: "КУБИК"
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
                CustomDrawerContent(
                    user = user,
                    groupName = groupName,
                    pendingUsersCount,
                    {
                        viewModel.logout { onLogout() }
                    },
                    {
                        scope.launch {
                            drawerState.close()
                        }
                        onNavigateToGroup()
                    },
                    {
                        showEditDialog = true
                        scope.launch {
                                drawerState.close()
                            }
                    } ,
                    onThemeChange = onThemeChange,
                    currentTheme = currentTheme
                )
        }
    ) {
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
                    navigationIcon = {
                        IconButton(
                            {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f), // Прозрачная, когда мы наверху списка
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
                    HomeTab(tabNavController, innerPadding)
                }
                composable(NavigationItem.Queues.route) {
                    QueuesListScreen(
                        innerPadding = innerPadding,
                        onQueueClick = { queueId ->
                            tabNavController.navigate(NavigationItem.QueueDetails.route(queueId))
                        })
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
                composable(
                    route = NavigationItem.QueueDetails.route,
                    arguments = listOf(navArgument("queueId") { type = NavType.StringType })
                ) {
                    QueueDetailsScreen(
                        innerPadding = innerPadding,
                        onBackClick = {
                            tabNavController.popBackStack()
                        }
                    )
                }

            }
        }
    }
    if (showEditDialog && user != null){
        CustomRenameProfileDialog(
            user = user!!,
            onDismiss = { showEditDialog = false },
            onConfirm = { firstName, lastName ->
                viewModel.updateUserProfile(firstName, lastName)
                showEditDialog = false
            }
        )
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


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    val navController = rememberNavController()
//    KubikTheme() {
//        HomeScreen(false,
//            {},
//            {})
//    }
//}

