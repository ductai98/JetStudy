package com.taild.jetstudy.presentation.subject

import androidx.lifecycle.ViewModel
import com.taild.jetstudy.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

}