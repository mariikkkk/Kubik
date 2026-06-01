package com.example.kubik.presentation.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.domain.announcement.usecase.CreateAnnouncementUseCase
import com.example.kubik.domain.announcement.usecase.DeleteAnnouncementUseCase
import com.example.kubik.domain.announcement.usecase.GetAnnouncementsUseCase
import com.example.kubik.domain.models.User
import com.example.kubik.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val createAnnouncementUseCase: CreateAnnouncementUseCase,
    private val getAnnouncementsUseCase: GetAnnouncementsUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AnnouncementUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<AnnouncementEvent>()
    val events = _events.asSharedFlow()
    private var announcementsJob: Job? = null
    init{
        loadUser()
    }
    private fun loadUser(){
        getUserUseCase().onEach { user ->
            if(user == null){
                _events.emit(
                    AnnouncementEvent.ShowError("Пользователь не найден")
                )
                return@onEach
            }
            _uiState.update {
                it.copy(currentUser = user)
            }
            val groupId = user.groupId
            if(groupId == null){
                _events.emit(
                    AnnouncementEvent.ShowError("Пользователь не состоит в группе")
                )
                return@onEach
            }
            observeAnnouncements(groupId)
        }.launchIn(viewModelScope)
    }
    private fun observeAnnouncements(groupId: String){
        announcementsJob?.cancel()
        announcementsJob = getAnnouncementsUseCase(groupId)
            .onEach { announcements ->
                _uiState.update { state ->
                    val sortedAnnouncements = sortAnnouncements(announcements)
                    state.copy(
                        announcements = sortedAnnouncements,
                        filteredAnnouncements = filterAnnouncements(
                            announcements = sortedAnnouncements,
                            filter = state.selectedFilter
                        )
                    )
                }
            }.launchIn(viewModelScope)
    }
    fun setFilter(filter: AnnouncementFilter){
        _uiState.update { state ->
            state.copy(
                selectedFilter = filter,
                filteredAnnouncements = filterAnnouncements(
                    announcements = state.announcements,
                    filter = filter)
            )
        }
    }
    fun openCreateDialog(){
        _uiState.update{
            it.copy(showCreateDialog = true)
        }
    }
    fun closeCreateDialog(){
        _uiState.update {
            it.copy(
                showCreateDialog = false,
                titleInput = "",
                textInput = "",
                selectedType = AnnouncementType.NORMAL
            )
        }
    }
    fun updateTitle(value: String){
            _uiState.update {
                it.copy(titleInput = value)
            }
    }
    fun updateText(value: String){
        _uiState.update {
            it.copy(textInput = value)
        }
    }
    fun selectType(type: AnnouncementType){
        _uiState.update{
            it.copy(selectedType = type)
        }
    }
    fun createAnnouncement(){
        viewModelScope.launch {
            val state = _uiState.value
            val user = state.currentUser
            if(user == null){
                _events.emit(
                    AnnouncementEvent.ShowError("Пользователь не найден")
                )
                return@launch
            }
            val groupId = user.groupId
            if(groupId == null){
                _events.emit(
                    AnnouncementEvent.ShowError("Пользователь не состоит в группе")
                )
                return@launch
            }
            _uiState.update {
                it.copy(isLoading = true)
            }
            createAnnouncementUseCase(
                groupId = groupId,
                title = state.titleInput,
                text = state.textInput,
                type = state.selectedType,
                authorId = user.id,
                authorName = "${user.firstName} ${user.lastName}"
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showCreateDialog = false,
                        textInput = "",
                        titleInput = "",
                        selectedType = AnnouncementType.NORMAL
                    )
                }
                _events.emit(
                    AnnouncementEvent.AnnouncementCreated
                )
            }.onFailure { exception ->
                _uiState.update {
                it.copy(isLoading = false)
                }
                _events.emit(
                    AnnouncementEvent.ShowError(
                        exception.message ?: "Ошибка создания объявления"
                    )
                )
            }
        }
    }
    fun deleteAnnouncement(announcementId: String){
        viewModelScope.launch {
            deleteAnnouncementUseCase(announcementId)
                .onFailure { exception ->
                    _events.emit(
                        AnnouncementEvent.ShowError(
                            exception.message ?: "Ошибка удаления объявления"
                        )
                    )
                }
        }
    }
    private fun sortAnnouncements(
        announcements: List<AnnouncementItem>
    ): List<AnnouncementItem>{
        return announcements.sortedWith (
            compareByDescending<AnnouncementItem> {
                it.type == AnnouncementType.IMPORTANT.value
            }.thenByDescending {
                it.createdAt
            }
        )
    }
    private fun filterAnnouncements(
        announcements: List<AnnouncementItem>,
        filter: AnnouncementFilter
    ): List<AnnouncementItem>{
        return announcements.filter { announcementItem ->
            when(filter){
                AnnouncementFilter.ALL -> true
                AnnouncementFilter.IMPORTANT -> announcementItem.type == AnnouncementType.IMPORTANT.value
                AnnouncementFilter.NORMAL -> announcementItem.type == AnnouncementType.NORMAL.value
            }
        }
    }
}

enum class AnnouncementFilter{
    ALL,
    IMPORTANT,
    NORMAL
}

data class AnnouncementUiState(
    val announcements: List<AnnouncementItem> = emptyList(),
    val filteredAnnouncements: List<AnnouncementItem> = emptyList(),
    val selectedFilter: AnnouncementFilter = AnnouncementFilter.ALL,
    val selectedType: AnnouncementType = AnnouncementType.NORMAL,
    val titleInput: String = "",
    val textInput: String = "",
    val showCreateDialog: Boolean = false,
    val isLoading: Boolean = false,
    val currentUser: User? = null
){
    val isStarosta: Boolean
        get() = currentUser?.role == "starosta"
    val allCount: Int
        get() = announcements.size
    val importantCount: Int
        get() = announcements.count{
            it.type == AnnouncementType.IMPORTANT.value
        }
    val normalCount: Int
        get() = announcements.count{
            it.type == AnnouncementType.NORMAL.value
        }
}

sealed interface AnnouncementEvent{
    data class ShowError(val message: String) : AnnouncementEvent
    data object AnnouncementCreated : AnnouncementEvent
}