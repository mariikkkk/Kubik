package com.example.kubik.data

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
            groupId = null,
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
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getAllGroups(): List<Group> {
        return try{
            val snapshot = firestore.collection("groups").get().await()
            snapshot.toObjects(Group::class.java)
        } catch (e: Exception){
            emptyList<Group>()
        }

    }

    override suspend fun getUserProfileFromFirestore(userId: String): User? {
        return try{
            val document = firestore.collection("users").document(userId).get().await()
            if(document.exists()){
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception){
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

    override suspend fun getGroupById(groupId: String): Group? {
        return try{
            val document = firestore.collection("groups").document(groupId).get().await()
            document.toObject(Group::class.java)
        } catch (e: Exception){
            null
        }
    }

}