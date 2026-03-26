package com.example.kubik.domain.repository

import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val themeModeFlow: Flow<ThemeMode>
    val userFlow: Flow<User?>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
}


