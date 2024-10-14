package com.taild.jetstudy.presentation.session

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.taild.jetstudy.presentation.components.DeleteDialog
import com.taild.jetstudy.presentation.components.JetStudyButton
import com.taild.jetstudy.presentation.components.RelatedToSubjectSession
import com.taild.jetstudy.presentation.components.SubjectListBottomSheet
import com.taild.jetstudy.presentation.components.studySessionsList
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import com.taild.jetstudy.fakeSessions
import com.taild.jetstudy.fakeSubjects
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_START
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var subjectText by rememberSaveable { mutableStateOf("(Please select a subject)") }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

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
            SessionTopBar(onBackButtonClick = {})
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
                        .aspectRatio(1f)
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
                            action = ACTION_SERVICE_START
                        )
                    },
                    onFinishButtonClick = { /*TODO*/ },
                    onPauseButtonClick = {}
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
            IconButton(onClick = { /*TODO*/ }) {
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
    modifier: Modifier = Modifier
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
            Text(
                text = "00:40:12",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
            )
        }
    }
}

@Composable
private fun ButtonSession(
    modifier: Modifier = Modifier,
    onStartButtonClick: () -> Unit,
    onFinishButtonClick: () -> Unit,
    onPauseButtonClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        JetStudyButton(
            text = "Start",
            onClick = onStartButtonClick
        )
        JetStudyButton(
            text = "Finish",
            onClick = onFinishButtonClick
        )
        JetStudyButton(
            text = "Pause",
            onClick = onPauseButtonClick
        )
    }
}

@Preview
@Composable
fun SessionScreenPreview() {
    JetStudyTheme {
        SessionScreen(
            onBackClick = {}
        )
    }
}