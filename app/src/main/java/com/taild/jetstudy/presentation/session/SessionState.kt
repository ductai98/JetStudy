package com.taild.jetstudy.presentation.session

import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)
