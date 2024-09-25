package com.taild.jetstudy.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun DeleteDialog(
    title: String,
    body: String,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {

    if (isOpen) {
        AlertDialog(
            title = {
                Text(text = title)
            },
            text = {
                Text(
                    text = body
                )
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonClick
                ) {
                    Text(text = "Save")
                }
            },
            onDismissRequest = onDismissRequest
        )
    }
}