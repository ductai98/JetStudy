package com.taild.jetstudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.data.dto.SubjectDto
import com.taild.jetstudy.data.dto.TaskDto

@Database(
    entities = [SubjectDto::class, TaskDto::class, SessionDto::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao() : SubjectDao

    abstract fun taskDao() : TaskDao

    abstract fun sessionDao() : SessionDao

}