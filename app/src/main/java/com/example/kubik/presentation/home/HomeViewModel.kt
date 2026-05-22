package com.example.kubik.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.usecase.GetQueuesUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NearestQueueCardState(
    val queueId: String,
    val title: String,
    val position: Int,              // текущее место пользователя в очережи
    val submissionDate: Long
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val getQueuesUseCase: GetQueuesUseCase
) : ViewModel() {
    val userState = getUserUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )       // превращаем flow в stateflow
    private val _nearestQueue = MutableStateFlow<NearestQueueCardState?>(null)
    val nearestQueue: StateFlow<NearestQueueCardState?> = _nearestQueue.asStateFlow() // состояние ближайшей очереди
    private var nearestQueueJob: Job? = null    // ссылка на корутину, которая слушает очереди группы,
    // если пользователь поменялся или группа поменялась, нельщя оставлять старую подписку
    init {
        observeUser()   // начинаем следить за текущим пользователем
    }

    private fun observeUser() {
        viewModelScope.launch {
            userState.collect { user ->
                val groupId = user?.groupId
                val userId = user?.id
                if (groupId == null || userId == null) {        // если нет группы или нет пользователя
                    nearestQueueJob?.cancel()                  // отменяем подписку на очереди
                    _nearestQueue.value = null                  // очищаем ближайшую очередь
                    return@collect                              // выходим
                }
                observeNearestQueue(
                    groupId = groupId,
                    userId = userId
                )
            }
        }
    }

    private fun observeNearestQueue(
        groupId: String,
        userId: String
    ) {
        nearestQueueJob?.cancel()               // отменяем старую подписку, чтобы не было дубляжей
        nearestQueueJob = viewModelScope.launch {       // запуск корутины
            getQueuesUseCase(groupId).collect { queues ->       // запуск при любом изменении
                val now = System.currentTimeMillis()
                val nearestQueue = queues
                    .filter { queue -> queue.typedStatus != QueueStatus.CLOSED }    // без закрытых
                    .filter { queue -> queue.submissionDate != null }   // без даты сдачи
                    .filter { queue ->
                        queue.userSlots.containsKey(userId)
                    }                                                   // только мои очереди
                    .minByOrNull { queue ->
                        (queue.submissionDate ?: Long.MAX_VALUE) >= now } // ищем ближайшую по дате сдачи

                _nearestQueue.value = nearestQueue?.let { queue ->
                    NearestQueueCardState(
                        queueId = queue.id,
                        title = queue.title,
                        position = queue.userSlots[userId] ?: 0,
                        submissionDate = queue.submissionDate ?: 0L
                    )
                }       // преобразование в stateflow
            }
        }
    }
}