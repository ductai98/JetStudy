package com.taild.jetstudy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.taild.jetstudy.domain.model.Subject

@Composable
fun AddSubjectDialog(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    selectedColors: List<Color>,
    onColorChanged: (List<Color>) -> Unit,
    subjectName: String,
    onSubjectNameChanged: (String) -> Unit,
    goalHours: String,
    onGoalHoursChanged: (String) -> Unit
) {
    var subjectNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var goalHoursError by rememberSaveable { mutableStateOf<String?>(null) }

    subjectNameError = when {
        subjectName.isBlank() -> "Please enter subject name."
        subjectName.length < 3 -> "Subject name is too short."
        subjectName.length > 20 -> "Subject name is too long."
        else -> null
    }

    goalHoursError = when {
        goalHours.isBlank() -> "Please enter goal study hours."
        goalHours.toFloatOrNull() == null -> "Please enter a valid number."
        goalHours.toFloat() < 1f -> "Goal study hours should be at least 1 hour."
        goalHours.toFloat() > 1000f -> "Goal study hours should be maximum of 1000 hour."
        else -> null
    }

    if (isOpen) {
        AlertDialog(
            title = {
                Text(text = "Add/Update Subject")
            },
            text = {
                DialogBody(
                    selectedColors = selectedColors,
                    onColorChanged = onColorChanged,
                    subjectName = subjectName,
                    onSubjectNameChanged = onSubjectNameChanged,
                    goalHours = goalHours,
                    onGoalHoursChanged = onGoalHoursChanged,
                    subjectNameError = subjectNameError,
                    goalHoursError = goalHoursError
                )
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonClick,
                    enabled = subjectNameError == null && goalHoursError == null
                ) {
                    Text(text = "Save")
                }
            },
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun DialogBody(
    selectedColors: List<Color>,
    onColorChanged: (List<Color>) -> Unit,
    subjectName: String,
    onSubjectNameChanged: (String) -> Unit,
    goalHours: String,
    onGoalHoursChanged: (String) -> Unit,
    subjectNameError: String?,
    goalHoursError: String?
    ) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Subject.subjectCardColors.forEach { colors ->
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(brush = Brush.verticalGradient(colors))
                        .border(
                            width = 2.dp,
                            color = if (colors == selectedColors) {
                                Color.Black
                            } else {
                                Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .clickable {
                            onColorChanged(colors)
                        }
                )
            }
        }
        OutlinedTextField(
            value = subjectName,
            onValueChange = onSubjectNameChanged,
            label = {
                Text(text = "Subject Name")
            },
            singleLine = true,
            isError = subjectNameError != null && subjectName.isNotBlank(),
            supportingText = { Text(text = subjectNameError.orEmpty()) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = goalHours,
            onValueChange = onGoalHoursChanged,
            label = {
                Text(text = "Study Goal Hours")
            },
            singleLine = true,
            isError = goalHoursError != null && goalHours.isNotBlank(),
            supportingText = { Text(text = goalHoursError.orEmpty()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}