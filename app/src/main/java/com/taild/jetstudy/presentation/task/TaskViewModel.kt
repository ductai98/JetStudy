package com.taild.jetstudy.presentation.task

import androidx.lifecycle.ViewModel
import com.taild.jetstudy.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

}