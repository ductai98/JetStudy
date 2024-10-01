package com.taild.jetstudy.utils

import com.taild.jetstudy.data.dto.SessionDto
import com.taild.jetstudy.data.dto.SubjectDto
import com.taild.jetstudy.data.dto.TaskDto
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task

fun TaskDto.toTask(): Task = Task(
    id = id ?: 0,
    subjectId = subjectId,
    title = title,
    description = description,
    dueDate = dueDate,
    priority = priority,
    relatedToSubject = relatedToSubject,
    isCompleted = isCompleted
)

fun SessionDto.toSession() : Session = Session(
    id = id ?: 0,
    subjectId = subjectId,
    relatedToSubject = relatedToSubject,
    date = date,
    duration = duration
)

fun SubjectDto.toSubject() : Subject = Subject(
    id = id ?: 0,
    name = name,
    goalHours = goalHours,
    colors = colors
)

fun List<SessionDto>.toSessionList() : List<Session> = map {
    it.toSession()
}

fun List<SubjectDto>.toSubjectList() : List<Subject> = map { it.toSubject() }

fun List<TaskDto>.toTaskList() : List<Task> = map { it.toTask() }