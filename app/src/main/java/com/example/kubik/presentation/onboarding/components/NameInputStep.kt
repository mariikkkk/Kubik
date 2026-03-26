package com.example.kubik.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
fun NameInputStep(
    initialFirstName: String,
    initialLastName: String,
    onContinue: (String, String) -> Unit
){
    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(120.dp))
        Icon(painter = painterResource(R.drawable.kubik),
            contentDescription = "Иконка Кубика",
            modifier = Modifier
                .size(100.dp)
                .glow(
                    MaterialTheme.colorScheme.primary,
                    0.5f,
                    90.dp,
                    64.dp
                ),
            tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text(
            "Давайте знакомиться",
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
            "Как к вам обращаться?",
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
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ){
            Icon(painter = painterResource(R.drawable.photo),
                contentDescription = "Добавить фото",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(16.dp))
        CustomTextField(
            firstName,
            "Имя",
            onValueChange = { firstName = it },
            icon = painterResource(R.drawable.people)
        )
        Spacer(Modifier.height(16.dp))
        CustomTextField(
            lastName,
            "Фамилия",
            onValueChange = { lastName = it },
            icon = painterResource(R.drawable.people)
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                onContinue(firstName.trim(), lastName.trim())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .glow(
                    MaterialTheme.colorScheme.primary,
                    if(firstName.isNotBlank() && lastName.isNotBlank()) 0.5f else 0f,
                    16.dp,
                    24.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContentColor = MaterialTheme.colorScheme.onTertiary
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = firstName.isNotBlank() && lastName.isNotBlank()
        ){
            Text(
                "Продолжить",
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_bold,
                        FontWeight.Bold
                    )
                ),
                fontSize = 16.sp
            )
            Spacer(Modifier.width(8.dp)) 
            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = "Продолжить",
                modifier = Modifier.size(16.dp).padding(top = 1.dp)
            )
        }


    }

}


@PreviewLightDark
@Composable
fun previewNameInputStep(){
    KubikTheme() {
        NameInputStep(initialFirstName = "", initialLastName = "", onContinue = { _, _ -> })

    }
}