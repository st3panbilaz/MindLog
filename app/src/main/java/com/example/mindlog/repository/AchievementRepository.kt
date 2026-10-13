package com.example.mindlog.repository

import com.example.mindlog.data.AchievementDao
import com.example.mindlog.data.AchievementEntity

class AchievementRepository(private val achievementDao: AchievementDao) {

    suspend fun insert(achievement: AchievementEntity) = achievementDao.insert(achievement)

    suspend fun update(achievement: AchievementEntity) = achievementDao.update(achievement)

    suspend fun getAllAchievements(): List<AchievementEntity> = achievementDao.getAllAchievements()

    suspend fun getCompletedGoalsCount(): Int = achievementDao.getCompletedGoalsCount()

    suspend fun getNotesCount(): Int = achievementDao.getNotesCount()

    suspend fun getMoodEntriesCount(): Int = achievementDao.getMoodEntriesCount()
}