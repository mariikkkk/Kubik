package com.example.kubik.domain.questions.models

data class QuestionItem(
    val id: String = "",
    val groupId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val text: String = "",
    val answer: String? = null,
    val answeredById: String? = null,
    val answeredByName: String? = null,
    val createdAt: Long = 0L,
    val answeredAt: Long? = null,
    val resolvedAt: Long? = null,
    val status: String = QuestionStatus.WAITING.value
){
    val typedStatus: QuestionStatus
        get() = QuestionStatus.fromValue(status)
}
