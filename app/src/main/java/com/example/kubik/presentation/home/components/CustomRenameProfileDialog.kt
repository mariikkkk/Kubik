package com.example.kubik.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kubik.R
import com.example.kubik.domain.models.User
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun CustomRenameProfileDialog(
    user: User,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
){
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }

    Dialog(
        onDismissRequest = onDismiss
    ){
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(25.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Настройки профиля",
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_bold,
                                FontWeight.Bold
                            )
                        ),
                        fontSize = 20.sp
                    )
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Закрыть окно"
                        )
                    }
                }
                Text(
                    "Имя",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_medium,
                            FontWeight.Medium
                        )
                    ),
                    fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Box(){
                    BasicTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .height(50.dp),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_regular,
                                    FontWeight.Normal
                                )
                            ),
                            fontSize = 16.sp

                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                if(firstName.isEmpty()){
                                    Text(
                                        "Введите имя",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        }

                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Фамилия",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_medium,
                            FontWeight.Medium
                        )
                    ),
                    fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Box(){
                    BasicTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .height(50.dp),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_regular,
                                    FontWeight.Normal
                                )
                            ),
                            fontSize = 16.sp

                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                if(lastName.isEmpty()){
                                    Text(
                                        "Введите фамилию",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        }

                    )
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { onConfirm(firstName, lastName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .glow(
                        MaterialTheme.colorScheme.primary,
                        0.5f,
                        16.dp,
                        32.dp)
                        ,
                    shape = RoundedCornerShape(12.dp),
                    enabled = if(firstName.isNotBlank() && lastName.isNotBlank()) true else false,
                ) {
                    Text(
                        "Сохранить",
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

@PreviewLightDark
@Composable
fun PreviewCustomRenameProfileDialog(){
    KubikTheme() {
        CustomRenameProfileDialog(User("1", "Марат", "Цой"),{}, { f, d ->})
    }
}