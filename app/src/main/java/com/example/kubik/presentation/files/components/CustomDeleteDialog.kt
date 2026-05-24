package com.example.kubik.presentation.files.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun CustomDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
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
                            color = MaterialTheme.colorScheme.error.copy(0.3f)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(R.drawable.attention),
                        contentDescription = "Предупреждение перед удалением",
                        modifier = Modifier.size(40.dp).offset(y=(-2).dp),
                        tint = MaterialTheme.colorScheme.error

                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Вы действительно хотите это удалить?",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Это действие нельзя отменить. Восстановить удаленное не получится!",
                    style = MaterialTheme.typography.bodyMedium,
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
                        onClick = onConfirm,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .glow(
                                MaterialTheme.colorScheme.error,
                                alpha = 0.5f,
                                5.dp,
                                20.dp
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
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
fun PreviewDeleteDialog(){
    KubikTheme() {
        CustomDeleteDialog({}, {})
    }
}