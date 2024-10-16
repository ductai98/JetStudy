package com.taild.jetstudy.presentation.session

import androidx.lifecycle.ViewModel
import com.taild.jetstudy.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

}