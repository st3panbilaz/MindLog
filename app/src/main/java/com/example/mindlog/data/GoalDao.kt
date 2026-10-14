package com.example.mindlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalDao {

    @Insert
    suspend fun insert(goal: GoalEntity)

    @Update
    suspend fun update(goal: GoalEntity)

    @Delete
    suspend fun delete(goal: GoalEntity)

    @Query("SELECT * FROM goals ORDER BY id DESC")
    suspend fun getAllGoals(): List<GoalEntity>

    // Используется для проверки достижений.
    @Query("SELECT COUNT(*) FROM goals WHERE isCompleted = 1")
    suspend fun getCompletedGoalsCount(): Int
}