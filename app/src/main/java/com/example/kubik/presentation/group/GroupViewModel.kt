package com.example.kubik.presentation.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.usecase.ApproveStudentUseCase
import com.example.kubik.domain.usecase.GetCurrentUserUseCase
import com.example.kubik.domain.usecase.GetGroupByIdUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import com.example.kubik.domain.usecase.GetUsersGroupUseCase
import com.example.kubik.domain.usecase.RemoveStudentUseCase
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
class GroupViewModel @Inject constructor(
    private val getUsersGroupUseCase: GetUsersGroupUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val approveStudentUseCase: ApproveStudentUseCase,
    private val removeStudentUseCase: RemoveStudentUseCase,
    private val getGroupByIdUseCase: GetGroupByIdUseCase
): ViewModel() {
    val currentUser: StateFlow<User?> = getUserUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _groupName = MutableStateFlow("Загрузка...")
    val groupName = _groupName.asStateFlow()

    private val _allGroupMembers = MutableStateFlow<List<User>>(emptyList())

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val approvedUsers = combine(_allGroupMembers, _searchQuery) { allMembers, query ->
        allMembers.filter {
            it.status == "approved" &&
                    (it.firstName.contains(query, ignoreCase = true)
                            || it.lastName.contains(query, ignoreCase = true))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val pendingUsers = _allGroupMembers.map { members ->
        members.filter { it.status == "pending" }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    init{
        loadData()
    }

    private fun loadData(){
        viewModelScope.launch {
            currentUser.collect { user ->
                if(user != null && user.groupId != null){
                    fetchGroupName(user.groupId)
                    getUsersGroupUseCase(user.groupId).collect { members ->
                        _allGroupMembers.value = members
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

    fun approveStudent(userId: String){
        viewModelScope.launch {
            val result = approveStudentUseCase(userId)
            result.onFailure { exception ->
                Log.e("GroupViewModel", "Ошибка при принятии студента", exception)
            }
        }
    }

    fun rejectStudent(userId: String){
        viewModelScope.launch {
            val result = removeStudentUseCase(userId)
            result.onFailure { exception ->
                Log.e("GroupViewModel", "Ошибка при отклонении студента", exception)
            }
        }
    }

    fun kickStudent(userId: String){
        viewModelScope.launch {
            val result = removeStudentUseCase(userId)
            result.onFailure { exception ->
                Log.e("GroupViewModel", "Ошибка при исключении студента", exception)
            }
        }
    }


    fun updateQuery(query: String){
        _searchQuery.value = query
    }

    fun updateTabIndex(index: Int){
        _selectedTabIndex.value = index
    }
}
