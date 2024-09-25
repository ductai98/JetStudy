package com.taild.jetstudy.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.presentation.components.AddSubjectDialog
import com.taild.jetstudy.presentation.components.CountCard
import com.taild.jetstudy.presentation.components.DeleteDialog
import com.taild.jetstudy.presentation.components.studySessionsList
import com.taild.jetstudy.presentation.components.taskList
import com.taild.jetstudy.sessions
import com.taild.jetstudy.tasks

const val TAG = "SubjectScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen() {
    val listState = rememberLazyListState()
    //Log.d(TAG, "SubjectScreen: index = ${listState.firstVisibleItemIndex}")
    val isFABExtended by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    var subjectName by rememberSaveable { mutableStateOf("") }
    var goalHours by rememberSaveable { mutableStateOf("") }
    var selectedColors by rememberSaveable { mutableStateOf(Subject.subjectCardColors.random()) }


    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        onDismissRequest = {
            isAddSubjectDialogOpen = false
        },
        onConfirmButtonClick = {
            isAddSubjectDialogOpen = false
        },
        selectedColors = selectedColors,
        onColorChanged = {
            selectedColors = it
        },
        subjectName = subjectName,
        onSubjectNameChanged = {
            subjectName = it
        },
        goalHours = goalHours,
        onGoalHoursChanged = {
            goalHours = it
        }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete subject?",
        body = "Are you sure, you want to delete this subject? All related " +
                "task and studied sessions will be permanently removed. This action cannot be undone.",
        onDismissRequest = { isDeleteSubjectDialogOpen = false },
        onConfirmButtonClick = { isDeleteSubjectDialogOpen = false }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete session?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session's time. This action cannot be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = { isDeleteSessionDialogOpen = false }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectTopAppBar(
                scrollBehavior = scrollBehavior,
                onBackClick = { /*TODO*/ },
                onDeleteClick = { isDeleteSubjectDialogOpen = true },
                onEditClick = { isAddSubjectDialogOpen = true })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add Task") },
                icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) },
                onClick = { /*TODO*/ },
                expanded = isFABExtended)
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    studiedHours = "10",
                    goalHours = "15",
                    progress = 0.75f
                )
            }
            taskList(
                title = "UPCOMING TASKS",
                emptyText = "You don't have any upcoming tasks.\n" +
                "Click the + button to add new tasks",
                tasks = tasks,
                onTaskCardClick = {},
                onCheckBoxClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                title = "COMPLETED TASKS",
                emptyText = "You don't have any completed tasks.\n" +
                "Click the checkbox to mark a task as completed",
                tasks = tasks,
                onTaskCardClick = {},
                onCheckBoxClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionsList(
                title = "RECENT STUDY SESSIONS",
                emptyText = "You don't have any recent study sessions.\n"+
                "Start a study session to begin recording your progress.",
                sessions = sessions,
                onDeleteClick = { isDeleteSessionDialogOpen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Subject",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null)
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null)
            }
        }
    )
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier = Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float
) {
    val percentageProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0,100)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CountCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            headingText = "Goal Study Hours",
            count = goalHours)
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            headingText = "Study Hours",
            count = studiedHours)
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = {
                    1f
                },
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = {
                    progress
                },
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentageProgress%")
        }
    }
}

@Preview
@Composable
fun SubjectScreenPreview() {
    SubjectScreen()
}