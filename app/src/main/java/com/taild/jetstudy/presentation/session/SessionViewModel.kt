package com.taild.jetstudy.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    fun onEvent(event: SessionEvent) {
        when(event) {
            SessionEvent.CheckSubjectId -> {}
            SessionEvent.DeleteSession -> {}
            is SessionEvent.OnDeleteSessionButtonClick -> {}
            is SessionEvent.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.id
                    )
                }
            }
            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectIdAndRelatedSubject -> {}
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            try {
                sessionRepository.insertSession(
                    session = SessionDto(
                        subjectId = _state.value.subjectId ?: -1,
                        relatedToSubject = _state.value.relatedToSubject ?: "",
                        date = Instant.now().toEpochMilli(),
                        duration = duration
                    )
                )
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Save session successfully"
                    )
                )
            } catch (e: Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save session"
                    )
                )
            }

        }
    }
}