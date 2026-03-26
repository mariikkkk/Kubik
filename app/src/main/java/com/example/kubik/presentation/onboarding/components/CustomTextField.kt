package com.example.kubik.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R

@Composable
fun CustomTextField(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    icon: Painter
){
    Box(){
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp),

                    )
                .height(62.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                ),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = icon,
                        contentDescription = "Camera Icon",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ){
                        if(value.isEmpty()){
                            Text(
                                hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.inter_regular,
                                        FontWeight.Medium
                                    )
                                ),
                                fontSize = 18.sp
                            )
                        }
                        innerTextField()
                    }
                }

            }
        )
    }
}