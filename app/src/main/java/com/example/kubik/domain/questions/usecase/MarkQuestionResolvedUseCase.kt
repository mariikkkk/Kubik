package com.example.kubik.domain.questions.usecase

import com.example.kubik.domain.questions.repository.QuestionsRepository
import javax.inject.Inject

class MarkQuestionResolvedUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke(questionId: String): Result<Unit>{
        return questionsRepository.markQuestionResolved(questionId)
    }
}