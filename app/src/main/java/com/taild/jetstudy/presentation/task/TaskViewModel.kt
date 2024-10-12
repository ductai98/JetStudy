package com.taild.jetstudy.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {
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
            is TaskEvent.OnDeleteTask -> {

            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state = _state.value
            if (state.subjectId == null || state.relatedToSubject.isNullOrBlank()) {
                return@launch
            }
            taskRepository.upsertTask(
                task = Task(
                    id = _state.value.taskId ?: 0,
                    subjectId = _state.value.subjectId!!,
                    relatedToSubject = _state.value.relatedToSubject!!,
                    title = _state.value.title,
                    description = _state.value.description,
                    dueDate = _state.value.dueDate ?: Instant.now().toEpochMilli(),
                    isCompleted = _state.value.isTaskCompleted,
                    priority = _state.value.priority.ordinal
                )
            )
        }
    }
}