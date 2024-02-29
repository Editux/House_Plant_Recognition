package com.example.indoorplantcare.screens.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import com.example.indoorplantcare.service.AlarmReceiver
import com.example.indoorplantcare.ui.theme.TealPrimary
import java.util.Calendar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReminderScreen(navController: NavController) {
    var reminderText by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var reminderId = 1

    val context = LocalContext.current

    Scaffold(
        topBar = {
            //TopBar
            TopAppBar(
                backgroundColor = TealPrimary,
                elevation = 0.dp
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        })
                    Spacer(modifier = Modifier.width(120.dp))
                    Text(text = "Notifications", fontWeight = FontWeight.Bold, color = Color.White,)
                    Spacer(modifier = Modifier.width(115.dp))

                    }

                }

        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = reminderText,
                onValueChange = { reminderText = it },
                label = { Text("Reminder Text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = reminderTime,
                onValueChange = { reminderTime = it },
                label = { Text("Reminder Time (HH:mm)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = {
                    val calendar = Calendar.getInstance()
                    val currentTime = calendar.timeInMillis
                    val selectedTime = parseTime(reminderTime)

                    if (selectedTime > currentTime) {
                        scheduleReminder(context, reminderId, selectedTime, reminderText)
                        reminderId++
                    } else {
                        // Show error message or handle invalid time
                    }
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        cancelReminder(context, reminderId - 1)
                        reminderId--
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel Last Reminder")
                }

                IconButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        val currentTime = calendar.timeInMillis
                        val selectedTime = parseTime(reminderTime)

                        if (selectedTime > currentTime) {
                            scheduleReminder(context, reminderId, selectedTime, reminderText)
                            reminderId++
                        } else {
                            // Show error message or handle invalid time
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {

                    Text("Schedule Reminder")
                }
            }
        }
    }
}

fun parseTime(timeString: String): Long {
    val calendar = Calendar.getInstance()
    val tokens = timeString.split(":")
    if (tokens.size == 2) {
        val hours = tokens[0].toInt()
        val minutes = tokens[1].toInt()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

@SuppressLint("ScheduleExactAlarm")
fun scheduleReminder(context: Context, id: Int, timeInMillis: Long, text: String) {
    val alarmManager = getSystemService(context, AlarmManager::class.java)
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("id", id)
        putExtra("text", text)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id,
        intent,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            0
        }
    )
    alarmManager?.setExact(
        AlarmManager.RTC_WAKEUP,
        timeInMillis,
        pendingIntent
    )
}

fun cancelReminder(context: Context, id: Int) {
    val alarmManager = getSystemService(context, AlarmManager::class.java)
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    alarmManager?.cancel(pendingIntent)
}