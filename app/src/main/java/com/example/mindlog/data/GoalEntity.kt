package com.example.mindlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность цели. Пользователь создаёт цель с дедлайном,
 * а потом может отметить её как выполненную.
 */
@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    // Дедлайн храним строкой, потому что формат простой (день/месяц).
    // Если понадобится — можно заменить на Date.
    val deadline: String,
    // По умолчанию цель не выполнена.
    val isCompleted: Boolean = false
)