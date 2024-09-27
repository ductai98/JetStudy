package com.taild.jetstudy.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JetStudyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "Confirm"
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            text = text,
        )
    }
}