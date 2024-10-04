package com.taild.jetstudy.presentation.task

import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.utils.Priority

data class TaskState(
    val taskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val isTaskCompleted: Boolean = false,
    val priority: Priority = Priority.LOW,
    val relatedToSubject: String? = null,
    val subjects: List<Subject> = emptyList(),
    val subjectId: Int? = null,
)