package com.taild.jetstudy

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.presentation.components.DashboardRoute
import com.taild.jetstudy.presentation.components.SessionRoute
import com.taild.jetstudy.presentation.components.SubjectRoute
import com.taild.jetstudy.presentation.components.TaskRoute
import com.taild.jetstudy.presentation.dashboard.DashboardScreen
import com.taild.jetstudy.presentation.dashboard.DashboardState
import com.taild.jetstudy.presentation.dashboard.DashboardViewModel
import com.taild.jetstudy.presentation.session.SessionScreen
import com.taild.jetstudy.presentation.session.SessionState
import com.taild.jetstudy.presentation.session.SessionViewModel
import com.taild.jetstudy.presentation.session.StudySessionTimerService
import com.taild.jetstudy.presentation.subject.SubjectScreen
import com.taild.jetstudy.presentation.subject.SubjectViewModel
import com.taild.jetstudy.presentation.task.TaskScreen
import com.taild.jetstudy.presentation.task.TaskViewModel
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import com.taild.jetstudy.utils.SnackBarEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudySessionTimerService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: ")
            val binder = service as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: ")
            isBound = false
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            Log.d(TAG, "onBindingDied: ")
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
            Log.d(TAG, "onNullBinding: ")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (isBound) {
                JetStudyTheme {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = DashboardRoute,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
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
                            val viewModel: SessionViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val onEvent = viewModel::onEvent
                            val snackBarEvent = viewModel.snackBarEvent
                            SessionScreen(
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                timerService = timerService,
                                state = state,
                                onEvent = onEvent,
                                snackBarEvent = snackBarEvent
                            )
                        }
                    }
                }
            }

        }
        requestPermission()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetStudyTheme {
        DashboardScreen(
            snackBarEvent = MutableStateFlow<SnackBarEvent>(
                SnackBarEvent.ShowSnackBar("")
            ),
            onEvent = {},
            tasks = emptyList(),
            sessions = emptyList(),
            uiState = DashboardState(),
            onSubjectClick = {},
            onStartSessionClick = {},
            onTaskClick = {}
        )
    }
}