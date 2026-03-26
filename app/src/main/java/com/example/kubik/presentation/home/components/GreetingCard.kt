package com.example.kubik.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.models.User

@Composable
fun GreetingCard(
    user: User?
){
    Card(
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier.fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(32.dp),
                elevation = 16.dp,
                clip = false,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary

            ),
    ){
        Box(
            modifier = Modifier
                .background(brush = Brush.linearGradient(listOf(Color(0xFF615FFF), Color(0xFF9810FA))) )
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text("Привет, ${user?.firstName ?: "Студент"}! ✌🏻",
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold, FontWeight.Bold)
                    ),
                    fontSize = 24.sp,
                    color = Color.White)
                Text("У тебя 2 дедлайна на этой неделе",
                    fontFamily = FontFamily(
                        Font(R.font.inter_regular, FontWeight.Normal)
                    ),
                    fontSize = 14.sp,
                    color = Color.White)
            }
        }

    }
}