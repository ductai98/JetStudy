package com.taild.jetstudy.presentation.session

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taild.jetstudy.presentation.components.DeleteDialog
import com.taild.jetstudy.presentation.components.JetStudyButton
import com.taild.jetstudy.presentation.components.RelatedToSubjectSession
import com.taild.jetstudy.presentation.components.SubjectListBottomSheet
import com.taild.jetstudy.presentation.components.studySessionsList
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import com.taild.jetstudy.fakeSessions
import com.taild.jetstudy.fakeSubjects
import com.taild.jetstudy.presentation.theme.Red
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_CANCEL
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_START
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_STOP
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onBackClick: () -> Unit,
    timerService: StudySessionTimerService
) {
    val context = LocalContext.current
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var subjectText by rememberSaveable { mutableStateOf("(Please select a subject)") }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds
    val currentTimerState by timerService.currentTimerState

    val scope = rememberCoroutineScope()

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete session?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session's time. This action cannot be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = { isDeleteSessionDialogOpen = false }
    )

    SubjectListBottomSheet(
        isOpen = isBottomSheetOpen,
        sheetState = sheetState,
        subjects = fakeSubjects,
        onSubjectClick = {subject ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    isBottomSheetOpen = false
                }
                subjectText = subject.name
            }
        },
        onDismissRequest = { isBottomSheetOpen = false }
    )

    Scaffold(
        topBar = {
            SessionTopBar(onBackButtonClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                TimerSession(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                RelatedToSubjectSession(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    onSubjectClick = { isBottomSheetOpen = true },
                    subjectText = subjectText
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                ButtonSession(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    onStartButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = if (currentTimerState == TimerState.STARTED) {
                                ACTION_SERVICE_STOP
                            } else {
                                ACTION_SERVICE_START
                            }
                        )
                    },
                    onFinishButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_STOP
                        )
                    },
                    onCancelButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    },
                    timerState = currentTimerState,
                    seconds = seconds
                )
            }
            studySessionsList(
                title = "STUDY SESSION HISTORY",
                emptyText = "You don't have any recent study sessions.\n"+
                        "Start a study session to begin recording your progress.",
                sessions = fakeSessions,
                onDeleteClick = { isDeleteSessionDialogOpen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = "Study Session",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun TimerSession(
    modifier: Modifier = Modifier,
    hours: String = "00",
    minutes: String = "00",
    seconds: String = "0"
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(
                    width = 5.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ){
            Row {
                AnimatedContent(
                    targetState = hours,
                    label = hours,
                    transitionSpec = { timerTextAnimation() }
                ) {hours ->
                    Text(
                        text = "$hours:",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                    )
                }
                AnimatedContent(
                    targetState = minutes,
                    label = minutes,
                    transitionSpec = { timerTextAnimation() }
                ) {minutes ->
                    Text(
                        text = "$minutes:",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                    )
                }
                AnimatedContent(
                    targetState = seconds,
                    label = seconds,
                    transitionSpec = { timerTextAnimation() }
                ) {seconds ->
                    Text(
                        text = seconds,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonSession(
    modifier: Modifier = Modifier,
    onStartButtonClick: () -> Unit,
    onFinishButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        JetStudyButton(
            text = when(timerState) {
                TimerState.STARTED -> "Stop"
                TimerState.STOPPED -> "Resume"
                else -> "Start"
            },
            onClick = onStartButtonClick,
            colors = if (timerState == TimerState.STARTED) {
                Red
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        JetStudyButton(
            text = "Finish",
            onClick = onFinishButtonClick,
            enabled = seconds != "00" && timerState != TimerState.STARTED
        )
        JetStudyButton(
            text = "Cancel",
            onClick = onCancelButtonClick,
            enabled = seconds != "00" && timerState != TimerState.STARTED
        )
    }
}

private fun timerTextAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(duration)) { fullHeight -> fullHeight} +
            fadeIn(animationSpec = tween(duration)) togetherWith
            slideOutVertically(animationSpec = tween(duration)) { fullHeight -> -fullHeight} +
            fadeOut(animationSpec = tween(duration))
}

@Preview
@Composable
fun SessionScreenPreview() {
    JetStudyTheme {
        SessionScreen(
            onBackClick = {},
            timerService = StudySessionTimerService()
        )
    }
}