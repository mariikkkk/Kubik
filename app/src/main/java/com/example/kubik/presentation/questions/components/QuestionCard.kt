package com.example.kubik.presentation.questions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.kubik.domain.questions.models.QuestionItem
import com.example.kubik.domain.questions.models.QuestionStatus
import com.example.kubik.presentation.questions.QuestionCardState
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.utils.toFormattedDate

private data class QuestionStatusColors(
    val backgroundColor: Color,
    val borderColor: Color,
    val textColor: Color
)

private data class QuestionAnswerColors(
    val backgroundColor: Color,
    val borderColor: Color,
    val titleColor: Color,
    val answerColor: Color
)
@Composable
fun QuestionCard(
    state: QuestionCardState,
    onAnswerClick: (String, String) -> Unit,
    onMarkResolvedClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    var answer by remember(state.question.id, state.question.answer) { mutableStateOf(state.question.answer.orEmpty()) }
    var showAnswerInput by remember { mutableStateOf(false) }
    val statusColors = getQuestionStatusColors(state.question.typedStatus)
    val statusText = getQuestionStatusText(state.question.typedStatus)
    val statusIcon = getQuestionStatusIcon(state.question.typedStatus)
    val answerColors = getQuestionAnswerColors()
    Card(
        modifier = modifier.fillMaxWidth().combinedClickable(
            onClick = {},
            onLongClick=  {
                if (state.canDelete) {
                    onDeleteClick(state.question.id)
                }
            }
        ),
        elevation = cardElevation(
            3.dp
        ),
        shape = RoundedCornerShape(24.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Column() {
                    if(state.showAuthorName) {
                        Text(
                            state.question.authorName,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_regular,
                                    FontWeight.Normal
                                )
                            ),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    else{
                        Text("Студент")
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        state.question.createdAt.toFormattedDate("d.MM.yyyy, HH:mm"),
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_regular,
                                FontWeight.Normal
                            )
                        ),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier.background(
                        color = statusColors.backgroundColor,
                        shape = RoundedCornerShape(24.dp)
                    ).border(
                        2.dp,
                        color = statusColors.borderColor,
                        shape = RoundedCornerShape(16.dp)
                    ).padding(horizontal = 12.dp, vertical = 8.dp)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(statusIcon),
                            "Статус ответа на вопрос",
                            tint = statusColors.textColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            statusText,
                            color = statusColors.textColor,
                            fontSize = 12.sp
                        )
                    }

                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                state.question.text,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if(!state.question.answer.isNullOrBlank() && !showAnswerInput){
                answerCard(state, answerColors)
            }
            AnimatedVisibility(
                visible = showAnswerInput,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column{
                    answerTextField(answer, {value -> answer = value})
                    answerRow(
                        answer.isNotBlank(),
                        {
                            answer = state.question.answer.orEmpty()
                            showAnswerInput = false
                        },
                        {
                            val trimmedAnswer = answer.trim()
                            if(trimmedAnswer.isNotEmpty()){
                                onAnswerClick(state.question.id, trimmedAnswer)
                                showAnswerInput = false
                            }
                        }
                    )
                }
            }
            if(state.canMarkResolved && !showAnswerInput){
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onMarkResolvedClick(state.question.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = buttonColors(
                        containerColor = Color(0xFF00BC7D),
                        contentColor = Color(0xFFFFFFFF)
                    )
                )
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(R.drawable.passed),
                            contentDescription = "Отметить решенным",
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Отметить решенным",
                        )
                    }
                }
            }
            if(state.canAnswer && !showAnswerInput){
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { showAnswerInput = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color(0xFFFFFFFF)
                    )
                )
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(R.drawable.reply),
                            contentDescription = "Ответить",
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Ответить"
                        )
                    }
                }
            }
            if(state.canEditAnswer && !showAnswerInput){
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        showAnswerInput = true
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(R.drawable.reply),
                            contentDescription = "Изменить ответ",
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Изменить ответ"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun answerRow(
    canSend: Boolean,
    onCancelClick: () -> Unit,
    onSendClick: () -> Unit
){
    Spacer(Modifier.height(8.dp))
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        TextButton(
            onClick = onCancelClick
        ) {
            Text(
                "Отмена",
                color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onSendClick,
            colors = iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color(0xFFFFFFFF)
            ),
            enabled = canSend
        ){
            Icon(
                painter = painterResource(R.drawable.send),
                contentDescription = "Отправить ответ",
                modifier = Modifier.size(16.dp).offset(y=1.dp)
            )
        }
    }
}
@Composable
private fun answerCard(
    state: QuestionCardState,
    answerColors: QuestionAnswerColors
){
    Spacer(Modifier.height(8.dp))
    if(!state.question.answer.isNullOrBlank()){
        Box(
            modifier = Modifier.background(
                color = answerColors.backgroundColor,
                shape = RoundedCornerShape(16.dp)
            ).border(
                2.dp,
                color = answerColors.borderColor,
                shape = RoundedCornerShape(16.dp)
            ).fillMaxWidth().padding(16.dp)
        ){
            Column(){
                Text(
                    "Ответ",
                    fontSize = 12.sp,
                    color = answerColors.titleColor)
                Text(
                    state.question.answer,
                    fontSize = 16.sp,
                    color = answerColors.answerColor)
            }
        }
    }
}
private fun getQuestionStatusText(status: QuestionStatus): String {
    return when (status) {
        QuestionStatus.WAITING -> "Ожидание"
        QuestionStatus.ANSWERED -> "Есть ответ"
        QuestionStatus.RESOLVED -> "Решён"
    }
}

