package com.example.mindlog.repository

import com.example.mindlog.data.MoodDao
import com.example.mindlog.data.MoodEntity

class MoodRepository(private val moodDao: MoodDao) {

    suspend fun insert(mood: MoodEntity) = moodDao.insert(mood)

    suspend fun getAllMoods(): List<MoodEntity> = moodDao.getAllMoods()

    suspend fun getMoodEntriesCount(): Int = moodDao.getMoodEntriesCount()
}
