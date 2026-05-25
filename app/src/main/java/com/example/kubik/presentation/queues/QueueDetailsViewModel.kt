package com.example.kubik.presentation.queues

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.domain.queues.usecase.CloseQueueUseCase
import com.example.kubik.domain.queues.usecase.DeleteQueueUseCase
import com.example.kubik.domain.queues.usecase.GetQueueByIdUseCase
import com.example.kubik.domain.queues.usecase.GetQueueSlotsUseCase
import com.example.kubik.domain.queues.usecase.JoinQueueUseCase
import com.example.kubik.domain.queues.usecase.LeaveQueueUseCase
import com.example.kubik.domain.queues.usecase.StartQueueUseCase
import com.example.kubik.domain.queues.usecase.UpdateQueueUseCase
import com.example.kubik.domain.queues.usecase.UpdateSlotStatusUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueueDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQueueSlotsUseCase: GetQueueSlotsUseCase,
    private val updateSlotStatusUseCase: UpdateSlotStatusUseCase,
    private val leaveQueueUseCase: LeaveQueueUseCase,
    private val joinQueueUseCase: JoinQueueUseCase,
    private val deleteQueueUseCase: DeleteQueueUseCase,
    private val startQueueUseCase: StartQueueUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val closeQueueUseCase: CloseQueueUseCase,
    private val getQueueByIdUseCase: GetQueueByIdUseCase,
    private val updateQueueUseCase: UpdateQueueUseCase
): ViewModel() {
    private val queueId: String = savedStateHandle["queueId"] ?: ""
    private val _currentQueue = MutableStateFlow<QueueItem?>(null)
    val currentQueue = _currentQueue.asStateFlow()

    private val _slots = MutableStateFlow<List<SlotItem>>(emptyList())
    val slots = _slots.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _rebookDialog = MutableStateFlow<RebookDialogState?>(null)
    val rebookDialog = _rebookDialog.asStateFlow()

    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog = _showEditDialog.asStateFlow()

    private val _showCloseDialog = MutableStateFlow(false)
    val showCloseDialog = _showCloseDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog = _showDeleteDialog.asStateFlow()

    val isStarosta = _currentUser.map { it?.role == "starosta" }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    val slotList: StateFlow<List<SlotDisplayItem>> = combine(
        //объединение три флоу в один, меняется при изменении любого из них
        _slots,     // Новый слот? пересчет
        _currentUser,       // загрузился пользователь? пересчет
        _currentQueue       //Запуск очереди? пересчет
    ){ slots, user, queue ->
        val total = queue?.totalSlots ?: 0
        val isActive = queue?.typedStatus == QueueStatus.ACTIVE
        val activeSlot = queue?.currentActiveSlot
        (1..total).map { slotNumber ->
            //Есть ли кто то на этом месте?
            val slot = slots.find { it.slotNumber == slotNumber }
            val progress = queue?.currentActiveSlot ?: slots.filter {
                it.typedStatus == SlotStatus.PASSED
                        || it.typedStatus == SlotStatus.FAILED
            }.maxOfOrNull{ it.slotNumber }
            //Если очереди запущена и есть текущий сдающий и номер места меньше текущего сдающего и место свободно
            val isBlocked = isActive && activeSlot != null &&
                    progress != null &&
                    slotNumber < progress &&
                    slot == null
            SlotDisplayItem(
                slotNumber = slotNumber,
                slot = slot,
                isBlocked = isBlocked,
                isCurrentUser = user?.id == slot?.userId,
                isActiveSlot = isActive && activeSlot == slotNumber
            )
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val myWaitingSlot: StateFlow<SlotDisplayItem?> = slotList.map { slots ->
        slots.find { it.isCurrentUser && it.slot?.typedStatus == SlotStatus.WAITING
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )


    init{
        loadData()
    }

    private fun loadData(){
        viewModelScope.launch {
            getQueueSlotsUseCase(queueId).collect{ slots ->
                _slots.value = slots
            }
        }
        viewModelScope.launch {
            getUserUseCase().collect{ user ->
                _currentUser.value = user
            }
        }
        viewModelScope.launch {
            getQueueByIdUseCase(queueId).collect{ queue ->
                _currentQueue.value = queue
            }
        }
    }

    fun onSlotClick(slotNumber: Int) {
        val existingSlot = _slots.value.find {
            it.userId == _currentUser.value?.id && it.typedStatus == SlotStatus.WAITING
        }
        if (existingSlot != null) {
            _rebookDialog.value = RebookDialogState(
                fromSlot = existingSlot.slotNumber,
                toSlot = slotNumber
            )
        } else {
            viewModelScope.launch {
                val user = _currentUser.value ?: return@launch
                val queue = _currentQueue.value ?: return@launch
                joinQueueUseCase(
                    queueId,
                    slotNumber,
                    user.id,
                    user.firstName + " " + user.lastName,
                    prevSlotNumber = null,
                    queueStatus = queue.typedStatus,
                    currentActiveSlot = queue.currentActiveSlot
                ).onFailure {
                    _errorMessage.value = it.message ?: "Ошибка записи"
                }
            }
        }
    }

    fun confirmRebook(fromSlot: Int, toSlot: Int){
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val queue = _currentQueue.value ?: return@launch
            joinQueueUseCase(
                queueId,
                toSlot,
                user.id,
                user.firstName + " " + user.lastName,
                prevSlotNumber = fromSlot,
                queueStatus = queue.typedStatus,
                currentActiveSlot = queue.currentActiveSlot
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка записи"
            }.onSuccess {
                _rebookDialog.value = null
            }
        }
    }

    fun leaveQueue(slotNumber: Int){
        viewModelScope.launch {
            leaveQueueUseCase(
                queueId,
                slotNumber,
                _currentQueue.value?.currentActiveSlot
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка выхода из очереди"
            }
        }
    }

    fun dismissRebook(){
        _rebookDialog.value = null
    }

    fun clearErrorMessage(){
        _errorMessage.value = null
    }

    fun deleteQueue(){
        viewModelScope.launch {
            deleteQueueUseCase(
                queueId
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка удаления очереди"
            }
        }
    }

    fun startQueue(){
        viewModelScope.launch {
            startQueueUseCase(
                queueId
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка запуска очереди"
                Log.e("QueueDetailsViewModel", "Ошибка запуска очереди", it)
            }
        }
    }

    fun updateSlotStatus(slotNumber: Int, newStatus: SlotStatus){
        viewModelScope.launch{
            updateSlotStatusUseCase(
                queueId,
                slotNumber,
                newStatus
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка обновления статуса слота"
            }
        }
    }

    fun closeQueue(shouldMigrate: Boolean, newTotalSlots: Int){
        viewModelScope.launch {
            closeQueueUseCase(
                _currentQueue.value ?: return@launch,
                _slots.value,
                shouldMigrate,
                newTotalSlots
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка закрытия очереди"
            }
        }
    }

    fun openEditDialog(){
        _showEditDialog.value = true
    }

    fun hideEditDialog(){
        _showEditDialog.value = false
    }

    fun editQueue(newTitle: String, newTotalSlots: Int){
        viewModelScope.launch {
            updateQueueUseCase(
                queueId,
                newTitle,
                newTotalSlots
            ).onFailure {
                _errorMessage.value = it.message ?: "Ошибка обновления очереди"
            }
        }
    }

    fun markCurrentSlotPassed(){
        val activeSlotNumber = _currentQueue.value?.currentActiveSlot ?: return
        val userId = _currentUser.value?.id ?: return
        val activeSlot = _slots.value.find { it.slotNumber == activeSlotNumber } ?: return
        if (activeSlot.userId != userId){
            _errorMessage.value = "Сейчас не ваша очередь!"
            return
        }
        updateSlotStatus(activeSlotNumber, SlotStatus.PASSED)
    }

    fun markCurrentSlotFailed(){
        val activeSlotNumber = _currentQueue.value?.currentActiveSlot ?: return
        val userId = _currentUser.value?.id ?: return
        val activeSlot = _slots.value.find { it.slotNumber == activeSlotNumber } ?: return
        if (activeSlot.userId != userId){
            _errorMessage.value = "Сейчас не ваша очередь!"
            return
        }
        updateSlotStatus(activeSlotNumber, SlotStatus.FAILED)
    }

    fun openCloseDialog(){
        _showCloseDialog.value = true
    }

    fun hideCloseDialog(){
        _showCloseDialog.value = false
    }

    fun openDeleteDialog(){
        _showDeleteDialog.value = true
    }

    fun hideDeleteDialog(){
        _showDeleteDialog.value = false
    }

    fun closeQueueWithoutMigration(){
        val queue = _currentQueue.value ?: return
        closeQueue(false, queue.totalSlots)
    }

    fun closeQueueWithMigration(newTotalSlots: Int){
        closeQueue(true, newTotalSlots)
    }

}

data class RebookDialogState(
    val fromSlot: Int,
    val toSlot: Int
)

data class SlotDisplayItem(
    val slotNumber: Int,
    val slot: SlotItem?,            // Если null, то место свободно
    val isBlocked: Boolean,         // это место уже сгорело и туда нельзя записаться?
    val isCurrentUser: Boolean,     // это мой слот?
    val isActiveSlot: Boolean       // этот слот сдает сейчас?
)