package com.taild.jetstudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.data.dto.SubjectDto
import com.taild.jetstudy.data.dto.TaskDto

@Database(
    entities = [SubjectDto::class, TaskDto::class, SessionDto::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao() : SubjectDao

    abstract fun taskDao() : TaskDao

    abstract fun sessionDao() : SessionDao

}