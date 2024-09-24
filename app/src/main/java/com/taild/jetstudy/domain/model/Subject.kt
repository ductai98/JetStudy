package com.taild.jetstudy.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.taild.jetstudy.presentation.theme.gradient1
import com.taild.jetstudy.presentation.theme.gradient2
import com.taild.jetstudy.presentation.theme.gradient3
import com.taild.jetstudy.presentation.theme.gradient4
import com.taild.jetstudy.presentation.theme.gradient5

data class Subject(
    val id: Int,
    val name: String,
    val goalHours: Float,
    val colors: List<Color>
) {
    companion object {
        val subjectCardColors = listOf(
            gradient1.map {
                it.toArgb()
            },
            gradient2.map {
                it.toArgb()
            },
            gradient3.map {
                it.toArgb()
            },
            gradient4.map {
                it.toArgb()
            },
            gradient5.map {
                it.toArgb()
            }
        )
    }
}