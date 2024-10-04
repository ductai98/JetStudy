package com.taild.jetstudy.presentation.subject

import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Task

sealed class SubjectEvent {
    data object OnUpdateSubject : SubjectEvent()
    data object OnDeleteSubject : SubjectEvent()
    data object OnDeleteSession: SubjectEvent()
    data class OnTaskCompleteChange(val task : Task) : SubjectEvent()
    data class OnSubjectCardColorChange(val colors: List<Int>) : SubjectEvent()
    data class OnSubjectNameChange(val name: String) : SubjectEvent()
    data class OnGoalStudyHoursChange(val hours: String) : SubjectEvent()
    data class OnDeleteSessionClick(val session: Session) : SubjectEvent()
}