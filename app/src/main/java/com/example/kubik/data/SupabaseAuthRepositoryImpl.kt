package com.example.kubik.data

import com.example.kubik.di.SupabaseModule
import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseAuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun loginWithVK(
        userId: Long,
        firstName: String,
        lastName: String
    ): Result<User> {
        return try{
            var session = SupabaseModule.supabase.auth.currentSessionOrNull()
            if (session == null){
                val emailGen = "vk_${userId}@kubik.app"
                val passGen = "secret_pass_${userId}"
                try {
                    SupabaseModule.supabase.auth.signInWith(Email) {
                        email = emailGen
                        password = passGen
                    }
                } catch (e : Exception) {
                    SupabaseModule.supabase.auth.signUpWith(Email) {
                        email = emailGen
                        password = passGen
                        data = buildJsonObject {
                            put("first_name", firstName)
                            put("last_name", lastName)
                            put("vk_id", userId)
                        }

                    }
                }
                session = SupabaseModule.supabase.auth.currentSessionOrNull()
            }
            if (session == null){
                return Result.failure(Exception("Session is null"))
            }
            val metadata = session.user?.userMetadata
            val firstName = metadata?.get("first_name").toString().replace("\"", "")
            val lastName = metadata?.get("last_name").toString().replace("\"", "")

            val user = User(
                id = session.user?.id ?: "",
                firstName = firstName,
                lastName = lastName,
                vkId = userId
            )
            Result.success(user)

        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val session = SupabaseModule.supabase.auth.currentSessionOrNull() ?: return null
        val metadata = session?.user?.userMetadata
        val firstName = metadata?.get("first_name").toString().replace("\"", "")
        val lastName = metadata?.get("last_name").toString().replace("\"", "")

        val user = User(
            id = session?.user?.id ?: "",
            firstName = firstName,
            lastName = lastName,
            group = "Пока не в группе",
            role = "Студент"
        )
        return user
    }

    override suspend fun checkHasCurrentSession(): Boolean {
        return SupabaseModule.supabase.auth.currentSessionOrNull() != null
    }

    override suspend fun logout() {
        try{
            SupabaseModule.supabase.auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeAuthState(): Flow<AuthState> {
        return SupabaseModule.supabase.auth.sessionStatus.map { status ->
            when(status){
                is SessionStatus.LoadingFromStorage -> AuthState.Loading
                is SessionStatus.Authenticated -> AuthState.Authenticated
                else -> AuthState.Unauthenticated

            }
        }

    }

    override suspend fun updateUserProfile(firstName: String, lastName: String) {
        try{
            SupabaseModule.supabase.auth.modifyUser {
                data = buildJsonObject {
                    put("first_name", firstName)
                    put("last_name", lastName)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}