package com.taild.jetstudy.presentation.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.taild.jetstudy.data.dto.TaskDto
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.domain.repository.TaskRepository
import com.taild.jetstudy.presentation.components.SubjectRoute
import com.taild.jetstudy.utils.SnackBarEvent
import com.taild.jetstudy.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subjectId: Int = savedStateHandle.toRoute<SubjectRoute>().subjectId ?: -1

    private val _state = MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(subjectId),
        taskRepository.getCompletedTasksForSubject(subjectId),
        sessionRepository.getRecentTenSessionsForSubject(subjectId),
        sessionRepository.getTotalSessionDurationForSubject(subjectId)
    ) { state, upcomingTasks, completedTasks, recentSessions, totalSessionDuration ->
        val progress by derivedStateOf {
            val studiedHours = state.studiedHours
            val goalHours = state.goalStudyHours.toFloatOrNull() ?: 100f
            (studiedHours / goalHours).coerceIn(0f, 1f)
        }
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTasks,
            recentSessions = recentSessions,
            studiedHours = totalSessionDuration.toHours(),
            progress = progress
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SubjectState()
    )

    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }
            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = event.colors)
                }
            }
            is SubjectEvent.OnUpdateSubject -> updateSubject()
            is SubjectEvent.OnDeleteSubject -> deleteSubject()
            is SubjectEvent.OnTaskCompleteChange -> updateTask(task = event.task)
            is SubjectEvent.OnDeleteSession -> TODO()
            is SubjectEvent.OnDeleteSessionClick -> TODO()
        }
    }
    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = TaskDto.fromTask(task.copy(
                        isCompleted = task.isCompleted.not()
                    ))
                )
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Action completed"
                    )
                )
            } catch (e : Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't completed action. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            try {
                _state.value.subjectId?.let {
                    subjectRepository.deleteSubject(it)
                }
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Delete subject successfully"
                    )
                )
            } catch (e: IOException) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete subject (IOException): ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            } catch (e: Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete subject (UnknownError): ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    Subject(
                        id = state.value.subjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloat(),
                        colors = state.value.subjectCardColors
                    )
                )
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Subject updated successfully"
                    )
                )
            } catch (e: IOException) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save subject (IOException): ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            } catch (e: Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save subject (UnknownError): ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository.getSubjectById(subjectId)?.let { subject ->
                _state.update {
                    it.copy(
                        subjectId = subject.id,
                        subjectName = subject.name,
                        goalStudyHours = subject.goalHours.toString(),
                        subjectCardColors = subject.colors,
                    )
                }
            }
        }
    }
}