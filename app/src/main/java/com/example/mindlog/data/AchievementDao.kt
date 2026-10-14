package com.example.mindlog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AchievementDao {

    @Insert
    suspend fun insert(achievement: AchievementEntity)

    @Update
    suspend fun update(achievement: AchievementEntity)

    @Query("SELECT * FROM achievements ORDER BY id")
    suspend fun getAllAchievements(): List<AchievementEntity>

    // Счётчики для проверки условий разблокировки.
    @Query("SELECT COUNT(*) FROM goals WHERE isCompleted = 1")
    suspend fun getCompletedGoalsCount(): Int

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNotesCount(): Int

    @Query("SELECT COUNT(*) FROM mood_entries")
    suspend fun getMoodEntriesCount(): Int
}