package com.example.mindlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность заметки для базы данных Room.
 * Хранит заголовок, текст, время создания и список задач в виде строки.
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    // Чек-лист хранится как строка, где каждая строка — отдельный пункт.
    // Формат: "[x] пункт" — выполнен, "[ ] пункт" — не выполнен.
    val checklist: String = "",
    // Время создания заметки в миллисекундах. По умолчанию — текущий момент.
    val timestamp: Long = System.currentTimeMillis()
)