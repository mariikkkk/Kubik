package com.example.kubik.presentation.questions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun QuestionCreateCard(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = cardElevation(
            3.dp
        ),
        shape = RoundedCornerShape(24.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        ) {
        Column(
            Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(R.drawable.question),
                    contentDescription = "Вопрос",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Задайте свой вопрос",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_medium,
                            FontWeight.Medium
                        )
                    ),
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        2.dp,
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
            ){
                BasicTextField(
                    value = questionText,
                    onValueChange = onQuestionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 12.dp,
                            end = 64.dp,
                            bottom = 56.dp
                        ),
                    singleLine = false,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                        ){
                            if(questionText.isEmpty()){
                                Text(
                                    "Напишите свой вопрос...",
                                    color = Color(0xFF90A1B9)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                IconButton(
                    onClick = onSendClick,
                    enabled = questionText.isNotBlank(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(38.dp),
                    colors = iconButtonColors(
                        contentColor = Color(0xFFFFFFFF),
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ){
                    Icon(
                        painter = painterResource(R.drawable.send),
                        contentDescription = "Отправить вопрос",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

        }
    }
}

@PreviewLightDark
@Composable
fun previewCreateCard(){
    KubikTheme {
        QuestionCreateCard(
            "",
            {s -> },
            {}
        )
    }
}