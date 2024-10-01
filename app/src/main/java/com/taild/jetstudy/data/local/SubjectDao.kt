package com.taild.jetstudy.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.taild.jetstudy.data.dto.SubjectDto
import com.taild.jetstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: SubjectDto)

    @Query("SELECT COUNT(*) FROM SUBJECTDTO")
    fun getSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECTDTO")
    fun getTotalHoursCount(): Flow<Float>

    @Query("SELECT * FROM SUBJECTDTO WHERE id = :id")
    suspend fun getSubjectById(id: Int) : SubjectDto?

    @Query("DELETE FROM SUBJECTDTO WHERE id = :id")
    suspend fun deleteSubject(id: Int)

    @Query("SELECT * FROM SUBJECTDTO")
    fun getAllSubjects(): Flow<List<SubjectDto>>
}