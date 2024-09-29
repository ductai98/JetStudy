package com.taild.jetstudy.data.repository

import com.taild.jetstudy.data.local.SubjectDao
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao
) : SubjectRepository {
    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getSubjectCount(): Flow<Int> {
        return subjectDao.getSubjectCount()
    }

    override fun getTotalHoursCount(): Flow<Float> {
        return subjectDao.getTotalHoursCount()
    }

    override suspend fun getSubjectById(id: Int): Subject? {
        return subjectDao.getSubjectById(id)
    }

    override suspend fun deleteSubject(id: Int) {
        subjectDao.deleteSubject(id)
    }

    override fun getAllSubject(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
    }
}