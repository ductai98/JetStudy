package com.taild.jetstudy.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: SessionDto)

    @Delete
    suspend fun deleteSession(session: SessionDto)

    @Query("SELECT * FROM SessionDto")
    fun getAllSession(): Flow<List<SessionDto>>

    @Query("SELECT * FROM SessionDto WHERE subjectId = :subjectId")
    fun getRecentSessionForSubject(subjectId: Int) : Flow<List<SessionDto>>

    @Query("SELECT SUM(duration) FROM SessionDto")
    fun getTotalSessionDuration() : Flow<Long>

    @Query("SELECT SUM(duration) FROM SessionDto WHERE subjectId = :subjectId")
    fun getTotalSessionDurationForSubject(subjectId: Int) : Flow<Long>

    @Query("DELETE FROM SessionDto WHERE subjectId = :subjectId")
    fun deleteSessionBySubjectId(subjectId: Int)
}