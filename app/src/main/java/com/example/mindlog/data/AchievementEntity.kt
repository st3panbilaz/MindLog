package com.example.mindlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность достижения. Инициализируется один раз при первом запуске,
 * потом только обновляется поле isUnlocked.
 */
@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    // Иконка в виде эмодзи — так проще, чем хранить ресурсы.
    val icon: String,
    val isUnlocked: Boolean = false
)