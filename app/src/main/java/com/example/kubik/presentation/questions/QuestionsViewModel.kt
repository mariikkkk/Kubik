package com.example.kubik.presentation.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.questions.models.QuestionItem
import com.example.kubik.domain.questions.models.QuestionStatus
import com.example.kubik.domain.questions.usecase.AnswerQuestionUseCase
import com.example.kubik.domain.questions.usecase.CreateQuestionUseCase
import com.example.kubik.domain.questions.usecase.DeleteQuestionUseCase
import com.example.kubik.domain.questions.usecase.GetQuestionsUseCase
import com.example.kubik.domain.questions.usecase.MarkQuestionResolvedUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val answerQuestionUseCase: AnswerQuestionUseCase,
    private val createQuestionUseCase: CreateQuestionUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val markQuestionResolvedUseCase: MarkQuestionResolvedUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _filteredQuestions = MutableStateFlow<List<QuestionCardState>>(emptyList())
    val filteredQuestions = _filteredQuestions.asStateFlow()
    private val _waitingCount = MutableStateFlow(0)
    val waitingCount = _waitingCount.asStateFlow()
    private val _answeredCount = MutableStateFlow(0)
    val answeredCount = _answeredCount.asStateFlow()
    private val _resolvedCount = MutableStateFlow(0)
    val resolvedCount = _resolvedCount.asStateFlow()

    private val _mineCount = MutableStateFlow(0)
    val mineCount = _mineCount.asStateFlow()
    private val _allCount = MutableStateFlow(0)
    val allCount = _allCount.asStateFlow()

    private val _selectedFilter = MutableStateFlow(QuestionFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()
    private val _currentUserId = MutableStateFlow("")
    private var questionsJob: Job? = null
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    private val _questionIdToDelete = MutableStateFlow<String?>(null)
    val questionIdToDelete = _questionIdToDelete.asStateFlow()
    init{
        loadUser()
    }
    private fun observeQuestions(groupId: String){
        questionsJob?.cancel()
        questionsJob = combine(
            getQuestionsUseCase(groupId),
            _selectedFilter,
            _currentUser
        ) { questions, filter, user ->
            if (user == null) emptyList()
            else {
                _allCount.value = questions.size
                _waitingCount.value = questions.count { it.typedStatus == QuestionStatus.WAITING }
                _answeredCount.value = questions.count{ it.typedStatus == QuestionStatus.ANSWERED }
                _resolvedCount.value = questions.count{ it.typedStatus == QuestionStatus.RESOLVED }
                _mineCount.value = questions.count{ it.authorId == user.id }
                questions
                    .filter { question ->
                        when (filter) {
                            QuestionFilter.ALL -> true
                            QuestionFilter.WAITING -> question.typedStatus == QuestionStatus.WAITING
                            QuestionFilter.ANSWERED -> question.typedStatus == QuestionStatus.ANSWERED
                            QuestionFilter.RESOLVED -> question.typedStatus == QuestionStatus.RESOLVED
                            QuestionFilter.MINE -> question.authorId == user.id
                        }
                    }.sortedByDescending { it.createdAt }
                    .map { questionItem ->
                        questionItem.toCardState(
                            currentUserId = user.id,
                            isStarosta = user.role == "starosta"
                        )
                    }
            }
        }.onEach { states ->
            _filteredQuestions.value = states
        }.launchIn(viewModelScope)
    }
    private fun QuestionItem.toCardState(
        currentUserId: String,
        isStarosta: Boolean
    ): QuestionCardState {
        val isMine = authorId == currentUserId
        return QuestionCardState(
            question = this,
            isMine = isMine,
            showAuthorName = isStarosta,
            canAnswer = typedStatus == QuestionStatus.WAITING && isStarosta,
            canEditAnswer = typedStatus == QuestionStatus.ANSWERED && isStarosta,
            canMarkResolved = typedStatus != QuestionStatus.RESOLVED && isMine,
            canDelete = isMine && typedStatus != QuestionStatus.RESOLVED
        )
    }
    private fun loadUser(){
        getUserUseCase().onEach { user ->
            user?.let{
                _currentUserId.value = it.id
                _currentUser.value = it
                val groupId = it.groupId ?: return@let
                observeQuestions(groupId)
            }
        }.launchIn(viewModelScope)
    }
    fun setFilter(filter: QuestionFilter){
        _selectedFilter.value = filter
    }
    fun createQuestion(questionText: String){
        viewModelScope.launch {
            _isLoading.value = true
            val user = _currentUser.value
            if(user == null){
                _errorMessage.value = "Пользователь не найден"
                _isLoading.value = false
                return@launch
            }else if(user.groupId == null){
                _errorMessage.value = "Пользователь не состоит в группе"
                _isLoading.value = false
                return@launch
            }
            createQuestionUseCase(
                groupId = user.groupId,
                authorId = user.id,
                authorName = "${user.firstName} ${user.lastName}",
                questionText = questionText
            ).onFailure {
                _errorMessage.value = it.message ?: "Неизвестная ошибка создания вопроса"
            }
            _isLoading.value = false
        }
    }
    fun deleteQuestion(questionId: String){
        viewModelScope.launch {
            deleteQuestionUseCase(questionId).onFailure { exception ->
                _errorMessage.value = exception.message ?: "Ошибка удаления"
            }
        }
    }
    fun answerQuestion(questionId: String, answerText: String){
        viewModelScope.launch {
            val user = _currentUser.value
            if(user == null){
                _errorMessage.value = "Пользователь не найден"
                return@launch
            }
            answerQuestionUseCase(
                questionId = questionId,
                answer = answerText,
                answeredById = user.id,
                answeredByName = "${user.firstName} ${user.lastName}"
            ).onFailure {
                _errorMessage.value = it.message ?: "Неизвестная ошибка ответа"
            }
        }
    }
    fun markQuestionResolved(questionId: String){
        viewModelScope.launch {
            markQuestionResolvedUseCase(questionId).onFailure {
                _errorMessage.value = it.message ?: "Ошибка закрытия вопроса"
            }
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }
    fun openDeleteDialog(questionId: String){
        _questionIdToDelete.value = questionId
        _showDeleteDialog.value = true
    }

    fun hideDeleteDialog(){
        _showDeleteDialog.value = false
        _questionIdToDelete.value = null
    }
}

enum class QuestionFilter {
    ALL,
    WAITING,
    ANSWERED,
    RESOLVED,
    MINE
}
data class QuestionCardState(
    val question: QuestionItem,
    val isMine: Boolean,            // принадлежит текущему студенту
    val showAuthorName: Boolean,    // автора видит только староста
    val canAnswer: Boolean,         // может отвечать только староста
    val canEditAnswer: Boolean,     // ответ есть, надо изменить
    val canMarkResolved: Boolean,   // для того, чтобы отметить решенным
    val canDelete: Boolean          // для удаления
)