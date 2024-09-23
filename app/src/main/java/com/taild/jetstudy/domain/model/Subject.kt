package com.taild.jetstudy.domain.model

import androidx.compose.ui.graphics.Color
import com.taild.jetstudy.presentation.theme.gradient1
import com.taild.jetstudy.presentation.theme.gradient2
import com.taild.jetstudy.presentation.theme.gradient3
import com.taild.jetstudy.presentation.theme.gradient4
import com.taild.jetstudy.presentation.theme.gradient5

data class Subject(
    val name: String,
    val goalHours: Float,
    val colors: List<Color>
) {
    companion object {
        val subjectCardColors = listOf(
            gradient1,
            gradient2,
            gradient3,
            gradient4,
            gradient5
        )
    }
}