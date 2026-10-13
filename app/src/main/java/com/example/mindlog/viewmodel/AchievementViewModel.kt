package com.example.mindlog.viewmodel

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindlog.NotificationHelper
import com.example.mindlog.data.AchievementEntity
import com.example.mindlog.data.AppDatabase
import com.example.mindlog.repository.AchievementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AchievementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AchievementRepository

    private val _achievements = MutableStateFlow<List<AchievementEntity>>(emptyList())
    val achievements: StateFlow<List<AchievementEntity>> = _achievements

    // Счётчик уведомлений, чтобы у каждой было уникальное id
    private var notificationId = 1

    init {
        val db = AppDatabase.getInstance(application)
        repository = AchievementRepository(db.achievementDao())
        viewModelScope.launch {
            if (repository.getAllAchievements().isEmpty()) {
                initAchievements()
            }
            loadAchievements()
            checkAchievements()
        }
    }

    private suspend fun initAchievements() {
        repository.insert(AchievementEntity(title = "Первая цель", description = "Выполните первую цель", icon = "🏆"))
        repository.insert(AchievementEntity(title = "Мастер целей", description = "Выполните 10 целей", icon = "🥇"))
        repository.insert(AchievementEntity(title = "Первый шаг", description = "Создайте первую заметку", icon = "📝"))
        repository.insert(AchievementEntity(title = "Писатель", description = "Создайте 10 заметок", icon = "✍️"))
        repository.insert(AchievementEntity(title = "Эмоциональный", description = "Добавьте первую эмоцию", icon = "😊"))
        repository.insert(AchievementEntity(title = "Гуру эмоций", description = "Добавьте 10 эмоций", icon = "🌟"))
    }

    fun loadAchievements() {
        viewModelScope.launch {
            _achievements.value = repository.getAllAchievements()
        }
    }

    fun checkAchievements() {
        viewModelScope.launch {
            val all = repository.getAllAchievements()
            val goalsCount = repository.getCompletedGoalsCount()
            val notesCount = repository.getNotesCount()
            val moodsCount = repository.getMoodEntriesCount()

            for (a in all) {
                val shouldUnlock = when (a.title) {
                    "Первая цель" -> goalsCount >= 1
                    "Мастер целей" -> goalsCount >= 10
                    "Первый шаг" -> notesCount >= 1
                    "Писатель" -> notesCount >= 10
                    "Эмоциональный" -> moodsCount >= 1
                    "Гуру эмоций" -> moodsCount >= 10
                    else -> false
                }
                if (shouldUnlock && !a.isUnlocked) {
                    // Разблокируем достижение
                    repository.update(a.copy(isUnlocked = true))
                    // Отправляем уведомление
                    sendAchievementNotification(a)
                }
            }
            loadAchievements()
        }
    }

    private fun sendAchievementNotification(achievement: AchievementEntity) {
        val context = getApplication<Application>()

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.star_on)
            .setContentTitle("${achievement.icon} Достижение получено!")
            .setContentText("${achievement.title}: ${achievement.description}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId++, notification)
    }
}