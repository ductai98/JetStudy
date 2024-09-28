package com.taild.jetstudy.presentation.components

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.taild.jetstudy.domain.model.Session
import com.taild.jetstudy.domain.model.Subject
import com.taild.jetstudy.domain.model.Task
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
object DashboardRoute

@Serializable
data class SubjectRoute(val subject: Subject)

@Serializable
data class TaskRoute(val task: Task)

@Serializable
data object SessionRoute

object JetStudyNavTypes {
    val SubjectType = object : NavType<Subject>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Subject? {
            return Json.decodeFromString(bundle.getString(key, null))
        }

        override fun parseValue(value: String): Subject {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: Subject): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Subject) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val TaskType = object : NavType<Task>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Task? {
            return Json.decodeFromString(bundle.getString(key, null))
        }

        override fun parseValue(value: String): Task {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: Task): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Task) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val SessionType = object : NavType<Session>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Session? {
            return Json.decodeFromString(bundle.getString(key, null))
        }

        override fun parseValue(value: String): Session {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: Session): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Session) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}