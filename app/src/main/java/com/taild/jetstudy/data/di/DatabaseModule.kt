package com.taild.jetstudy.data.di

import android.app.Application
import androidx.room.Room
import com.taild.jetstudy.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ) : AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "jetstudy.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(db: AppDatabase) = db.subjectDao()

    @Provides
    @Singleton
    fun provideSessionDao(db: AppDatabase) = db.sessionDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase) = db.taskDao()
}