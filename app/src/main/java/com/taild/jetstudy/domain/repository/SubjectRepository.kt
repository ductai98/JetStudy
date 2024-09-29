package com.taild.jetstudy.domain.repository

import com.taild.jetstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    suspend fun upsertSubject(subject: Subject)
    fun getSubjectCount(): Flow<Int>
    fun getTotalHoursCount(): Flow<Float>
    suspend fun getSubjectById(id: Int) : Subject?
    suspend fun deleteSubject(id: Int)
    fun getAllSubject(): Flow<List<Subject>>
}