package com.taild.jetstudy.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SessionState())
    val state = combine(
        _state,
        sessionRepository.getAllSession(),
        subjectRepository.getAllSubject()
    ) { state, sessions, subjects ->
        state.copy(
            sessions = sessions,
            subjects = subjects
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionState()
    )

    fun onEvent(event: SessionEvent) {
        when(event) {
            SessionEvent.CheckSubjectId -> {}
            SessionEvent.DeleteSession -> {}
            is SessionEvent.OnDeleteSessionButtonClick -> {}
            is SessionEvent.OnRelatedSubjectChange -> {}
            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectIdAndRelatedSubject -> {}
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            sessionRepository.insertSession(
                session = SessionDto(
                    subjectId = _state.value.subjectId,
                    relatedToSubject = _state.value.relatedToSubject,
                    date = Instant.now().toEpochMilli(),
                    duration = duration
                )
            )
        }
    }
}