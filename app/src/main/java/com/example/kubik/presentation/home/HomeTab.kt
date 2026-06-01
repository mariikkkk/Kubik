package com.example.kubik.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kubik.R
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.domain.models.User
import com.example.kubik.presentation.announcement.AnnouncementUiState
import com.example.kubik.presentation.announcement.AnnouncementViewModel
import com.example.kubik.presentation.announcement.components.AnnouncementCard
import com.example.kubik.presentation.announcement.components.AnnouncementCreateDialog
import com.example.kubik.presentation.home.components.DeadlineCard
import com.example.kubik.presentation.home.components.EventCard
import com.example.kubik.presentation.home.components.GreetingCard
import com.example.kubik.presentation.home.components.MainCard
import com.example.kubik.presentation.home.components.NotificationCard
import com.example.kubik.presentation.home.components.QueueCard
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun HomeTab(
    tabNavController: NavController,
    innerPadding: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    announcementViewModel: AnnouncementViewModel = hiltViewModel()
){
    val user by viewModel.userState.collectAsStateWithLifecycle()
    val groupName by viewModel.groupName.collectAsStateWithLifecycle()
    val nearestQueueCardState by homeViewModel.nearestQueue.collectAsStateWithLifecycle()
    val announcementUiState by announcementViewModel.uiState.collectAsStateWithLifecycle()
    HomeTabContent(
        user = user,
        tabNavController = tabNavController,
        innerPadding = innerPadding,
        groupName = groupName,
        nearestQueueCardState = nearestQueueCardState,
        announcementUiState = announcementUiState,
        onOpenAnnouncementDialog = announcementViewModel::openCreateDialog,
        onCloseAnnouncementDialog = announcementViewModel::closeCreateDialog,
        onAnnouncementTextChange = announcementViewModel::updateText,
        onAnnouncementTypeChange = announcementViewModel::selectType,
        onAnnouncementTitleChange = announcementViewModel::updateTitle,
        onCreateAnnouncement = announcementViewModel::createAnnouncement
    )
}

@Composable
fun HomeTabContent(
    user: User?,
    tabNavController: NavController,
    innerPadding: PaddingValues,
    groupName: String,
    nearestQueueCardState: NearestQueueCardState?,
    announcementUiState: AnnouncementUiState,
    onOpenAnnouncementDialog: () -> Unit,
    onCloseAnnouncementDialog: () -> Unit,
    onAnnouncementTitleChange: (String) -> Unit,
    onAnnouncementTextChange: (String) -> Unit,
    onAnnouncementTypeChange: (AnnouncementType) -> Unit,
    onCreateAnnouncement: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val role = when(user?.role){
        "student" -> "Студент"
        "starosta" -> "Староста"
        else -> "Загрузка..."
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        //.padding(top = 24.dp, start = 16.dp, end = 16.dp)
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
            bottom = innerPadding.calculateBottomPadding() + 100.dp
        ),
    ) {

        item{
            Spacer(modifier = Modifier.height(24.dp))
                //GreetingCard(user)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    "Привет,",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_regular,
                            FontWeight.Normal
                        )
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${user?.firstName.toString()}\uD83D\uDC4B",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_medium,
                            FontWeight.Medium
                        )
                    ),
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(0.2f),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                    ){
                        Text(
                            groupName,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_regular,
                                    FontWeight.Normal
                                )
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(0.2f),
                                shape = RoundedCornerShape(24.dp   )
                            )
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                    ){
                        Text(
                            text = role,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_regular,
                                    FontWeight.Normal
                                )
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(24.dp))
            nearestQueueCardState?.let { queue ->
                QueueCard(
                    title = queue.title,
                    position = queue.position,
                    onClick = {
                        tabNavController.navigate(
                            NavigationItem.QueueDetails.route(queue.queueId)
                        )
                    }
                )
            }
