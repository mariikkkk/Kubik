package com.example.kubik.presentation.group.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.models.User
import com.example.kubik.presentation.theme.KubikPrimary
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun PendingRequestCard(
    user: User,
    onRejectClick: () -> Unit,
    onAcceptClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxHeight()
                    .width(8.dp),
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                shape = CircleShape,
                                color = if(isDarkTheme) Color(0xFF615FFF).copy(0.1f) else Color(0xFFEEF2FF)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            user.firstName[0].toString(),
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_bold,
                                    FontWeight.Bold
                                )
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_semibold,
                                FontWeight.SemiBold
                            )
                        ),
                        fontSize = 16.sp,
                    )
                }
                Spacer(Modifier.weight(1f))
                Row() {
                    OutlinedButton(
                        onClick = onRejectClick,
                        modifier = Modifier
                            .height(42.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Отклонить заявку",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                "Отклонить",
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.inter_medium,
                                        FontWeight.Medium
                                    )
                                ),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onAcceptClick,
                        modifier = Modifier
                            .height(42.dp)
                            .weight(1f)
                            .glow(
                                KubikPrimary,
                                alpha = 0.4f,
                                borderRadius = 8.dp,
                                blurRadius = 16.dp
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KubikPrimary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Отклонить заявку",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                "Принять",
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.inter_medium,
                                        FontWeight.Medium
                                    )
                                ),
                                color = Color.White
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
fun PreviewPendingRequestCard(){
    KubikTheme() {
        PendingRequestCard(
            User(
                "1",
                "Марат",
                "Цой",
                ",",
                "student"
            ),
            {},
            {}
        )
    }
}