@Composable
private fun answerTextField(
    answer: String,
    onAnswerChange: (String) -> Unit
){
    Spacer(Modifier.height(8.dp))
    BasicTextField(
        value = answer,
        onValueChange = onAnswerChange,
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
            ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = 16.sp
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(12.dp)
            ){
                if(answer.isEmpty()){
                    Text(
                        "Напишите ответ студенту...",
                        color = Color(0xFF90A1B9)
                    )
                }
                innerTextField()
            }
        }
    )
}
@Composable
private fun getQuestionAnswerColors(): QuestionAnswerColors{
    val isDarkTheme = LocalIsDarkTheme.current
    return if(isDarkTheme){
        QuestionAnswerColors(
            backgroundColor = Color(0xFF312C85).copy(0.1f),
            borderColor = Color(0xFF615FFF).copy(0.2f),
            titleColor = Color(0xFF7C86FF),
            answerColor = Color(0xFFCAD5E2)
        )
    }else{
        QuestionAnswerColors(
            backgroundColor = Color(0xFFEEF2FF).copy(0.5f),
            borderColor = Color(0xFFE0E7FF),
            titleColor = Color(0xFF4F39F6),
            answerColor = Color(0xFF314158)
        )
    }
}
@Composable
private fun getQuestionStatusColors(
    status: QuestionStatus
): QuestionStatusColors {
    val isDarkTheme = LocalIsDarkTheme.current

    return when (status) {
        QuestionStatus.WAITING -> {
            if (isDarkTheme) {
                QuestionStatusColors(
                    backgroundColor = Color(0xFFFE9A00).copy(alpha = 0.1f),
                borderColor = Color(0xFFFE9A00).copy(alpha = 0.2f),
                textColor = Color(0xFFFFB900)
                )
            } else {
                QuestionStatusColors(
                    backgroundColor = Color(0xFFFEF3C6),
                borderColor = Color(0xFFFEE685),
                textColor = Color(0xFFBB3F00)
                )
            }
        }

        QuestionStatus.ANSWERED -> {
            if (isDarkTheme) {
                QuestionStatusColors(
                    backgroundColor = Color(0xFF2B7FFF).copy(alpha = 0.1f),
                    borderColor = Color(0xFF2B7FFF).copy(alpha = 0.2f),
                    textColor = Color(0xFF51a2ff)
                )
            } else {
                QuestionStatusColors(
                    backgroundColor = Color(0xFFDBEAFE),
                    borderColor = Color(0xFFBEDBFF),
                    textColor = Color(0xFF1447E6)
                )
            }
        }

        QuestionStatus.RESOLVED -> {
            if (isDarkTheme) {
                QuestionStatusColors(
                    backgroundColor = Color(0xFF00BC7D).copy(0.1f),
                    borderColor = Color(0xFF00BC7D).copy(0.2f),
                    textColor = Color(0xFF00d492)
                )
            } else {
                QuestionStatusColors(
                    backgroundColor = Color(0xFFD0FAE5),
                    borderColor = Color(0xFFA4F4CF),
                    textColor = Color(0xFF007A55)
                )
            }
        }
    }
}

private fun getQuestionStatusIcon(
    status: QuestionStatus
): Int{
    return when(status){
        QuestionStatus.WAITING -> R.drawable.queue
        QuestionStatus.ANSWERED -> R.drawable.message
        QuestionStatus.RESOLVED -> R.drawable.passed
    }
}

@PreviewLightDark
@Composable
fun previewPanel() {
    KubikTheme {
        QuestionCard(
            state = QuestionCardState(
                question = QuestionItem(
                    id = "question_1",
                    groupId = "group_1",
                    authorId = "user_1",
                    authorName = "Иван Иванов",
                    text = "Здравствуйте, можно ли будет сдать лабораторную работу позже, если не успел на этой неделе?",
                    //answer = "Да, можно. Подойдите после пары в пятницу или запишитесь в следующую очередь.",
                    answeredById = "starosta_1",
                    answeredByName = "Мария Петрова",
                    createdAt = System.currentTimeMillis() - 3_600_000,
                    answeredAt = System.currentTimeMillis() - 1_800_000,
                    resolvedAt = null,
                    status = QuestionStatus.WAITING.value
                ),
                isMine = false,
                showAuthorName = true,
                canAnswer = true,
                canEditAnswer = true,
                canMarkResolved = true,
                canDelete = false
            ),
            onAnswerClick = { _, _ -> },
            onMarkResolvedClick = {s ->},
            onDeleteClick = { s ->},
        )
    }
}