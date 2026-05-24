package com.example.kubik.domain.questions.repository

import com.example.kubik.domain.questions.models.QuestionItem
import kotlinx.coroutines.flow.Flow

interface QuestionsRepository {
    fun getQuestions(groupId: String): Flow<List<QuestionItem>>
    suspend fun createQuestion(question: QuestionItem): Result<Unit>
    suspend fun answerQuestion(
        questionId: String,
        answer: String,
        answeredById: String,
        answeredByName: String
    ): Result<Unit>
    suspend fun deleteQuestion(questionId: String): Result<Unit>
    suspend fun markQuestionResolved(questionId: String): Result<Unit>
}