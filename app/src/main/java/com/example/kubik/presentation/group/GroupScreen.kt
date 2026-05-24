package com.example.kubik.presentation.group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kubik.R
import com.example.kubik.domain.models.User
import com.example.kubik.presentation.group.components.GroupCard
import com.example.kubik.presentation.group.components.GroupSegmentedControl
import com.example.kubik.presentation.group.components.PendingRequestCard
import com.example.kubik.presentation.group.components.StudentCard
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import kotlinx.coroutines.launch

@Composable
fun GroupScreen(
    viewModel: GroupViewModel = hiltViewModel(),
    onBackClick: () -> Unit
){
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isStarosta = currentUser?.role == "starosta"
    val groupName by viewModel.groupName.collectAsStateWithLifecycle()
    val approvedStudents by viewModel.approvedUsers.collectAsStateWithLifecycle()
    val pendingStudents by viewModel.pendingUsers.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    GroupScreenContent(
        isStarosta,
        groupName,
        selectedTab,
        approvedStudents,
        pendingStudents,
        onBackClick,
        { i -> viewModel.updateTabIndex(i) },
        { userId ->
            viewModel.rejectStudent(userId)
        },
        { userId ->
            viewModel.approveStudent(userId)
        },
        {userId ->
            viewModel.kickStudent(userId)
        }
    )

}

@Composable
fun GroupScreenContent(
    isStarosta: Boolean,
    groupName: String,
    selectedTab: Int,
    approvedStudents: List<User>,
    pendingStudents: List<User>,
    onBackClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onRejectClick: (String) -> Unit,
    onAcceptCick: (String) -> Unit,
    onKickClick: (String) -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val scope = rememberCoroutineScope()
    val pageCount = if(isStarosta) 2 else 1
    val pagerState = rememberPagerState(
        initialPage = selectedTab,
        pageCount = { pageCount }
    )

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress && pagerState.currentPage != selectedTab) {
            onTabSelected(pagerState.currentPage)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .size(40.dp)
                .border(
                    2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
                .shadow(
                    elevation = 1.dp,
                    shape = CircleShape
                )
        ) {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Вернуться назад"
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        GroupCard(
            isStarosta,
            groupName,
            if(isStarosta) {
                painterResource(R.drawable.starosta_light)
            } else {
                painterResource(R.drawable.student_light)
            },
            if(isStarosta){
                painterResource(R.drawable.starosta_dark)
            } else{
                painterResource(R.drawable.student_dark)
            }

        )
        if(isStarosta){
            Spacer(Modifier.height(16.dp))
            GroupSegmentedControl(
                selectedTab = pagerState.targetPage,
                onTabSelected = { newTab ->
                    scope.launch {
                        pagerState.animateScrollToPage(newTab)
                    }
                },
                approvedStudents.size,
                pendingStudents.size

            )
        }
        Spacer(Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            val usersToShow = if (page == 1) pendingStudents else approvedStudents
            if(usersToShow.isEmpty()){
                if(page == 1){
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.outline
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Все заявки одобрены",
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Все заявки разобраны",
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.inter_semibold,
                                        FontWeight.SemiBold
                                    )
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp
                            )
                            Text(
                                "Новых заявок на вступление пока нет",
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.inter_regular,
                                        FontWeight.Normal
                                    )
                                ),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(usersToShow, key = { _, user -> user.id }) {index, user ->
                        if(page == 1 && isStarosta){
                            PendingRequestCard(
                                user,
                                { onRejectClick(user.id) },
                                { onAcceptCick(user.id) }
                            )
                        }else{
                            StudentCard(
                                user,
                                isStarosta,
                                index + 1,
                                { onKickClick(user.id)}


                            )
                        }
                    }
                }
            }
        }
    }
}
@PreviewLightDark
@Composable
fun PreviewGroupScreen(){
    KubikTheme() {
        GroupScreenContent(
            true,
            groupName = "ИКБО-31-24",
            0,
            listOf(
                User(
                    "1",
                    "Марат",
                    "Цой",
                    "",
                    "starosta",
                ),
                User(
                    "2",
                    "Григорий",
                    "Порфирьев",
                    "",
                    "student",
                ),

            ),
            listOf(
                User(
                    "1",
                    "Марат",
                    "Цой",
                    "",
                    "starosta",
                ),
                User(
                    "2",
                    "Григорий",
                    "Порфирьев",
                    "",
                    "student",
                ),
            )
            ,
//            emptyList(),
            {},
            {},
            {s -> },
            {s ->},
            {s ->}
        )
    }
}