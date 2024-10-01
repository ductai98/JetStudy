package com.taild.jetstudy.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taild.jetstudy.domain.model.Task

@Entity
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val subjectId: Int,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToSubject: String,
    val isCompleted: Boolean
) {
    companion object {
        fun fromTask(task: Task) : TaskDto = TaskDto(
            id = task.id,
            subjectId = task.subjectId,
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            priority = task.priority,
            relatedToSubject = task.relatedToSubject,
            isCompleted = task.isCompleted
        )
    }
}
