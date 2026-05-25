package com.example.kubik.domain.questions.usecase

import com.example.kubik.domain.questions.models.QuestionItem
import com.example.kubik.domain.questions.models.QuestionStatus
import com.example.kubik.domain.questions.repository.QuestionsRepository
import javax.inject.Inject

class CreateQuestionUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke(
        groupId: String,
        authorId: String,
        authorName: String,
        questionText: String
    ): Result<Unit>{
        if(questionText.isBlank()){
            return Result.failure(Exception("Вопрос не может быть пустым"))
        }
        val question = QuestionItem(
            id = "",
            groupId = groupId,
            authorId = authorId,
            authorName = authorName,
            text = questionText.trim(),
            createdAt = System.currentTimeMillis(),
            status = QuestionStatus.WAITING.value
        )
        return questionsRepository.createQuestion(question)
    }
}