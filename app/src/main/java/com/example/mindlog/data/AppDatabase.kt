package com.example.mindlog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Главный класс базы данных. Связывает сущности и DAO.
 * Синглтон — создаётся один раз при первом обращении.
 */
@Database(
    entities = [
        NoteEntity::class,
        GoalEntity::class,
        MoodEntity::class,
        AchievementEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun goalDao(): GoalDao
    abstract fun moodDao(): MoodDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mindlog_database"
                )
                    // Чтобы не писать миграции на этапе разработки.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}