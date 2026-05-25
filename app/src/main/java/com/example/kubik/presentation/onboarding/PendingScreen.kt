package com.example.kubik.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow


@Composable
fun PendingScreen(
    groupName: String,
    onRefreshClick: () -> Unit,
    onExitClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painterResource(R.drawable.kubik),
                    contentDescription = "Логотип кубика",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Ожидание принятия",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    buildAnnotatedString {
                        append("Ваша заявка на вступление в группу ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ){
                            append(groupName)
                        }
                        append(" отправлена старосте. Пожалуйста, подождите, пока он подтвердит её.")
                    },
                    textAlign = TextAlign.Center,
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
                Button(
                    onClick = onRefreshClick,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .glow(
                            Color(0xFF155DFC),
                            alpha = 0.5f,
                            borderRadius = 16.dp,
                            blurRadius = 24.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF155DFC),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Обновить",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(9.dp))
                    Text(
                        "Обновить статус",
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_medium,
                                FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onExitClick,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.exit),
                        contentDescription = "Сменить аккаунт",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(9.dp))
                    Text(
                        "Сменить аккаунт",
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_medium,
                                FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp
                    )
                }

            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewPendingScreen(){
    KubikTheme() {
        PendingScreen(
            "ИКБО-31-24",
            {},
            {}
        )
    }
}
