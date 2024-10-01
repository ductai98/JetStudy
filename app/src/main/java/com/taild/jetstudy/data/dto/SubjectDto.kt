package com.taild.jetstudy.data.dto

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taild.jetstudy.domain.model.Subject

@Entity
data class SubjectDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val goalHours: Float,
    @ColorInt val colors: List<Int>
) {
    companion object {
        fun fromSubject(subject: Subject) : SubjectDto = SubjectDto(
            id = subject.id,
            name = subject.name,
            goalHours = subject.goalHours,
            colors = subject.colors
        )
    }
}
