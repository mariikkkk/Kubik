package com.example.kubik.domain.questions.usecase

import com.example.kubik.domain.questions.models.QuestionItem
import com.example.kubik.domain.questions.repository.QuestionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    operator fun invoke(groupId: String): Flow<List<QuestionItem>> {
        return questionsRepository.getQuestions(groupId)
    }
}