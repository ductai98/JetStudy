package com.taild.jetstudy.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.presentation.components.DeleteDialog
import com.taild.jetstudy.presentation.components.FutureOrPresentSelectableDates
import com.taild.jetstudy.presentation.components.RelatedToSubjectSession
import com.taild.jetstudy.presentation.components.SubjectListBottomSheet
import com.taild.jetstudy.presentation.components.TaskCheckBox
import com.taild.jetstudy.presentation.components.TaskDatePicker
import com.taild.jetstudy.presentation.theme.JetStudyTheme
import com.taild.jetstudy.presentation.theme.Red
import com.taild.jetstudy.subjects
import com.taild.jetstudy.utils.Priority
import com.taild.jetstudy.utils.toDateString
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    task: Task,
    onBackClick: () -> Unit
) {
    val viewModel : TaskViewModel = hiltViewModel()
    
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
        selectableDates = FutureOrPresentSelectableDates
    )

    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var subjectText by rememberSaveable { mutableStateOf("(Please select a subject)") }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var titleError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    titleError = when {
        title.isBlank() -> "Please enter the task title."
        title.length < 4 -> "Title must be at least 4 characters."
        title.length > 30 -> "Title must not exceed 30 characters."
        else -> null
    }

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Task?",
        body = "Are you sure, you want to delete this task? " +
                "This action can not be undone.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = { isDeleteDialogOpen = false }
    )

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = { isDatePickerDialogOpen = false },
        onConfirmButtonClick = { isDatePickerDialogOpen = false }
    )

    SubjectListBottomSheet(
        isOpen = isBottomSheetOpen,
        sheetState = sheetState,
        subjects = subjects,
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
            TaskScreenTopBar(
                exist = true,
                completed = false,
                checkBoxBorderColor = Red,
                onBackButtonClick = onBackClick,
                onDeleteButtonClick = { isDeleteDialogOpen = true },
                onCheckBoxClick = {})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = {
                    Text(text = "Title")
                },
                singleLine = true,
                isError = titleError != null && title.isNotBlank(),
                supportingText = {
                    Text(
                        text = titleError.orEmpty()
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = {
                    Text(text = "Description")
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Due Date",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { isDatePickerDialogOpen = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = datePickerState.selectedDateMillis.toDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Priority",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Priority.entries.forEach {
                    PriorityButton(
                        modifier = Modifier.weight(1f),
                        label = it.title,
                        backgroundColor = it.color,
                        borderColor = if (it == Priority.MEDIUM) {
                            Color.White
                        } else { Color.Transparent },
                        labelColor = if (it == Priority.MEDIUM) {
                            Color.White
                        } else { Color.White.copy(alpha = 0.7f) },
                        onClick = {}
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            RelatedToSubjectSession(
                onSubjectClick = { isBottomSheetOpen = true },
                subjectText = subjectText
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                onClick = { /*TODO*/ },
                enabled = titleError == null
            ) {
                Text(text = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    exist: Boolean,
    completed: Boolean,
    checkBoxBorderColor: Color,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onCheckBoxClick: () -> Unit
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
                text = "Task",
                style = MaterialTheme.typography.headlineSmall)
                },
        actions = {
            if (exist) {
                TaskCheckBox(
                    borderColor = checkBoxBorderColor,
                    isCompleted = completed,
                    onCheckBoxClick = onCheckBoxClick
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = labelColor
        )
    }
}

@Preview
@Composable
fun TaskScreenPreview() {
    JetStudyTheme {
        TaskScreen(
            task = Task(
                id = 1,
                subjectId = 0,
                title = "Prepare notes",
                description = "",
                dueDate = 0L,
                priority = 0,
                relatedToSubject = "",
                isCompleted = false
            ),
            onBackClick = {}
        )
    }
}