package com.example.kubik.data

import com.example.kubik.domain.questions.models.QuestionItem
import com.example.kubik.domain.questions.models.QuestionStatus
import com.example.kubik.domain.questions.repository.QuestionsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : QuestionsRepository {
    private val questionsCollection get() = firestore.collection("questions")
    override fun getQuestions(groupId: String): Flow<List<QuestionItem>> = callbackFlow{
        val listener = questionsCollection
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val questions = snapshot.documents.mapNotNull {
                        it.toObject(QuestionItem::class.java)
                            ?.copy(id = it.id)
                    }.sortedByDescending { it.createdAt }
                    trySend(questions)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun createQuestion(question: QuestionItem): Result<Unit> = runCatching{
        val questionRef = questionsCollection.document()
        questionRef.set(question.copy(id = questionRef.id)).await()
    }

    override suspend fun answerQuestion(
        questionId: String,
        answer: String,
        answeredById: String,
        answeredByName: String
    ): Result<Unit> = runCatching {
        questionsCollection
            .document(questionId)
            .update(
                mapOf(
                    "answer" to answer,
                    "answeredById" to answeredById,
                    "answeredByName" to answeredByName,
                    "answeredAt" to System.currentTimeMillis(),
                    "status" to QuestionStatus.ANSWERED.value
                )
            )
            .await()
    }

    override suspend fun deleteQuestion(questionId: String): Result<Unit> = runCatching {
        questionsCollection
            .document(questionId)
            .delete()
            .await()
    }

    override suspend fun markQuestionResolved(questionId: String): Result<Unit> = runCatching{
        questionsCollection
            .document(questionId)
            .update(
                mapOf(
                    "resolvedAt" to System.currentTimeMillis(),
                    "status" to QuestionStatus.RESOLVED.value
                )
            )
            .await()
    }
}
