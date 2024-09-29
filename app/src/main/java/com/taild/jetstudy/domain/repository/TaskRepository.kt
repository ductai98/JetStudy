package com.taild.jetstudy.domain.repository

import com.taild.jetstudy.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun upsertTask(task: Task)
    suspend fun deleteTask(id: Int)
    suspend fun getTaskById(id: Int) : Task
    fun getTasksForSubject(subjectId: Int) : Flow<List<Task>>
    fun getAllTasks() : Flow<List<Task>>
}