package com.taild.jetstudy.domain.repository

import com.taild.jetstudy.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun insertSession(session: Session)
    suspend fun deleteSession(session: Session)
    suspend fun deleteSessionBySubjectId(subjectId: Int)
    fun getAllSession(): Flow<List<Session>>
    fun getRecentSessionForSubject(subjectId: Int) : Flow<List<Session>>
    fun getTotalSessionDuration() : Flow<Long>
    fun getTotalSessionDurationForSubject(subjectId: Int) : Flow<Long>
    fun getRecentFiveSessions() : Flow<List<Session>>
    fun getRecentTenSessionsForSubject(subjectId: Int) : Flow<List<Session>>
}