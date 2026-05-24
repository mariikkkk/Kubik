package com.example.kubik.presentation.questions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.kubik.presentation.queues.components.QueueDeleteDialog
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun QuestionDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
){
    val isDarkTheme = LocalIsDarkTheme.current
    val deleteColorContainer = if(isDarkTheme) Color(0xFF35122E) else Color(0xFFffe4e6)
    val deleteColorContent = if(isDarkTheme) Color(0xFFFF2056) else Color(0xFFFF2056)
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
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
                            color = deleteColorContainer
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(R.drawable.delete_queue),
                        contentDescription = "Предупреждение перед удалением",
                        modifier = Modifier.size(40.dp).offset(y=(-2).dp),
                        tint = deleteColorContent

                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Удалить вопрос",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Вы уверены, что хотите удалить этот вопрос? Это действие нельзя отменить.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ){
                        Text(
                            text = "Отмена",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .glow(
                                deleteColorContent,
                                alpha = 0.5f,
                                5.dp,
                                20.dp
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = deleteColorContent
                        )
                    ) {
                        Text(
                            text = "Удалить",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewDelete(){
    KubikTheme() {
        QuestionDeleteDialog({}, {} )
    }
}