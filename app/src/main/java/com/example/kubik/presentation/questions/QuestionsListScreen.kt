package com.example.kubik.presentation.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kubik.R
import com.example.kubik.domain.questions.models.QuestionItem
import com.example.kubik.domain.questions.models.QuestionStatus
import com.example.kubik.presentation.questions.components.QuestionCard
import com.example.kubik.presentation.questions.components.QuestionCreateCard
import com.example.kubik.presentation.questions.components.QuestionDeleteDialog
import com.example.kubik.presentation.questions.components.QuestionFilterTabs
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import kotlin.math.min

@Composable
fun QuestionsListScreen(
    innerPadding: PaddingValues,
    viewModel: QuestionsViewModel = hiltViewModel()
){
    val questions by viewModel.filteredQuestions.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allCount by viewModel.allCount.collectAsStateWithLifecycle()
    val waitingCount by viewModel.waitingCount.collectAsStateWithLifecycle()
    val answeredCount by viewModel.answeredCount.collectAsStateWithLifecycle()
    val resolvedCount by viewModel.resolvedCount.collectAsStateWithLifecycle()
    val mineCount by viewModel.mineCount.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val questionIdToDelete by viewModel.questionIdToDelete.collectAsStateWithLifecycle()
    val isStarosta = currentUser?.role == "starosta"
    QuestionsListScreenContent(
        questions = questions,
        innerPadding = innerPadding,
        selectedFilter = selectedFilter,
        isStarosta = isStarosta,
        allCount = allCount,
        waitingCount = waitingCount,
        resolvedCount = resolvedCount,
        answeredCount = answeredCount,
        mineCount = mineCount,
        onFilterChange = viewModel::setFilter,
        onAnswerClick = viewModel::answerQuestion,
        onDeleteClick =  viewModel::openDeleteDialog,
        onCreateQuestionClick = viewModel::createQuestion,
        onMarkResolvedClick = viewModel::markQuestionResolved
    )
    if(showDeleteDialog){
        QuestionDeleteDialog(
            onDismiss = viewModel::hideDeleteDialog,
            onConfirm = {
                questionIdToDelete?.let{ questionId ->
                    viewModel.deleteQuestion(questionId)
                }
                viewModel.hideDeleteDialog()
            }
        )
    }

}

@Composable
fun QuestionsListScreenContent(
    innerPadding: PaddingValues,
    questions: List<QuestionCardState>,
    selectedFilter: QuestionFilter,
    isStarosta: Boolean,
    allCount: Int,
    waitingCount: Int,
    answeredCount: Int,
    resolvedCount: Int,
    mineCount: Int,
    onFilterChange: (QuestionFilter) -> Unit,
    onAnswerClick: (String, String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onCreateQuestionClick: (String) -> Unit,
    onMarkResolvedClick: (String) -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    var questionText by remember { mutableStateOf("") }
    val filters = if(isStarosta){
        listOf(
            QuestionFilter.ALL,
            QuestionFilter.WAITING,
            QuestionFilter.ANSWERED,
            QuestionFilter.RESOLVED
        ) 
    }else{
        listOf(
            QuestionFilter.ALL,
            QuestionFilter.MINE,
            QuestionFilter.WAITING,
            QuestionFilter.ANSWERED,
            QuestionFilter.RESOLVED
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                start = 16.dp,
                end = 16.dp
            )
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            if(!isStarosta){
                item {
                    QuestionCreateCard(
                        questionText = questionText,
                        onQuestionChange = {
                            questionText = it
                        },
                        onSendClick = {
                            val trimmedText = questionText.trim()
                            if(trimmedText.isNotEmpty()){
                                onCreateQuestionClick(trimmedText)
                                questionText = ""
                            }
                        }
                    )
                }
            }
            item{
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.filter),
                        contentDescription = "Фильтры вопросов",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Вопросы группы",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_medium
                            )
                        ),
                        fontSize = 20.sp
                    )
                }

            }
            item {
                QuestionFilterTabs(
                    filters = filters,
                    selectedFilter = selectedFilter,
                    onFilterChange = onFilterChange,
                    allCount = allCount,
                    waitingCount = waitingCount,
                    answeredCount = answeredCount,
                    resolvedCount = resolvedCount,
                    mineCount = mineCount,
                    Modifier.fillMaxWidth()
                )
            }
            if(questions.isEmpty()){
                item {
                    emptyCard(isDarkTheme)
                }
            } else{
                items(
                    items = questions,
                    key = { it.question.id }
                ){ state ->
                    QuestionCard(
                        state = state,
                        onAnswerClick = onAnswerClick,
                        onMarkResolvedClick = onMarkResolvedClick,
                        onDeleteClick = onDeleteClick,
                        modifier = Modifier.fillMaxWidth().animateItem()
                    )
                }
            }
        }
    }

}

