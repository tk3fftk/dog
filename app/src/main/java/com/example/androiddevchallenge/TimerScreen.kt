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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerScreen(timerViewModel: TimerViewModel) {
    var timerValue = timerViewModel.timerValue
    var timerStarted = timerViewModel.timerStarted
    var timerFinished = timerViewModel.timerFinished
    val updateTimer = timerViewModel::updateTimer
    val startTimer = timerViewModel::startTimer
    val stopTimer = timerViewModel::stopTimer

    Surface(color = MaterialTheme.colors.background) {
        Column() {
            Column() {
                TimerField(
                    modifier = Modifier,
                    timerValue = timerValue,
                    timerFinished = timerFinished
                )
                /*
                TimerFieldWithInput(
                    timerValue = timerValue,
                    timerStarted = timerStarted,
                    updateTimer = updateTimer,
                    timerFinished = timerFinished,
                    onImeAction = startTimer,
                )
                 */
                TimerButtons(
                    stopTimer = stopTimer,
                    updateTimer = updateTimer,
                    startTimer = startTimer,
                    timerStarted = timerStarted,
                )
                if (!timerStarted) {
                    TimerInput(
                        modifier = Modifier,
                        timerValue = timerValue,
                        timerStarted = timerStarted,
                        updateTimer = updateTimer,
                        startTimer = startTimer,
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerButtons(
    modifier: Modifier = Modifier,
    stopTimer: () -> Unit,
    updateTimer: (value: String) -> Unit,
    startTimer: () -> Unit,
    timerStarted: Boolean,
) {
    Row(
        modifier = modifier
            .padding(all = 64.dp)
            .fillMaxWidth()
    ) {
        val buttonModifier = Modifier.requiredSize(width = 128.dp, height = 64.dp)
        val buttonFontSize = 24.sp

        Button(
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            onClick = {
                if (timerStarted) {
                    stopTimer()
                } else {
                    startTimer()
                }
            }
        ) {
            if (timerStarted) {
                Text(
                    text = "Pause",
                    fontSize = buttonFontSize,
                    color = Color.White
                )
            } else {
                Text(
                    text = "Resume",
                    fontSize = buttonFontSize,
                    color = Color.White
                )
            }
        }
        Button(
            modifier = buttonModifier.offset(x = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = {
                updateTimer(0.toString())
            }
        ) {
            Text(
                text = "Clear",
                fontSize = buttonFontSize,
                color = Color.Blue
            )
        }
    }
}

@Composable
private fun TimerInput(
    modifier: Modifier = Modifier,
    timerValue: Int,
    timerStarted: Boolean,
    updateTimer: (String) -> Unit,
    startTimer: () -> Unit
) {
    Row() {
        TimerInputField(
            modifier = modifier.padding(all = 8.dp),
            timerValue = timerValue,
            timerStarted = timerStarted,
            updateTimer = updateTimer,
            onImeAction = startTimer
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimerInputField(
    modifier: Modifier = Modifier,
    timerValue: Int,
    timerStarted: Boolean,
    updateTimer: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = modifier.padding(32.dp),
        value = timerValue.toString(),
        onValueChange = updateTimer,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        label = { Text("input time here!") },
        maxLines = 1,
        enabled = !timerStarted,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hideSoftwareKeyboard()
            }
        )
    )
}

@Composable
private fun TimerField(modifier: Modifier = Modifier, timerValue: Int, timerFinished: Boolean) {
    Text(
        modifier = modifier
            .padding(top = 150.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 128.sp,
        text = toTimerString(timerValue)
    )
    if (timerFinished) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            text = "finished\uD83D\uDE80"
        )
    }
}

fun toTimerString(value: Int): String {
    val min = value / 100
    val sec = value % 100
    return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TimerFieldWithInput(
    modifier: Modifier = Modifier,
    timerValue: Int,
    timerStarted: Boolean,
    updateTimer: (String) -> Unit,
    onImeAction: () -> Unit = {},
    timerFinished: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = toTimerString(timerValue),
        onValueChange = updateTimer,
        modifier = modifier
            .padding(top = 150.dp)
            .fillMaxWidth(),
        enabled = !timerStarted,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 128.sp),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hideSoftwareKeyboard()
            }
        )

    )
    if (timerFinished) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            text = "finished\uD83D\uDE80"
        )
    }
}

@Preview
@Composable
fun DarkPreview() {
    TimerButtons(
        stopTimer = { /*TODO*/ },
        updateTimer = { /*TODO*/ },
        timerStarted = true,
        startTimer = {}
    )
}
