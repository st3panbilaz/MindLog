package com.example.mindlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * DAO для заметок. Room сам сгенерирует реализацию.
 * Все методы suspend — работают в корутинах, чтобы не блокировать UI.
 */
@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    // Сортируем по времени, чтобы свежие заметки были сверху.
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    // Нужен для экрана редактирования.
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Delete
    suspend fun delete(note: NoteEntity)
}