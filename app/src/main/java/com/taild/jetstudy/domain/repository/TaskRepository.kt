package com.taild.jetstudy.domain.repository

import com.taild.jetstudy.data.dto.TaskDto
import com.taild.jetstudy.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun upsertTask(task: TaskDto)
    suspend fun deleteTask(id: Int)
    suspend fun getTaskById(id: Int) : Task?
    fun getTasksForSubject(subjectId: Int) : Flow<List<Task>>
    fun getUpcomingTasks() : Flow<List<Task>>
    fun getUpcomingTasksForSubject(subjectId: Int) : Flow<List<Task>>
    fun getCompletedTasksForSubject(subjectId: Int) : Flow<List<Task>>
}