package com.taild.jetstudy.presentation.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.utils.Constant.MIN_DURATION
import com.taild.jetstudy.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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
            SessionEvent.NotifyToUpdateSubject -> {
                viewModelScope.launch {
                    if (state.value.subjectId == null || state.value.relatedToSubject.isNullOrBlank()) {
                        _snackBarEvent.emit(
                            SnackBarEvent.ShowSnackBar(
                                message = "Please select a subject relate to the session"
                            )
                        )
                    }
                }
            }
            SessionEvent.DeleteSession -> deleteSession()
            is SessionEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }
            is SessionEvent.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.id
                    )
                }
            }
            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectIdAndRelatedSubject -> {
                _state.update {
                    it.copy(
                        subjectId = event.subjectId,
                        relatedToSubject = event.relatedToSubject
                    )
                }
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                _state.value.session?.let {
                    sessionRepository.deleteSession(it)
                    _snackBarEvent.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "Delete session successfully"
                        )
                    )
                }
            } catch (e: Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete session",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < MIN_DURATION) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Single session can not be less than $MIN_DURATION seconds"
                    )
                )
                return@launch
            }
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