package com.example.indoorplantcare.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.indoorplantcare.R
import com.example.indoorplantcare.screens.notification.scheduleReminder
import java.util.Calendar


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "reminder_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getIntExtra("id", 0)
        val text = intent?.getStringExtra("text")

        if (context != null && id != null && text != null) {
            Toast.makeText(context, "Reminder #$id: $text", Toast.LENGTH_SHORT).show()

            // Schedule for the next week
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            val nextWeekTime = calendar.timeInMillis

            scheduleReminder(context, id, nextWeekTime, text)

            // Show notification
            showNotification(context, text)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, text: String) {
        createNotificationChannel(context)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Reminder Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for reminder notifications"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}