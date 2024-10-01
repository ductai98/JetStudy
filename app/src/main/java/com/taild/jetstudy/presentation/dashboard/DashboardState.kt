package com.taild.jetstudy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject

data class DashboardState(
    val totalSubjectCount : Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalStudyHours: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Int> = Subject.subjectCardColors.random(),
    val session: Session? = null
)