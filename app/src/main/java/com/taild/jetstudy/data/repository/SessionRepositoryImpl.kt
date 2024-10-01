package com.taild.jetstudy.data.repository

import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.data.local.SessionDao
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.utils.toSessionList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {
    override suspend fun insertSession(session: Session) {
        val sessionDto = SessionDto.fromSession(session)
        sessionDao.insertSession(sessionDto)
    }

    override suspend fun deleteSession(session: Session) {
        val sessionDto = SessionDto.fromSession(session)
        sessionDao.deleteSession(sessionDto)
    }

    override fun getAllSession(): Flow<List<Session>> {
        return sessionDao.getAllSession().map { it.toSessionList() }
    }

    override fun getRecentSessionForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDao.getRecentSessionForSubject(subjectId).map { it.toSessionList() }
    }

    override fun getTotalSessionDuration(): Flow<Long> {
        return sessionDao.getTotalSessionDuration()
    }

    override fun getTotalSessionDurationForSubject(subjectId: Int): Flow<Long> {
        return sessionDao.getTotalSessionDurationForSubject(subjectId)
    }

    override fun deleteSessionBySubjectId(subjectId: Int) {
        return sessionDao.deleteSessionBySubjectId(subjectId)
    }
}