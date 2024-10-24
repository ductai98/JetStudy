package com.taild.jetstudy

import com.taild.jetstudy.domain.repository.SessionRepository
import com.taild.jetstudy.domain.repository.SubjectRepository
import com.taild.jetstudy.domain.repository.TaskRepository
import com.taild.jetstudy.presentation.dashboard.DashboardViewModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class ViewModelUnitTest {
    private val subjectRepository : SubjectRepository = mock()
    private val sessionRepository : SessionRepository = mock()
    private val taskRepository : TaskRepository = mock()

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        viewModel = DashboardViewModel(subjectRepository, sessionRepository, taskRepository)
    }

    @Test
    fun dashboardViewModel_Initialization_FirstOpen() {
        val uiState = viewModel.state.value
        assertTrue(uiState.totalSubjectCount == 0)
        assertTrue(uiState.subjects.isEmpty())
    }
}