package com.example.mindlog.repository

import com.example.mindlog.data.AchievementDao
import com.example.mindlog.data.AchievementEntity

/**
 * Репозиторий достижений. Прослойка между ViewModel и DAO.
 * Нужен, чтобы ViewModel не зависела напрямую от Room —
 * если завтра захочу хранить достижения где-то ещё, менять придётся только репозиторий.
 */
class AchievementRepository(private val achievementDao: AchievementDao) {

    /**
     * Добавляет новое достижение в базу.
     * Вызывается один раз — при первой инициализации стартового набора.
     */
    suspend fun insert(achievement: AchievementEntity) = achievementDao.insert(achievement)

    /**
     * Обновляет достижение — например, когда разблокировали новое.
     */
    suspend fun update(achievement: AchievementEntity) = achievementDao.update(achievement)

    /**
     * Возвращает весь список достижений для отображения на экране.
     * Сортируются по id, чтобы порядок был стабильным.
     */
    suspend fun getAllAchievements(): List<AchievementEntity> = achievementDao.getAllAchievements()

    /**
     * Три метода ниже возвращают счётчики из разных таблиц.
     * Они нужны, чтобы проверить условия разблокировки достижений.
     */
    suspend fun getCompletedGoalsCount(): Int = achievementDao.getCompletedGoalsCount()

    suspend fun getNotesCount(): Int = achievementDao.getNotesCount()

    suspend fun getMoodEntriesCount(): Int = achievementDao.getMoodEntriesCount()
}