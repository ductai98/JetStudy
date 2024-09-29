package com.taild.jetstudy.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val subjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
)
