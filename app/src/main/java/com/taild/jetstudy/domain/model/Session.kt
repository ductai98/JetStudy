package com.taild.jetstudy.domain.model

data class Session(
    val id: Int,
    val subjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
)
