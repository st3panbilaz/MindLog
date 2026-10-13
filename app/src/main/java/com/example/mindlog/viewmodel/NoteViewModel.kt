package com.example.mindlog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindlog.data.AppDatabase
import com.example.mindlog.data.NoteEntity
import com.example.mindlog.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes

    init {
        val db = AppDatabase.getInstance(application)
        repository = NoteRepository(db.noteDao())
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repository.getAllNotes()
        }
    }

    // Создание новой заметки
    fun addNote(title: String, content: String, checklist: String) {
        viewModelScope.launch {
            repository.insert(
                NoteEntity(
                    title = title,
                    content = content,
                    checklist = checklist
                )
            )
            loadNotes()
        }
    }

    
    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.update(note)
            loadNotes()
        }
    }

    // Получение заметки по id (для экрана редактирования)
    suspend fun getNoteById(id: Int): NoteEntity? {
        return repository.getNoteById(id)
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.delete(note)
            loadNotes()
        }
    }
}