package com.example.mindlog.repository

import com.example.mindlog.data.MoodDao
import com.example.mindlog.data.MoodEntity

/**
 * Репозиторий эмоций. Простой, потому что и логика простая:
 * добавить запись и получить историю.
 */
class MoodRepository(private val moodDao: MoodDao) {

    /**
     * Добавляет новую запись в дневник эмоций.
     */
    suspend fun insert(mood: MoodEntity) = moodDao.insert(mood)

    /**
     * Возвращает всю историю эмоций.
     */
    suspend fun getAllMoods(): List<MoodEntity> = moodDao.getAllMoods()

    /**
     * Счётчик записей — для достижений «Эмоциональный» и «Гуру эмоций».
     */
    suspend fun getMoodEntriesCount(): Int = moodDao.getMoodEntriesCount()
}