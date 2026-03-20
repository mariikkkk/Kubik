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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kubik.R
import com.example.kubik.presentation.home.components.EventCard
import com.example.kubik.presentation.home.components.GreetingCard
import com.example.kubik.presentation.home.components.MainCard
import com.example.kubik.presentation.home.components.NotificationCard
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun HomeTab(
    tabNavController: NavController,
    innerPadding: PaddingValues,
    isDarkTheme: Boolean
){

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
            //.padding(top = 24.dp, start = 16.dp, end = 16.dp)
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding() + 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = innerPadding.calculateBottomPadding() + 16.dp
        ),
    ) {
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(R.drawable.kubik),
                    contentDescription = "Иконка Кубика",
                    modifier = Modifier
                        .size(64.dp)
                        .offset(y = 5.dp),
                    tint = MaterialTheme.colorScheme.primary)
                Column(){
                    Text("КУБИК",
                        fontFamily =
                            FontFamily(
                                Font(
                                    R.font.inter_black,
                                    FontWeight.Normal)),
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text("STUDENT DASHBOARD",
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(
                            Font(R.font.inter_bold, FontWeight.Bold)
                        ),

                        fontSize = 10.sp
                    )
                }

            }
        }
        item{
            Spacer(modifier = Modifier.height(24.dp))
            GreetingCard()
        }
        item{
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                ){
                MainCard(
                    modifier = Modifier.weight(1f),
                    icon = if(isDarkTheme) R.drawable.queuedark else R.drawable.queue,
                    title = "Очереди",
                    subtitle = "Вы не состоите в очереди",
                    onClick = {
                        tabNavController.navigate(NavigationItem.Queues.route){
                            popUpTo(NavigationItem.Home.route){ saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                Spacer(Modifier.width(12.dp))
                MainCard(
                    modifier = Modifier.weight(1f),
                    icon = if(isDarkTheme) R.drawable.deadlinedark else R.drawable.deadline,
                    title = "Дедлайны",
                    subtitle = "Дедлайнов пока нет",
                    onClick = {
                        tabNavController.navigate(NavigationItem.Calendar.route){
                            popUpTo(NavigationItem.Home.route){ saveState = true }
                            launchSingleTop = true
                            restoreState = true
                            }
                    })

            }
        }
        item{
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                if(isDarkTheme) {
                    Image(painter = painterResource(R.drawable.notificationsdark),
                        contentDescription = "Объявления",
                        modifier = Modifier.size(32.dp))
                } else {
                    Image(painter = painterResource(R.drawable.notifications),
                        contentDescription = "Обявления",
                        modifier = Modifier.size(32.dp))
                }
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
                    modifier = Modifier.clickable {  }
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
                        ),
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
            NotificationCard(
                "Перенос пар ⚠\uFE0F",
                "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                "Сегодня, 14:30"
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotificationCard(
                "Перенос пар ⚠\uFE0F",
                "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                "Сегодня, 14:30"
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotificationCard(
                "Перенос пар ⚠\uFE0F",
                "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                "Сегодня, 14:30"
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotificationCard(
                "Перенос пар ⚠\uFE0F",
                "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                "Сегодня, 14:30"
            )
        }
        item{
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                if(isDarkTheme) {
                    Image(
                        painter = painterResource(R.drawable.deadlinedark),
                        contentDescription = "События",
                        modifier = Modifier.size(32.dp)
                    )
                } else{
                    Image(
                        painter = painterResource(R.drawable.deadline),
                        contentDescription = "События",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text("События",
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold, FontWeight.Bold)
                    ),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.weight(1f))
                Text("Все",
                    fontFamily = FontFamily(
                        Font(R.font.inter_medium, FontWeight.Normal)
                    ),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {  },
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            EventCard("Контрольная по матану", "24.03", {})
        }
    }
}

@PreviewLightDark
@Composable
fun HomeTabPreview(){
    val navController = rememberNavController()
    KubikTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ){
            HomeTab(tabNavController = navController, innerPadding = PaddingValues(0.dp), false)
        }

    }

}