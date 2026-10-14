package com.example.mindlog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO для дневника эмоций.
 * Здесь только две операции: добавить запись и получить все записи.
 * Удаление эмоций я не делаю — история должна сохраняться.
 */
@Dao
interface MoodDao {

    /**
     * Добавляет новую запись в дневник.
     */
    @Insert
    suspend fun insert(mood: MoodEntity)

    /**
     * Возвращает все записи, отсортированные по времени.
     * Новые — сверху, чтобы сразу видеть последнее настроение.
     */
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    suspend fun getAllMoods(): List<MoodEntity>

    /**
     * Считает общее количество записей.
     */
    @Query("SELECT COUNT(*) FROM mood_entries")
    suspend fun getMoodEntriesCount(): Int
}