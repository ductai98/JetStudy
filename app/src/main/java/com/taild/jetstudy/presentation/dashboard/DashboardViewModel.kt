package com.taild.jetstudy.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalHoursCount(),
        subjectRepository.getAllSubject(),
        sessionRepository.getTotalSessionDuration()
    ) { state, subjectCount, goalHours, subjects, totalSessionDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionDuration.toHours())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    fun onEvent(event: DashboardEvent ) {
        when(event) {
            is DashboardEvent.OnSubjectNameChange -> {
                _state.value = _state.value.copy(
                    subjectName = event.name
                )
            }
            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.value = _state.value.copy(
                    goalStudyHours = event.hours
                )
            }
            is DashboardEvent.OnDeleteSessionClick -> {
                _state.value = _state.value.copy(
                    session = event.session
                )
            }
            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.value = _state.value.copy(
                    subjectCardColors = event.colors
                )
            }
            is DashboardEvent.OnSaveSubject -> saveSubject()
            is DashboardEvent.OnDeleteSubject -> {}
            is DashboardEvent.OnTaskCompletedChange -> {}
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
            subjectRepository.upsertSubject(
                subject = Subject(
                    name = _state.value.subjectName,
                    colors = _state.value.subjectCardColors,
                    goalHours = _state.value.goalStudyHours.toFloatOrNull() ?: 1f
                )
            )
        }
    }
}