package com.taild.jetstudy.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.taild.jetstudy.R
import com.taild.jetstudy.domain.model.Task

fun LazyListScope.taskList(
    tasks: List<Task>
) {
    item {
        Text(
            text = "UPCOMING TASKS",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp)
        )
    }
    if (tasks.isEmpty()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp),
                    painter = painterResource(id = R.drawable.img_tasks),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "You don't have any upcoming tasks.\n " +
                            "Click the + button in subject screen to add new task.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    items(items = tasks) { task ->
        TaskCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            task = task,
            onClick = { },
            onCheckBoxClick = {}
        )
    }
}

@Composable
private fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onClick: () -> Unit,
    onCheckBoxClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TaskCheckBox(
                isCompleted = task.isCompleted,
                onCheckBoxClick = {
                    onCheckBoxClick()
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${task.dueDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}