package com.example.kubik.presentation.group.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikAttention
import com.example.kubik.presentation.theme.KubikAttentionDark
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun CustomExcludeUserDialog(
    isDarkTheme: Boolean,
    userFirstName: String,
    userLastName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ){
            Column( modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            shape = CircleShape,
                            color = if(isDarkTheme) KubikAttentionDark.copy(alpha=0.2f) else KubikAttention
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(R.drawable.attention),
                        contentDescription = "Предупреждение перед удалением",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified

                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Исключить студента?",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Вы собираетесь исключить ${userFirstName} ${userLastName}. Это действие нельзя отменить",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ){
                        Text(
                            text = "Отмена",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).glow(
                            color = KubikAttentionDark,
                            alpha = 0.4f,
                            borderRadius = 8.dp,
                            blurRadius = 16.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KubikAttentionDark
                        )
                    ) {
                        Text(
                            text = "Исключить",
                            color = Color.White,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCustomExcludeUserDialog() {
    KubikTheme() {
        CustomExcludeUserDialog(isSystemInDarkTheme(), "Анна", "Дутова",{}, {})
    }
}