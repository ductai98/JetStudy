package com.taild.jetstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.taild.jetstudy.presentation.session.SessionScreen
import com.taild.jetstudy.presentation.subject.SubjectScreen
import com.taild.jetstudy.presentation.task.TaskScreen
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import kotlin.reflect.typeOf

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
                ) {
                    composable<DashboardRoute> {
                        DashboardScreen(
                            onSubjectClick = {
                                navController.navigate(SubjectRoute(it))
                            },
                            onStartSessionClick = {
                                navController.navigate(SessionRoute)
                            },
                            onTaskClick = {
                                navController.navigate(TaskRoute(it))
                            }
                        )
                    }

                    composable<SubjectRoute>(
                        typeMap = mapOf(
                            typeOf<Subject>() to JetStudyNavTypes.SubjectType
                        )
                    ) {
                        val argument = it.toRoute<SubjectRoute>()
                        SubjectScreen(
                            subject = argument.subject,
                            onBackClick = {
                                navController.navigateUp()
                            },
                            onTaskClick = {
                                navController.navigate(TaskRoute(it))
                            }
                        )
                    }

                    composable<TaskRoute>(
                        typeMap = mapOf(
                            typeOf<Task>() to JetStudyNavTypes.TaskType
                        )
                    ){
                        val argument = it.toRoute<TaskRoute>()
                        TaskScreen(
                            task = argument.task,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<SessionRoute> {
                        SessionScreen(
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }
        }
    }
}

val subjects = listOf(
    Subject(id = 0, name = "English", goalHours = 10f, colors = Subject.subjectCardColors[0]),
    Subject(id = 0, name = "Physics", goalHours = 10f, colors = Subject.subjectCardColors[1]),
    Subject(id = 0, name = "Maths", goalHours = 10f, colors = Subject.subjectCardColors[2]),
    Subject(id = 0, name = "Geology", goalHours = 10f, colors = Subject.subjectCardColors[3]),
    Subject(id = 0, name = "Fine Arts", goalHours = 10f, colors = Subject.subjectCardColors[0]),
    Subject(id = 0, name = "Jetpack Compose", goalHours = 10f, colors = Subject.subjectCardColors[0]),
    Subject(id = 0, name = "SwiftUI", goalHours = 10f, colors = Subject.subjectCardColors[0]),
)

val tasks = listOf(
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

val sessions = listOf(
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
            onSubjectClick = {},
            onStartSessionClick = {},
            onTaskClick = {}
        )
    }
}