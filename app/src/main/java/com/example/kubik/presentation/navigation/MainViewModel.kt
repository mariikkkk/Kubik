package com.example.kubik.presentation.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.AppState
import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.repository.UserPreferencesRepository
import com.example.kubik.domain.usecase.ClearUserUseCase
import com.example.kubik.domain.usecase.GetCurrentUserUseCase
import com.example.kubik.domain.usecase.GetGroupByIdUseCase
import com.example.kubik.domain.usecase.GetThemeModeUseCase
import com.example.kubik.domain.usecase.GetUserProfileFromFirestoreUseCase
import com.example.kubik.domain.usecase.LogoutUseCase
import com.example.kubik.domain.usecase.ObserveAuthStateUseCase
import com.example.kubik.domain.usecase.ObserveUserProfile
import com.example.kubik.domain.usecase.RemoveStudentUseCase
import com.example.kubik.domain.usecase.SetThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeUserProfileUseCase: ObserveUserProfile,
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val clearUserUseCase: ClearUserUseCase,
    private val removeStudentUseCase: RemoveStudentUseCase
): ViewModel() {

    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    var profileJob: Job? = null
    var authJob: Job? = null

    private val _groupName = MutableStateFlow("Загрузка...")
    val groupName = _groupName.asStateFlow()

    init {
        checkUserStatus()
    }
    fun checkUserStatus(){
        authJob?.cancel()
        authJob = viewModelScope.launch {
            profileJob?.cancel()
            observeAuthStateUseCase().collect { authState ->
                when(authState){
                    is AuthState.Loading -> {
                        _appState.value = AppState.Loading
                    }
                    is AuthState.Unauthenticated -> {
                        _appState.value = AppState.Login
                    }
                    is AuthState.Authenticated -> {
                        val userRes = getCurrentUserUseCase()
                        userRes.onSuccess { user ->
                            if (user != null){
                                val userId = user.id
                                profileJob = launch {
                                    observeUserProfileUseCase(userId).collect { user ->
                                        if (user?.groupId != null){
                                            if (user.role == "starosta" || user.status == "approved"){
                                                _appState.value = AppState.Home
                                            } else {
                                                fetchGroupName(user.groupId   )
                                                _appState.value = AppState.Pending
                                            }
                                        } else{
                                            _appState.value = AppState.Onboarding
                                        }
                                    }
                                }
                            } else{
                                _appState.value = AppState.Login
                            }
                        }.onFailure { e ->
                            e.printStackTrace()
                            _appState.value = AppState.Login
                        }

                    }
                }
            }
        }
    }

    fun fetchGroupName(groupId: String){
        viewModelScope.launch {
            val groupRes = getGroupByIdUseCase(groupId)
            groupRes.onSuccess { group ->
                if (group != null) {
                    _groupName.value = group.name
                } else {
                    _groupName.value = "Группа не найдена"
                }
            }.onFailure { exception ->
                Log.e("GroupViewModel", "Ошибка при загрузке имени группы")
            }
        }
    }
    val themeMode = getThemeModeUseCase().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeMode.SYSTEM
    )

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            setThemeModeUseCase(mode)
        }
    }

    fun cancelRequestAndLogout(onSuccess: () -> Unit){
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            user.onSuccess { user ->
                if (user != null) {
                    val userId = user.id
                    removeStudentUseCase(userId)
                }
            }
            logout(onSuccess)
        }
    }

    fun logout(onSuccess: () -> Unit){
        viewModelScope.launch {
            val res = logoutUseCase()
            res.onFailure { e ->
                Log.e("DEBUG_KUBIK", "Ошибка выхода: ${e.message}")
            }
            try{
                clearUserUseCase()
                withContext(Dispatchers.Main){
                    onSuccess()
                }
            } catch (e: Exception){
                Log.e("DEBUG_KUBIK", "Ошибка очистки локальных данных: ${e.message}")
            }
        }
    }
}
