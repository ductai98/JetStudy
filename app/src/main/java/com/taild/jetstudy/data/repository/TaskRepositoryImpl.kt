package com.taild.jetstudy.data.repository

import com.taild.jetstudy.data.dto.TaskDto
import com.taild.jetstudy.data.local.TaskDao
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.domain.repository.TaskRepository
import com.taild.jetstudy.utils.toTask
import com.taild.jetstudy.utils.toTaskList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(TaskDto.fromTask(task))
    }

    override suspend fun deleteTask(id: Int) {
        taskDao.deleteTask(id)
    }

    override suspend fun getTaskById(id: Int): Task {
        return taskDao.getTaskById(id).toTask()
    }

    override fun getTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId).map { it.toTaskList() }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { it.toTaskList() }
    }
}