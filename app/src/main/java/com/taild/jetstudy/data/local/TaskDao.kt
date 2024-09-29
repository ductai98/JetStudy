package com.taild.jetstudy.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.taild.jetstudy.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM TASKDTO WHERE id = :id")
    suspend fun deleteTask(id: Int)

    @Query("SELECT * FROM TASKDTO WHERE id = :id")
    suspend fun getTaskById(id: Int) : Task

    @Query("SELECT * FROM TASKDTO WHERE subjectId = :subjectId")
    fun getTasksForSubject(subjectId: Int) : Flow<List<Task>>

    @Query("SELECT * FROM TASKDTO")
    fun getAllTasks() : Flow<List<Task>>
}