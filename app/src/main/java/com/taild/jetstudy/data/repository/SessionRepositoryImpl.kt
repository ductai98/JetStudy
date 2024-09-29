package com.taild.jetstudy.data.repository

import com.taild.jetstudy.data.local.SessionDao
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {
    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    override fun getAllSession(): Flow<List<Session>> {
        return sessionDao.getAllSession()
    }

    override fun getRecentSessionForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDao.getRecentSessionForSubject(subjectId)
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