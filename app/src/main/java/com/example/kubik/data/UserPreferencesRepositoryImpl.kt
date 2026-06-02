package com.example.kubik.data

import android.content.Context
import androidx.compose.material3.contentColorFor
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "kubik_ref"
)

class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    ): UserPreferencesRepository {
    private companion object{
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        val USER_ID = stringPreferencesKey("user_id")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val GROUP = stringPreferencesKey("group")
        val ROLE = stringPreferencesKey("role")
        val SELECTED_SEMESTER = intPreferencesKey("selected_semester")

    }
    override val selectedSemesterFlow: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[SELECTED_SEMESTER] ?: 1
        }
    override val themeModeFlow: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        val themeString = prefs[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
        ThemeMode.valueOf(themeString)
    }
    override val userFlow: Flow<User?> = context.dataStore.data.map { prefs ->
        val userId = prefs[USER_ID] ?: return@map null
        User(
            id = userId,
            firstName = prefs[FIRST_NAME] ?: "",
            lastName = prefs[LAST_NAME] ?: "",
            groupId = prefs[GROUP] ?: "",
            role = prefs[ROLE] ?: ""
        )
    }
    override suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[THEME_MODE_KEY] = themeMode.name
        }
    }

    override suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = user.id
            prefs[FIRST_NAME] = user.firstName
            prefs[LAST_NAME] = user.lastName
            prefs[GROUP] = user.groupId ?: ""
            prefs[ROLE] = user.role
        }
    }

    override suspend fun deleteUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_ID)
            prefs.remove(FIRST_NAME)
            prefs.remove(LAST_NAME)
            prefs.remove(GROUP)
            prefs.remove(ROLE)
    }
        }

    override suspend fun saveSelectedSemester(semester: Int) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_SEMESTER] = semester
        }
    }
}