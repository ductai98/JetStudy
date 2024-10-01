package com.taild.jetstudy.data.local

import androidx.annotation.ColorRes
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromColorList(@ColorRes colors: List<Int>) : String {
        return colors.joinToString(",") {
            it.toString()
        }
    }

    @TypeConverter
    fun toColorList(colors: String) : List<Int> {
        return colors.split(",").map {
            it.toInt()
        }
    }
}