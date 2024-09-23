package com.taild.jetstudy.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taild.jetstudy.R
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import com.taild.jetstudy.presentation.components.CountCard
import com.taild.jetstudy.presentation.components.SubjectCard
import com.taild.jetstudy.presentation.components.taskList

@Composable
fun DashboardScreen() {
    val subjects = listOf(
        Subject(name = "English", goalHours = 10f, colors = Subject.subjectCardColors[0]),
        Subject(name = "Physics", goalHours = 10f, colors = Subject.subjectCardColors[1]),
        Subject(name = "Maths", goalHours = 10f, colors = Subject.subjectCardColors[2]),
        Subject(name = "Geology", goalHours = 10f, colors = Subject.subjectCardColors[3]),
        Subject(name = "Fine Arts", goalHours = 10f, colors = Subject.subjectCardColors[0])
    )

    val tasks = listOf(
        Task(
            title = "Prepate notes",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isCompleted = false
        ),
        Task(
            title = "Do homework",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isCompleted = true
        ),
        Task(
            title = "Go Coaching",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isCompleted = false
        ),
        Task(
            title = "Assignment",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isCompleted = false
        ),
        Task(
            title = "Write poem",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isCompleted = true
        )
    )


    Scaffold(
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
                    subjectCount = 5,
                    studiedHours = "10",
                    goalHours = "10")
            }
            item {
                SubjectCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjects = subjects
                )
            }
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 12.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }
            taskList(tasks = tasks)
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
    subjects: List<Subject>
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
                onClick = { /*TODO*/ }
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
                    gradientColors = subject.colors,
                    onClick = {}
                )
            }
        }
    }
}

