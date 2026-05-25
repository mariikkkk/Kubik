package com.example.kubik.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun BaseJoinStep(
    title: String,
    subtitle: String,
    hint: String,
    icon: Painter,
    buttonText: String,
    explanation: AnnotatedString,
    onSubmit: (String) -> Unit
){
    var inputValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
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
            title,
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
            subtitle,
            fontFamily = FontFamily(
                Font(
                    R.font.inter_regular,
                    FontWeight.Normal
                )
            ),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        CustomTextField(
            value = inputValue,
            hint = hint,
            onValueChange = { inputValue = it },
            icon = icon
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(0.2f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(16.dp)
        ){
            Text(
                explanation,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center

            )
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onSubmit(inputValue.trim()) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .glow(
                    MaterialTheme.colorScheme.primary,
                    if(inputValue.isNotBlank()) 0.5f else 0f,
                    16.dp,
                    24.dp
                ),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContentColor = MaterialTheme.colorScheme.onTertiary
            ),
            enabled = inputValue.isNotBlank()
        ){
            Text(
                buttonText,
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_bold,
                        FontWeight.Bold
                    )
                ),
                fontSize = 16.sp
            )
        }
    }

}


@Composable
fun StudentJoinStep(
    onSubmit: (String) -> Unit
){
    BaseJoinStep(
        "Присоединение к группе",
        "Введите номер вашей группы для отправки запроса",
        "НАЗВАНИЕ ГРУППЫ",
        painterResource(R.drawable.group),
        "Отправить запрос",
        buildAnnotatedString {
            append("Отправьте запрос старосте. До его подтверждения вы сможете пользоваться приложением в ")
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ){
                append("режиме гостя")
            }
        },
        onSubmit
    )
}

@Composable
fun StarostaJoinStep(
    onSubmit: (String) -> Unit
){
    BaseJoinStep(
        "Регистрация старосты",
        "Введите секретный код для получения прав",
        "Секретный код",
        painterResource(R.drawable.invite_code),
        "Подтвердить",
        buildAnnotatedString {
            append("Получить пригласительный код можно, написав")
            withLink(
                link = LinkAnnotation.Url("https://t.me/marrri1k",
                    TextLinkStyles(
                        style = SpanStyle(color = Color(0xFF7C86FF)),
                        hoveredStyle = SpanStyle(textDecoration = TextDecoration.None),
                    )
                ),

                ){
                append(" создателю в Telegram")
            }
        },
        onSubmit
    )
}
@PreviewLightDark
@Composable
fun previewBaseJoinStepStudent(){
    KubikTheme() {
        BaseJoinStep(
            "Присоединение к группе",
            "Введите номер вашей группы для отправки запроса",
            "НАЗВАНИЕ ГРУППЫ",
            painterResource(R.drawable.group),
            "Отправить запрос",
            buildAnnotatedString {
                append("Отправьте запрос старосте. До его подтверждения вы сможете пользоваться приложением в ")
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("режиме гостя")
                }
            },
            {s ->}
        )
    }
}

@PreviewLightDark
@Composable
fun previewBaseJoinStepStarosta(){
    KubikTheme() {
        BaseJoinStep(
            "Регистрация старосты",
            "Введите секретный код для получения прав",
            "Секретный код",
            painterResource(R.drawable.invite_code),
            "Подтвердить",
            buildAnnotatedString {
                append("Получить пригласительный код можно, написав")
                withLink(
                    link = LinkAnnotation.Url("https://t.me/marrri1k",
                        TextLinkStyles(
                            style = SpanStyle(color = Color(0xFF7C86FF)),
                            hoveredStyle = SpanStyle(textDecoration = TextDecoration.None),
                        )
                    ),

                ){
                    append(" создателю в Telegram")
                }
            },
            {s ->}
        )
    }
}