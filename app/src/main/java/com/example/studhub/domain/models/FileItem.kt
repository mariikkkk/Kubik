package com.example.studhub.domain.models


enum class FileType{
    PDF, DOCX, TXT, PPTX, JPEG, RAR, ZIP
}

enum class FileCategory(val title: String){
    ALL("Все"),
    LECTURES("Лекции"),
    PRACTICE("Практики"),
    LABS("Лабораторные"),
    BOOK("Книги"),
    OTHER("Другое")
}
data class FileItem(
    val id: Int,
    val name: String,
    val size: String,
    val type: FileType,
    val date: String,
    val author: String,
    val category: FileCategory
)

