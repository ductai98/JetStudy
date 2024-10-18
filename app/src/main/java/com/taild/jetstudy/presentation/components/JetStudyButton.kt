package com.taild.jetstudy.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun JetStudyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "Confirm",
    enabled: Boolean = true,
    colors: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = colors, contentColor = Color.White)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            text = text,
        )
    }
}