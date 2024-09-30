package com.taild.jetstudy.data.di

import com.taild.jetstudy.data.repository.SessionRepositoryImpl
import com.taild.jetstudy.data.repository.SubjectRepositoryImpl
import com.taild.jetstudy.data.repository.TaskRepositoryImpl
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSubjectRepository(subjectRepository: SubjectRepositoryImpl): SubjectRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(subjectRepository: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(subjectRepository: SessionRepositoryImpl): SessionRepository
}