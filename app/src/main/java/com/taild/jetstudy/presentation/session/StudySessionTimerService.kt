package com.taild.jetstudy.presentation.session

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_CANCEL
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_START
import com.taild.jetstudy.utils.Constant.ACTION_SERVICE_STOP
import com.taild.jetstudy.utils.Constant.NOTIFICATION_CHANNEL_ID
import com.taild.jetstudy.utils.Constant.NOTIFICATION_CHANNEL_NAME
import com.taild.jetstudy.utils.Constant.NOTIFICATION_ID
import com.taild.jetstudy.utils.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class StudySessionTimerService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private lateinit var timer: Timer

    var duration: Duration = Duration.ZERO
        private set

    var seconds = mutableStateOf("00")
        private set

    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var currentState = mutableStateOf(TimerState.IDLE)
        private set

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when(it) {
                ACTION_SERVICE_START -> {

                }
                ACTION_SERVICE_STOP -> {}
                ACTION_SERVICE_CANCEL -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun startTimer(
        onTick: (h: String, m: String, s: String) -> Unit
    ) {
        currentState.value = TimerState.STARTED
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this.hours.value = hours.toInt().pad()
            this.minutes.value = minutes.pad()
            this.seconds.value = seconds.pad()
        }
    }

    enum class TimerState {
        IDLE,
        STARTED,
        STOP
    }
}