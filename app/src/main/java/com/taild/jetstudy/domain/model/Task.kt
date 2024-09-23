package com.taild.jetstudy.domain.model

data class Task(
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToSubject: String,
    val isCompleted: Boolean
)