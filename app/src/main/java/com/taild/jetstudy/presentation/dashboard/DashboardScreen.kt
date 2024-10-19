package com.taild.jetstudy.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taild.jetstudy.R
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.presentation.components.AddSubjectDialog
import com.taild.jetstudy.presentation.components.CountCard
import com.taild.jetstudy.presentation.components.DeleteDialog
import com.taild.jetstudy.presentation.components.SubjectCard
import com.taild.jetstudy.presentation.components.studySessionsList
import com.taild.jetstudy.presentation.components.taskList
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import com.taild.jetstudy.utils.SnackBarEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreen(
    uiState : DashboardState,
    tasks : List<Task>,
    sessions : List<Session>,
    snackBarEvent: SharedFlow<SnackBarEvent>?,
    onEvent: (DashboardEvent) -> Unit,
    onSubjectClick: (Subject) -> Unit,
    onStartSessionClick: () -> Unit,
    onTaskClick: (Task) -> Unit
) {

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(key1 = true) {
        snackBarEvent?.collectLatest { event ->
            when(event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                is SnackBarEvent.NavigateUp -> {}
            }
        }
    }

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        onDismissRequest = {
            isAddSubjectDialogOpen = false
        },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.OnSaveSubject)
            isAddSubjectDialogOpen = false
        },
        selectedColors = uiState.subjectCardColors,
        onColorChanged = {
            onEvent(DashboardEvent.OnSubjectCardColorChange(it))
        },
        subjectName = uiState.subjectName,
        onSubjectNameChanged = {
            onEvent(DashboardEvent.OnSubjectNameChange(it))
        },
        goalHours = uiState.goalStudyHours,
        onGoalHoursChanged = {
            onEvent(DashboardEvent.OnGoalStudyHoursChange(it))
        }
    )

    DeleteDialog(
        title = "Delete session?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
        "by this session's time. This action cannot be undone.",
        isOpen = isDeleteDialogOpen,
        onDismissRequest = {
            isDeleteDialogOpen = false 
        },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.
            OnDeleteSession)
            isDeleteDialogOpen = false
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            DashboardTopAppBar()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                CountCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = uiState.totalSubjectCount,
                    studiedHours = uiState.totalStudiedHours.toString(),
                    goalHours = uiState.totalGoalStudyHours.toString())
            }
            item {
                SubjectCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjects = uiState.subjects,
                    onAddIconClick = {
                        isAddSubjectDialogOpen = true
                    },
                    onSubjectClick = onSubjectClick
                )
            }
            item {
                Button(
                    onClick = onStartSessionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 12.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }
            taskList(
                title = "UPCOMING TASKS",
                emptyText = "You don't have any upcoming tasks.\n " +
                "Click the + button in subject screen to add new task.",
                tasks = tasks,
                onTaskCardClick = {
                    onTaskClick(it)
                },
                onCheckBoxClick = {
                    onEvent(DashboardEvent.OnTaskCompletedChange(it))
                }
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionsList(
                title = "RECENT STUDY SESSIONS",
                emptyText = "You don't have any recent study sessions.\n " +
                "Start a study session to begin recording your progress.",
                sessions = sessions,
                onDeleteClick = {
                    onEvent(DashboardEvent.OnDeleteSessionClick(it))
                    isDeleteDialogOpen = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            text = "JetStudy",
            style = MaterialTheme.typography.headlineMedium
        )
    })
}

@Composable
private fun CountCardsSection(
    modifier: Modifier = Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String
) {
    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount" )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours)
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours)
    }
}

@Composable
private fun SubjectCardsSection(
    modifier: Modifier = Modifier,
    subjects: List<Subject>,
    onAddIconClick: () -> Unit,
    onSubjectClick: (Subject) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(
                onClick = onAddIconClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }

        if (subjects.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.img_books),
                contentDescription = null
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "You don't have any subjects.\n Click the + button to add new subject.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = subjects) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors.map { Color(it) },
                    onClick = { onSubjectClick(subject) }
                )
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    JetStudyTheme {
        DashboardScreen(
            onEvent = {},
            snackBarEvent = null,
            tasks = emptyList(),
            sessions = emptyList(),
            uiState = DashboardState(),
            onSubjectClick = {},
            onStartSessionClick = {},
            onTaskClick = {}
        )
    }
}

