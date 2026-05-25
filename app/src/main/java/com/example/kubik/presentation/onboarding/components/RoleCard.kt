package com.example.kubik.presentation.onboarding.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun RoleCard(
    modifier: Modifier = Modifier,
    title: String,
    iconLight: Painter,
    iconDark: Painter,
    isDarkTheme: Boolean,
    onContinue: () -> Unit
){
    Card(
        onClick = onContinue,
        modifier = modifier
            .heightIn(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = if(isDarkTheme) iconDark else iconLight,
                contentDescription = "Иконка роли",
                modifier = Modifier.size(64.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.height(16.dp))
            Text(
                title,
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_bold,
                        FontWeight.Bold
                    )
                ),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
                )
        }
    }

}

@PreviewLightDark
@Composable
fun previewRoleCard(){
    KubikTheme() {
        RoleCard(
            modifier = Modifier,
            "Я староста",
            painterResource(R.drawable.student_light),
            painterResource(R.drawable.student_dark),
            isDarkTheme = isSystemInDarkTheme(),
            {})
    }
}