@Composable
private fun emptyCard(
    isDarkTheme: Boolean
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if(isDarkTheme) Color(0xFF0F172B) else Color(0xFFFFFFFF),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.outline
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .background(
                        color = if(isDarkTheme) Color(0xFF1D293D) else Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(16.dp)
                    ).padding(12.dp)
            ){
                Icon(
                    painter = painterResource(R.drawable.empty),
                    contentDescription = "Вопросов пока еще нет",
                    tint = Color(0xFF90A1B9)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Здесь пока пусто",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Задайте свой первый вопрос старосте",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 12.sp
            )
        }
    }
}
@PreviewLightDark
@Composable
fun previewQuestionsScreen() {
    KubikTheme {
        QuestionsListScreenContent(
            innerPadding = PaddingValues(0.dp),
            questions =
                listOf(
                QuestionCardState(
                    question = QuestionItem(
                        id = "question_1",
                        groupId = "group_1",
                        authorId = "user_1",
                        authorName = "Иван Иванов",
                        text = "Когда будет следующая пересдача по математике?",
                        answer = null,
                        createdAt = System.currentTimeMillis() - 3_600_000,
                        status = QuestionStatus.WAITING.value
                    ),
                    isMine = false,
                    showAuthorName = true,
                    canAnswer = false,
                    canEditAnswer = false,
                    canMarkResolved = true,
                    canDelete = false
                ),
                QuestionCardState(
                    question = QuestionItem(
                        id = "question_2",
                        groupId = "group_1",
                        authorId = "user_2",
                        authorName = "Мария Петрова",
                        text = "Можно ли отправить лабораторную работу в электронном виде?",
                        answer = "Да, можно. Отправьте файл в раздел с материалами и напишите преподавателю.",
                        answeredById = "starosta_1",
                        answeredByName = "Алексей Смирнов",
                        createdAt = System.currentTimeMillis() - 7_200_000,
                        answeredAt = System.currentTimeMillis() - 3_000_000,
                        status = QuestionStatus.ANSWERED.value
                    ),
                    isMine = false,
                    showAuthorName = true,
                    canAnswer = false,
                    canEditAnswer = true,
                    canMarkResolved = true,
                    canDelete = false
                ),
                QuestionCardState(
                    question = QuestionItem(
                        id = "question_3",
                        groupId = "group_1",
                        authorId = "user_3",
                        authorName = "Дарья Кузнецова",
                        text = "Где посмотреть список тем для докладов?",
                        answer = "Список тем лежит в файлах группы, папка «Доклады».",
                        answeredById = "starosta_1",
                        answeredByName = "Алексей Смирнов",
                        createdAt = System.currentTimeMillis() - 86_400_000,
                        answeredAt = System.currentTimeMillis() - 80_000_000,
                        resolvedAt = System.currentTimeMillis() - 70_000_000,
                        status = QuestionStatus.RESOLVED.value
                    ),
                    isMine = false,
                    showAuthorName = true,
                    canAnswer = false,
                    canEditAnswer = false,
                    canMarkResolved = false,
                    canDelete = false
                )
            ),
            selectedFilter = QuestionFilter.ALL,
            isStarosta = false,
            allCount = 3,
            waitingCount = 1,
            answeredCount = 1,
            resolvedCount = 1,
            mineCount = 0,
            onFilterChange = {},
            onAnswerClick = { _, _ -> },
            onDeleteClick = {},
            onCreateQuestionClick = {},
            onMarkResolvedClick = {}
        )
    }
}