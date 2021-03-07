package com.example.androiddevchallenge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.TextField

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
            Row {
                TimerInputField(
                    timerValue = timerValue,
                    timerStarted = timerStarted,
                    updateTimer = updateTimer
                )
                Button(onClick = {
                    startTimer()
                }) {
                    Text("Start")
                }
            }
            Column() {
                Text(text = toTimer(timerValue))
                if(timerFinished) {
                    Text(text = "finished")
                }
                Row() {
                    Button(onClick = {
                        stopTimer()
                    }) {
                        Text("Pause")
                    }
                    Button(onClick = {
                        updateTimer(0.toString())
                    }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimerInputField(
    modifier: Modifier = Modifier,
    timerValue: Int,
    timerStarted: Boolean,
    updateTimer: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = timerValue.toString(),
        onValueChange = updateTimer,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hideSoftwareKeyboard()
        })
    )
}

fun toTimer(value: Int): String {
    val min = value / 100
    val sec = value % 100
    return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
}
