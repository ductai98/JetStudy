package com.taild.jetstudy.domain.model

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import com.taild.jetstudy.presentation.theme.gradient1
import com.taild.jetstudy.presentation.theme.gradient2
import com.taild.jetstudy.presentation.theme.gradient3
import com.taild.jetstudy.presentation.theme.gradient4
import com.taild.jetstudy.presentation.theme.gradient5
import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Int? = null,
    val name: String,
    val goalHours: Float,
    @ColorInt val colors: List<Int>
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