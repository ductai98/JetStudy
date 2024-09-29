package com.taild.jetstudy.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.taild.jetstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsert(subject: Subject)

    @Query("SELECT COUNT(*) FROM SUBJECTDTO")
    fun getSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECTDTO")
    fun getTotalHoursCount(): Flow<Float>

    @Query("SELECT * FROM SUBJECTDTO WHERE id = :id")
    suspend fun getSubjectById(id: Int) : Subject?

    @Query("DELETE FROM SUBJECTDTO WHERE id = :id")
    suspend fun deleteSubject(id: Int)

    @Query("SELECT * FROM SUBJECTDTO")
    fun getAll(): Flow<List<Subject>>
}