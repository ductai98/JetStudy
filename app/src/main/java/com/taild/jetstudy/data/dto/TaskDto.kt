package com.taild.jetstudy.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val subjectId: Int,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToSubject: String,
    val isCompleted: Boolean
)
