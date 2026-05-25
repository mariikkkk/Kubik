package com.example.kubik.presentation.queues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.usecase.CreateQueueUseCase
import com.example.kubik.domain.queues.usecase.DeleteQueueUseCase
import com.example.kubik.domain.queues.usecase.GetQueuesUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueuesListViewModel @Inject constructor(
    private val createQueueUseCase: CreateQueueUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getQueuesUseCase: GetQueuesUseCase,
    private val deleteQueueUseCase: DeleteQueueUseCase
): ViewModel() {

    private val _selectedFilter = MutableStateFlow(QueueFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _currentUserId = MutableStateFlow("")
    val currentUserId = _currentUserId.asStateFlow()

   private val _filteredQueues = MutableStateFlow<List<QueueCardState>>(emptyList())
    val filteredQueues = _filteredQueues.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _showCreateDialog = MutableStateFlow(false)
    val showCreateDialog = _showCreateDialog.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()


    init{
        loadUser()
    }
    private fun observeQueues(groupId: String){
        combine(
            getQueuesUseCase(groupId),
            _selectedFilter,
            _searchQuery
        ){ queues, filter, query  ->
            queues
                .filter { queue ->
                    query.isEmpty() || queue.title.contains(query, ignoreCase = true)
                }
                .filter { queue ->
                    when (filter) {
                        QueueFilter.ALL -> true
                        QueueFilter.ACTIVE -> queue.typedStatus == QueueStatus.ACTIVE
                        QueueFilter.MINE -> _currentUserId.value in queue.participantIds
                                || queue.creatorId == _currentUserId.value
                        QueueFilter.CLOSED -> queue.typedStatus == QueueStatus.CLOSED
                    }
                }.sortedByDescending { it.createdAt }
                .map{ queue ->
                    QueueCardState(
                        queue = queue,
                        slotNumber = queue.userSlots[_currentUserId.value]
                    )
                }
        }.onEach { states ->
            _filteredQueues.value = states
            }.launchIn(viewModelScope)
    }
    private fun loadUser(){
        getUserUseCase().onEach { user ->
            user?.let{
                _currentUserId.value = it.id
                _currentUser.value = it
                val groupId = it.groupId ?: return@let
                observeQueues(groupId)
            }
        }.launchIn(viewModelScope)
    }

    fun setFilter(filter: QueueFilter){
        _selectedFilter.value = filter
    }

    fun createQueue(title: String, totalSlots: Int, submissionDate: Long?){
        viewModelScope.launch {
            _isLoading.value = true
            val user = _currentUser.value
            if(user == null || user.groupId == null){
                _errorMessage.value = "Пользователь не найден"
                _isLoading.value = false
                return@launch
            }
            createQueueUseCase(
                title = title,
                totalSlots = totalSlots,
                groupId = user.groupId,
                creatorId = user.id,
                submissionDate = submissionDate
            ).onFailure {
                _errorMessage.value = it.message ?: "Неизвестная ошибка создания очереди"
            }
            _isLoading.value = false
        }
    }

    fun deleteQueue(queueId: String){
        viewModelScope.launch {
            deleteQueueUseCase(queueId).onFailure { exception ->
                _errorMessage.value = exception.message ?: "Ошибка удаления"
            }
        }
    }

    fun clearError(){
        _errorMessage.value = null
    }

    fun updateSearchQuery(query: String){
        _searchQuery.value = query
    }

    fun showCreateDialog() {
        _showCreateDialog.value = true
    }

    fun hideCreateDialog() {
        _showCreateDialog.value = false
    }
}

enum class QueueFilter {
    ALL, ACTIVE, MINE, CLOSED
}

data class QueueCardState(
    val queue: QueueItem,
    val slotNumber: Int?
)