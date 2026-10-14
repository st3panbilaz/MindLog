package com.example.mindlog.repository

import com.example.mindlog.data.GoalDao
import com.example.mindlog.data.GoalEntity

/**
 * Репозиторий целей. Стандартный CRUD + счётчик выполненных.
 * ViewModel обращается только сюда, не зная, что внутри Room.
 */
class GoalRepository(private val goalDao: GoalDao) {

    // Создать новую цель.
    suspend fun insert(goal: GoalEntity) = goalDao.insert(goal)

    // Обновить существующую — например, отметить выполненной.
    suspend fun update(goal: GoalEntity) = goalDao.update(goal)

    // Удалить цель.
    suspend fun delete(goal: GoalEntity) = goalDao.delete(goal)

    // Получить все цели для отображения на экране.
    suspend fun getAllGoals(): List<GoalEntity> = goalDao.getAllGoals()

    suspend fun getCompletedGoalsCount(): Int = goalDao.getCompletedGoalsCount()
}