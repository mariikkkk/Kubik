package com.example.kubik.domain.questions.usecase

import com.example.kubik.domain.questions.repository.QuestionsRepository
import javax.inject.Inject

class AnswerQuestionUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke(
        questionId: String,
        answer: String,
        answeredById: String,
        answeredByName: String
    ): Result<Unit> {
        val trimmedAnswer = answer.trim()
        if(trimmedAnswer.isBlank()){
            return Result.failure(Exception("Ответ не может быть пустым"))
        }
        return questionsRepository.answerQuestion(
            questionId = questionId,
            answer = trimmedAnswer,
            answeredById = answeredById,
            answeredByName = answeredByName.trim()
        )
    }
}