package com.taild.jetstudy.presentation.subject

import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task

data class SubjectState(
    val subjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val studiedHours: Float = 0f,
    val progress: Float = 0f,
    val subjectCardColors: List<Int> = Subject.subjectCardColors.random(),
    val recentSessions: List<Session> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val session: Session? = null,
    val isLoading: Boolean = false
)
