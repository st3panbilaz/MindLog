package com.example.mindlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mood: String,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)