package com.example.kubik.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun RoleSelectionStep(
    isDarkTheme: Boolean,
    onStarosta: () -> Unit,
    onStudent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(230.dp))
        Icon(painter = painterResource(R.drawable.kubik),
            contentDescription = "Иконка Кубика",
            modifier = Modifier
                .size(100.dp)
                .glow(
                    MaterialTheme.colorScheme.primary,
                    0.5f,
                    45.dp,
                    120.dp
                ),
            tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text(
            "Кто вы?",
            fontFamily = FontFamily(
                Font(
                    R.font.inter_bold,
                    FontWeight.Bold
                )
            ),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Выберите свою роль",
            fontFamily = FontFamily(
                Font(
                    R.font.inter_regular,
                    FontWeight.Normal
                )
            ),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            RoleCard(
                modifier = Modifier.weight(1f),
                "Я староста",
                painterResource(R.drawable.starosta_light),
                painterResource(R.drawable.starosta_dark),
                isDarkTheme,
                onStarosta
            )
            RoleCard(
                modifier = Modifier.weight(1f),
                "Я студент",
                painterResource(R.drawable.student_light),
                painterResource(R.drawable.student_dark),
                isDarkTheme,
                onStudent
            )
        }
    }
}

@PreviewLightDark
@Composable
fun previewRoleSelectionStep(){
    KubikTheme() {
        RoleSelectionStep(
            isSystemInDarkTheme(), {}, {}
        )
    }
}