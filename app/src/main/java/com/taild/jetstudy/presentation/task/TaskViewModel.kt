package com.taild.jetstudy.presentation.task

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.taild.jetstudy.data.dto.TaskDto
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.domain.repository.TaskRepository
import com.taild.jetstudy.presentation.components.SubjectRoute
import com.taild.jetstudy.presentation.components.TaskRoute
import com.taild.jetstudy.utils.Priority
import com.taild.jetstudy.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Int? = savedStateHandle.toRoute<TaskRoute>().taskId
    private val subjectId: Int? = savedStateHandle.toRoute<TaskRoute>().subjectId

    private val _state = MutableStateFlow(TaskState())
    val state = combine(
        _state,
        subjectRepository.getAllSubject()
    ) { state, subjects ->
        state.copy(
            subjects = subjects
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskState()
    )

    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        fetchTask()
        fetchSubject()
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            is TaskEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is TaskEvent.OnDueDateChange -> {
                _state.update {
                    it.copy(
                        dueDate = event.dueDate
                    )
                }
            }
            is TaskEvent.OnIsCompletedChange -> {
                _state.update {
                    it.copy(
                        isTaskCompleted = _state.value.isTaskCompleted.not()
                    )
                }
            }
            is TaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(
                        priority = event.priority
                    )
                }
            }
            is TaskEvent.OnRelatedToSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.id
                    )
                }
            }
            is TaskEvent.OnSaveTask -> saveTask()
            is TaskEvent.OnDeleteTask -> deleteTask()
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            _state.value.taskId?.let {
                withContext(Dispatchers.IO) {
                    taskRepository.deleteTask(it)
                }
                _snackBarEvent.emit(
                    SnackBarEvent.NavigateUp
                )
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state = _state.value
            if (state.subjectId == null || state.relatedToSubject.isNullOrBlank()) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Please select subject related to the task",
                        duration = SnackbarDuration.Long
                    )
                )
                return@launch
            }
            try {
                taskRepository.upsertTask(
                    task = TaskDto(
                        id = state.taskId,
                        subjectId = _state.value.subjectId!!,
                        relatedToSubject = _state.value.relatedToSubject!!,
                        title = _state.value.title,
                        description = _state.value.description,
                        dueDate = _state.value.dueDate ?: Instant.now().toEpochMilli(),
                        isCompleted = _state.value.isTaskCompleted,
                        priority = _state.value.priority.ordinal
                    )
                )
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Task saved successfully"
                    )
                )
                _snackBarEvent.emit(
                    SnackBarEvent.NavigateUp
                )
            } catch (e: Exception) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun fetchTask() {
        viewModelScope.launch {
            taskId?.let {
                taskRepository.getTaskById(it)?.let { task ->
                    _state.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            dueDate = task.dueDate,
                            isTaskCompleted = task.isCompleted,
                            priority = Priority.fromInt(task.priority),
                            relatedToSubject = task.relatedToSubject,
                            taskId = task.id,
                            subjectId = task.subjectId
                        )
                    }
                }
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectId?.let { id ->
                subjectRepository.getSubjectById(id)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectId = subject.id,
                            relatedToSubject = subject.name
                        )
                    }
                }
            }
        }
    }
}