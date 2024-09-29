package com.taild.jetstudy.data.dto

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val goalHours: Float,
    @ColorInt val colors: List<Int>
)
