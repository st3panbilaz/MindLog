package com.example.mindlog.repository

import com.example.mindlog.data.GoalDao
import com.example.mindlog.data.GoalEntity

class GoalRepository(private val goalDao: GoalDao) {

    suspend fun insert(goal: GoalEntity) = goalDao.insert(goal)

    suspend fun update(goal: GoalEntity) = goalDao.update(goal)

    suspend fun delete(goal: GoalEntity) = goalDao.delete(goal)

    suspend fun getAllGoals(): List<GoalEntity> = goalDao.getAllGoals()

    suspend fun getCompletedGoalsCount(): Int = goalDao.getCompletedGoalsCount()
}