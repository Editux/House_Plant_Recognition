package com.example.indoorplantcare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IndoorPlantCare: Application(){
    override fun onCreate() {
        super.onCreate()
        val notificationChannel= NotificationChannel(
            "reminder_channel",
            "Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}