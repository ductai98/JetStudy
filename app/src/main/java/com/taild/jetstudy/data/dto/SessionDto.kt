package com.taild.jetstudy.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taild.jetstudy.domain.model.Session

@Entity
data class SessionDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val subjectId: Int?,
    val relatedToSubject: String?,
    val date: Long,
    val duration: Long,
) {
    companion object {
        fun fromSession(session: Session) : SessionDto = SessionDto(
            id = session.id,
            subjectId = session.subjectId,
            relatedToSubject = session.relatedToSubject,
            date = session.date,
            duration = session.duration
        )
    }
}
