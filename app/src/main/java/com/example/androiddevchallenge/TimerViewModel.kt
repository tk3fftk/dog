/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Timer
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
            if (timerStarted && (valueInt % 100) == 99) {
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

        if ((timerValue % 100) / 60 >= 1.0) {
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