//            Spacer(Modifier.width(12.dp))
//            DeadlineCard(
//                title = "Физика",
//                days = 9,
//                onClick = {
//                    tabNavController.navigate(NavigationItem.Calendar.route){
//                        popUpTo(NavigationItem.Home.route){ saveState = true }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
        }
        item{
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(painter = painterResource(R.drawable.notifications),
                    contentDescription = "Обявления",
                    modifier = Modifier.size(20.dp))

                Spacer(modifier = Modifier.width(8.dp))
                Text("Объявления",
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold, FontWeight.Bold)
                    ),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("Все",
                    fontFamily = FontFamily(
                        Font(R.font.inter_medium, FontWeight.Normal)
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        tabNavController.navigate(NavigationItem.Announcements.route)
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(2.dp, RoundedCornerShape(32.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(32.dp)
                        )
                        .border(
                            BorderStroke(1.5f.dp, MaterialTheme.colorScheme.outline),
                            RoundedCornerShape(32.dp)
                        )
                        .clickable{
                            onOpenAnnouncementDialog()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add,
                        contentDescription = "Создать объявление",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            val announcementsHome = announcementUiState.filteredAnnouncements.take(3)
            if(announcementsHome.isEmpty()){
                Text("Пока что объявлений нет")
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    announcementsHome.forEach { announcementItem -> 
                        AnnouncementCard(
                            announcementItem,
                            onDeleteClick = {}
                        )
                    }
                }
            }
        }
//        item{
//            Spacer(modifier = Modifier.height(24.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                if(isDarkTheme) {
//                    Image(
//                        painter = painterResource(R.drawable.deadlinedark),
//                        contentDescription = "События",
//                        modifier = Modifier.size(32.dp)
//                    )
//                } else{
//                    Image(
//                        painter = painterResource(R.drawable.deadline),
//                        contentDescription = "События",
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//                Spacer(Modifier.width(8.dp))
//                Text("События",
//                    fontFamily = FontFamily(
//                        Font(R.font.inter_bold, FontWeight.Bold)
//                    ),
//                    fontSize = 18.sp,
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//                Spacer(Modifier.weight(1f))
//                Text("Все",
//                    fontFamily = FontFamily(
//                        Font(R.font.inter_medium, FontWeight.Normal)
//                    ),
//                    fontSize = 14.sp,
//                    modifier = Modifier.clickable {  },
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//        item{
//            Spacer(modifier = Modifier.height(8.dp))
//            EventCard("Контрольная по матану", "24.03", {})
//        }
    }
    if(announcementUiState.showCreateDialog){
        AnnouncementCreateDialog(
            title = announcementUiState.titleInput,
            text = announcementUiState.textInput,
            selectedType = announcementUiState.selectedType,
            onTitleChange = onAnnouncementTitleChange,
            onTextChange = onAnnouncementTextChange,
            onTypeChange = onAnnouncementTypeChange,
            onDismissClick = onCloseAnnouncementDialog,
            onCreateClick = onCreateAnnouncement
        )
    }
}
@PreviewLightDark
@Composable
fun HomeTabPreview(){
    val navController = rememberNavController()
    val fakeUser = User(
        id = "1",
        firstName = "Марат",
        lastName = "Цой",
        groupId = "ИКБО-31-24",
        role = "starosta"
    )
    KubikTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeTabContent(
                fakeUser,
                tabNavController = navController,
                innerPadding = PaddingValues(0.dp),
                groupName = "ИКБО-31-24",
                nearestQueueCardState = NearestQueueCardState(
                    "",
                    "Матан",
                    3,
                    System.currentTimeMillis()
                ),
                announcementUiState = AnnouncementUiState(),
                onOpenAnnouncementDialog = {},
                onCloseAnnouncementDialog = {},
                onAnnouncementTitleChange = {},
                onAnnouncementTextChange = {},
                onAnnouncementTypeChange = {},
                onCreateAnnouncement = {}
            )
        }
    }

}