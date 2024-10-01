package com.taild.jetstudy.domain.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: Int,
    val subjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
)
