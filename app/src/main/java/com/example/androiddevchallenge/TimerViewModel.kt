package com.example.androiddevchallenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.schedule

class TimerViewModel : ViewModel() {
    var timerStarted by mutableStateOf(false)
    var timerFinished by mutableStateOf(false)
        protected set
    var timerValue by mutableStateOf(0)
    private var timer = Timer()

    fun updateTimer(value: String) {
        if (!value.isNullOrEmpty()) {

            var valueInt = value.toInt()
            if (timerStarted && (valueInt % 100) == 99 ) {
                valueInt -= 40
            }
            if (valueInt <= 0 && timerStarted) {
                valueInt = 0
                timerFinished = true
                stopTimer()
            }
            timerValue = valueInt
        } else {
            timerValue = 0
        }
    }

    fun startTimer() {
        timerFinished = false

        if ((timerValue % 100) / 60 >= 1.0 ) {
            timerValue += 40
        }

        timer = Timer()
        timer.schedule(delay = 1000, period = 1000) {
            updateTimer((timerValue - 1).toString())
        }
        timerStarted = true
    }

    fun stopTimer() {
        timer.cancel()
        timerStarted = false
    }
}