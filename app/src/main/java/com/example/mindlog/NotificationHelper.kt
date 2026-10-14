package com.example.mindlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Хелпер для создания канала уведомлений.
 * На Android 8.0+ каналы обязательны, иначе уведомления не показываются.
 */
object NotificationHelper {

    const val CHANNEL_ID = "achievements_channel"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Достижения",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о получении достижений"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}