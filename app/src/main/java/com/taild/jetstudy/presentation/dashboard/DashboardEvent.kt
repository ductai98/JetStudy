package com.taild.jetstudy.presentation.dashboard

import androidx.annotation.ColorRes
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Task

sealed class DashboardEvent {
    data object OnSaveSubject : DashboardEvent()
    data object OnDeleteSubject : DashboardEvent()
    data class OnDeleteSessionClick(val session: Session) : DashboardEvent()
    data class OnTaskCompletedChange(val task: Task) : DashboardEvent()
    data class OnSubjectCardColorChange(@ColorRes val colors : List<Int>) : DashboardEvent()
    data class OnSubjectNameChange(val name: String) : DashboardEvent()
    data class OnGoalStudyHoursChange(val hours: String) : DashboardEvent()
}