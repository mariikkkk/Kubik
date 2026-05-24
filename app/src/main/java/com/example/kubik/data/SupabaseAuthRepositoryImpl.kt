package com.example.kubik.data

import android.util.Log
import com.example.kubik.BuildConfig
import com.example.kubik.di.SupabaseModule
import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.models.Group
import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.security.MessageDigest
import javax.inject.Inject

class SupabaseAuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AuthRepository {
    private fun generateSecurePassword(vkId: Long): String {
        val input = "${vkId}_${BuildConfig.VK_SALT}"
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override suspend fun loginWithVK(
        userId: Long,
        firstName: String,
        lastName: String
    ): Result<User> {
        return try{
            var session = SupabaseModule.supabase.auth.currentSessionOrNull()
            if (session == null){
                val emailGen = "vk_${userId}@kubik.app"
                val passGen = generateSecurePassword(userId)
                try {
                    SupabaseModule.supabase.auth.signInWith(Email) {
                        email = emailGen
                        password = passGen
                    }
                } catch (e: Exception) {
                    Log.e("KUBIK_AUTH", "Тип ошибки: ${e::class.simpleName}")
                    Log.e("KUBIK_AUTH", "Сообщение: ${e.message}")
                    Log.e("KUBIK_AUTH", "Причина: ${e.cause?.message}")
                    when {
                        // пользователь не найден — регистрируем
                        e.message?.contains("Invalid login credentials") == true -> {
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
                        // таймаут — пробуем войти ещё раз
                        e.message?.contains("timed out") == true -> {
                            SupabaseModule.supabase.auth.signInWith(Email) {
                                email = emailGen
                                password = passGen
                            }
                        }
                        // остальные ошибки — пробрасываем
                        else -> throw e
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

    override suspend fun getCurrentUser(): Result<User?> = runCatching {
        val session = SupabaseModule.supabase.auth.currentSessionOrNull() ?: return@runCatching null
        val metadata = session?.user?.userMetadata
        val firstName = metadata?.get("first_name").toString().replace("\"", "")
        val lastName = metadata?.get("last_name").toString().replace("\"", "")

        val user = User(
            id = session?.user?.id ?: "",
            firstName = firstName,
            lastName = lastName,
            groupId = null,
            role = "Студент"
        )
        user
    }

    override suspend fun checkHasCurrentSession(): Boolean {
        return SupabaseModule.supabase.auth.currentSessionOrNull() != null
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        SupabaseModule.supabase.auth.signOut()
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

    override suspend fun updateUserProfile(firstName: String, lastName: String): Result<Unit> = runCatching {
            val session = SupabaseModule.supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: ""
            SupabaseModule.supabase.auth.modifyUser {
                data = buildJsonObject {
                    put("first_name", firstName)
                    put("last_name", lastName)
                }
            }
            firestore.collection("users").document(userId).update(
                mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName
                )
            ).await()
    }

    override suspend fun getAllGroups(): Result<List<Group>> = runCatching {
            val snapshot = firestore.collection("groups").get().await()
            snapshot.toObjects(Group::class.java)
    }

    override suspend fun getUserProfileFromFirestore(userId: String): Result<User?> = runCatching {
            val document = firestore.collection("users").document(userId).get().await()
            if(document.exists()){
                document.toObject(User::class.java)
            } else {
                null
            }
    }

    override suspend fun registerStudent(
        userId: String,
        firstName: String,
        lastName: String,
        groupId: String
    ): Result<Unit> = runCatching{
        val userMap = hashMapOf(
            "id" to userId,
            "firstName" to firstName,
            "lastName" to lastName,
            "groupId" to groupId,
            "role" to "student",
            "status" to "pending"
        )
        firestore.collection("users").document(userId).set(userMap).await()
    }

    override suspend fun registerStarosta(
        userId: String,
        firstName: String,
        lastName: String,
        inviteCode: String
    ): Result<Unit> = runCatching{
        val groupQuery = firestore.collection("groups").whereEqualTo("inviteCode", inviteCode).get().await()
        if (groupQuery.isEmpty){
            throw Exception("Неверный код старосты")
        }
        val groupDoc = groupQuery.documents[0]
        val groupId = groupDoc.id
        firestore.runTransaction { transaction ->
            val userRef = firestore.collection("users").document(userId)
            val userMap = hashMapOf(
                "id" to userId,
                "firstName" to firstName,
                "lastName" to lastName,
                "groupId" to groupId,
                "role" to "starosta",
                "status" to "approved"
            )
            transaction.set(userRef, userMap)
            transaction.update(groupDoc.reference, "starostaId", userId)

        }.await()
    }

    override suspend fun getGroupById(groupId: String): Result<Group?> = runCatching {
        val document = firestore.collection("groups").document(groupId).get().await()
        document.toObject(Group::class.java)
    }

    override suspend fun getUsersGroup(groupId: String): Flow<List<User>> = callbackFlow {
        val collection = firestore.collection("users")
        val listener =
            collection.whereEqualTo("groupId", groupId).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
                    trySend(users)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun approveStudent(userId: String): Result<Unit> = runCatching{
        firestore.collection("users").document(userId).update("status", "approved").await()
    }

    override suspend fun removeStudent(userId: String): Result<Unit> = runCatching{
        firestore.collection("users")
            .document(userId)
            .update(
                "groupId", null
            )
            .await()
    }

    override fun observeUserProfile(userId: String): Flow<User?> = callbackFlow{
        val collection = firestore.collection("users").document(userId)
        val listener =
            collection.addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (value != null && value.exists()) {
                    val user = value.toObject(User::class.java)
                    trySend(user)
                } else{
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }
}