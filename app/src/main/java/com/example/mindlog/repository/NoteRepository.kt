package com.example.mindlog.repository

import com.example.mindlog.data.NoteDao
import com.example.mindlog.data.NoteEntity

/**
 * Репозиторий — прослойка между ViewModel и DAO.
 * Нужен, чтобы ViewModel не знала, откуда именно берутся данные
 * (сейчас это Room, потом может быть сеть или кэш).
 */
class NoteRepository(private val noteDao: NoteDao) {

    suspend fun insert(note: NoteEntity) = noteDao.insert(note)

    suspend fun update(note: NoteEntity) = noteDao.update(note)

    suspend fun getAllNotes(): List<NoteEntity> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Int): NoteEntity? = noteDao.getNoteById(id)

    suspend fun delete(note: NoteEntity) = noteDao.delete(note)
}