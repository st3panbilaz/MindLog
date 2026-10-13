package com.example.mindlog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoodDao {

    @Insert
    suspend fun insert(mood: MoodEntity)

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    suspend fun getAllMoods(): List<MoodEntity>

    @Query("SELECT COUNT(*) FROM mood_entries")
    suspend fun getMoodEntriesCount(): Int
}