package com.taild.jetstudy.presentation.task

import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.utils.Priority

sealed class TaskEvent {
    data class OnTitleChange(val title: String) : TaskEvent()
    data class OnDescriptionChange(val description: String) : TaskEvent()
    data class OnDueDateChange(val dueDate: Long?) : TaskEvent()
    data class OnPriorityChange(val priority: Priority) : TaskEvent()
    data class OnRelatedToSubjectChange(val subject: Subject) : TaskEvent()
    data object OnIsCompletedChange : TaskEvent()
    data object OnSaveTask : TaskEvent()
    data object OnDeleteTask : TaskEvent()
}
