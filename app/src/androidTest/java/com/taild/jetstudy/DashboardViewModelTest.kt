package com.taild.jetstudy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.data.dto.SubjectDto
import com.taild.jetstudy.data.local.AppDatabase
import com.taild.jetstudy.data.local.SessionDao
import com.taild.jetstudy.data.local.SessionDao_Impl
import com.taild.jetstudy.data.local.SubjectDao
import com.taild.jetstudy.data.local.SubjectDao_Impl
import com.taild.jetstudy.data.local.TaskDao
import com.taild.jetstudy.data.local.TaskDao_Impl
import com.taild.jetstudy.data.repository.SessionRepositoryImpl
import com.taild.jetstudy.data.repository.SubjectRepositoryImpl
import com.taild.jetstudy.data.repository.TaskRepositoryImpl
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.domain.repository.TaskRepository
import com.taild.jetstudy.presentation.dashboard.DashboardViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardViewModelTest {
    private lateinit var subjectRepository : SubjectRepository
    private lateinit var sessionRepository : SessionRepository
    private lateinit var taskRepository : TaskRepository
    private lateinit var database: AppDatabase
    private lateinit var subjectDao: SubjectDao
    private lateinit var taskDao: TaskDao
    private lateinit var sessionDao: SessionDao
    private lateinit var viewModel: DashboardViewModel
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        subjectDao = SubjectDao_Impl(database)
        taskDao = TaskDao_Impl(database)
        sessionDao = SessionDao_Impl(database)
        subjectRepository = SubjectRepositoryImpl(subjectDao, taskDao, sessionDao)
        sessionRepository = SessionRepositoryImpl(sessionDao)
        taskRepository = TaskRepositoryImpl(taskDao)
        viewModel = DashboardViewModel(subjectRepository, sessionRepository, taskRepository)
    }

    @Test
    fun dashboardViewModel_Initialization_FirstOpen() {
        val uiState = viewModel.state.value
        assertTrue(uiState.totalSubjectCount == 0)
        assertTrue(uiState.subjects.isEmpty())
    }

    @Test
    fun dashboardViewModel_Database_InsertSubject() {
        runTest {
            val subject = Subject(
                name = "Math",
                colors = Subject.subjectCardColors.random(),
                goalHours = 10f
            )
            subjectDao.upsertSubject(SubjectDto.fromSubject(subject))
            val count = subjectDao.getSubjectCount().first()
            assertTrue(count == 1)
        }
    }

    @Test
    fun dashboardViewModel_Database_DeleteSession() {
        runTest {
            val session = SessionDto(
                id = 1,
                subjectId = 1,
                relatedToSubject = "",
                duration = 12000,
                date = 10000000
            )
            sessionDao.insertSession(session)
            val count = sessionDao.getAllSession().first().size
            assertTrue(count == 1)
            sessionDao.deleteSession(session)
            val count1 = sessionDao.getAllSession().first().size
            assertTrue(count1 == 0)
        }
    }

    @After
    fun finish() {
        database.close()
    }
}