package com.taild.jetstudy.presentation.session

import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject

sealed class SessionEvent {
    data class OnRelatedSubjectChange(val subject: Subject) : SessionEvent()
    data class SaveSession(val duration: Long) : SessionEvent()
    data class OnDeleteSessionButtonClick(val session: Session) : SessionEvent()
    data object DeleteSession : SessionEvent()
    data object CheckSubjectId : SessionEvent()
    data class UpdateSubjectIdAndRelatedSubject(
        val subjectId: Int?,
        val relatedToSubject: String?
    ) : SessionEvent()
}