package com.taild.jetstudy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.presentation.components.DashboardRoute
import com.taild.jetstudy.presentation.components.JetStudyNavTypes
import com.taild.jetstudy.presentation.components.SessionRoute
import com.taild.jetstudy.presentation.components.SubjectRoute
import com.taild.jetstudy.presentation.components.TaskRoute
import com.taild.jetstudy.presentation.dashboard.DashboardScreen
import com.taild.jetstudy.presentation.dashboard.DashboardState
import com.taild.jetstudy.presentation.dashboard.DashboardViewModel
import com.taild.jetstudy.presentation.session.SessionScreen
import com.taild.jetstudy.presentation.subject.SubjectScreen
import com.taild.jetstudy.presentation.subject.SubjectViewModel
import com.taild.jetstudy.presentation.task.TaskScreen
import com.taild.jetstudy.presentation.task.TaskViewModel
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import com.taild.jetstudy.utils.SnackBarEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetStudyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = DashboardRoute,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    composable<DashboardRoute> {
                        val viewModel: DashboardViewModel = hiltViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        val tasks by viewModel.tasks.collectAsStateWithLifecycle()
                        val sessions by viewModel.sessions.collectAsStateWithLifecycle()
                        val snackBarEvent = viewModel.snackBarEvent
                        DashboardScreen(
                            onEvent = viewModel::onEvent,
                            uiState = state,
                            tasks = tasks,
                            sessions = sessions,
                            snackBarEvent = snackBarEvent,
                            onSubjectClick = {
                                navController.navigate(SubjectRoute(it.id))
                            },
                            onStartSessionClick = {
                                navController.navigate(SessionRoute)
                            },
                            onTaskClick = {
                                navController.navigate(TaskRoute(it.id, null))
                            }
                        )
                    }

                    composable<SubjectRoute> {
                        val viewModel: SubjectViewModel = hiltViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        val snackBarEvent = viewModel.snackBarEvent
                        SubjectScreen(
                            uiState = state,
                            onEvent = viewModel::onEvent,
                            snackBarEvent = snackBarEvent,
                            onBackClick = {
                                navController.navigateUp()
                            },
                            onTaskClick = {
                                navController.navigate(TaskRoute(it.id, null))
                            },
                            onAddTaskClick = { subjectId ->
                                navController.navigate(TaskRoute(null, subjectId))
                            }
                        )
                    }

                    composable<TaskRoute> {
                        val viewModel: TaskViewModel = hiltViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        val onEvent = viewModel::onEvent
                        val snackBarEvent = viewModel.snackBarEvent
                        TaskScreen(
                            uiState = state,
                            onEvent = onEvent,
                            snackBarEvent = snackBarEvent,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<SessionRoute>(
                        deepLinks = listOf(
                            navDeepLink<SessionRoute>(
                                basePath = "jet_study://dashboard/session"
                            )
                        )
                    ) {
                        SessionScreen(
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }
        }
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }
}



val fakeSubjects = listOf(
    Subject(id = 0, name = "English", goalHours = 10f, colors = Subject.subjectCardColors[0]),
    Subject(id = 0, name = "Physics", goalHours = 10f, colors = Subject.subjectCardColors[1]),
    Subject(id = 0, name = "Maths", goalHours = 10f, colors = Subject.subjectCardColors[2]),
    Subject(id = 0, name = "Geology", goalHours = 10f, colors = Subject.subjectCardColors[3]),
    Subject(id = 0, name = "Fine Arts", goalHours = 10f, colors = Subject.subjectCardColors[0]),
    Subject(id = 0, name = "Jetpack Compose", goalHours = 10f, colors = Subject.subjectCardColors[0]),
    Subject(id = 0, name = "SwiftUI", goalHours = 10f, colors = Subject.subjectCardColors[0]),
)

val fakeTasks = listOf(
    Task(
        id = 1,
        subjectId = 0,
        title = "Prepare notes",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isCompleted = false
    ),
    Task(
        id = 1,
        subjectId = 0,
        title = "Do homework",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isCompleted = true
    ),
    Task(
        id = 1,
        subjectId = 0,
        title = "Go Coaching",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isCompleted = false
    ),
    Task(
        id = 1,
        subjectId = 0,
        title = "Assignment",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isCompleted = false
    ),
    Task(
        id = 1,
        subjectId = 0,
        title = "Write poem",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isCompleted = true
    )
)

val fakeSessions = listOf(
    Session(
        id = 0,
        subjectId = 0,
        relatedToSubject = "English",
        date = 0L,
        duration = 2
    ),
    Session(
        id = 0,
        subjectId = 0,
        relatedToSubject = "English",
        date = 0L,
        duration = 2
    ),
    Session(
        id = 0,
        subjectId = 0,
        relatedToSubject = "Physics",
        date = 0L,
        duration = 2
    ),
    Session(
        id = 0,
        subjectId = 0,
        relatedToSubject = "Maths",
        date = 0L,
        duration = 2
    )
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetStudyTheme {
        DashboardScreen(
            snackBarEvent = MutableStateFlow<SnackBarEvent>(
                SnackBarEvent.ShowSnackBar("")
            ),
            onEvent = {},
            tasks = fakeTasks,
            sessions = fakeSessions,
            uiState = DashboardState(),
            onSubjectClick = {},
            onStartSessionClick = {},
            onTaskClick = {}
        )
    }
}