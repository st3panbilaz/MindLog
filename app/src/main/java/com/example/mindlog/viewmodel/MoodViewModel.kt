package com.example.mindlog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindlog.data.AppDatabase
import com.example.mindlog.data.MoodEntity
import com.example.mindlog.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel дневника эмоций.
 * Хранит историю записей и добавляет новые.
 */
class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoodRepository

    // Список записей — реактивный, UI подписывается через collectAsState().
    private val _moods = MutableStateFlow<List<MoodEntity>>(emptyList())
    val moods: StateFlow<List<MoodEntity>> = _moods

    init {
        val db = AppDatabase.getInstance(application)
        repository = MoodRepository(db.moodDao())
        loadMoods()
    }

    /**
     * Загружает всю историю эмоций из базы.
     */
    fun loadMoods() {
        viewModelScope.launch {
            _moods.value = repository.getAllMoods()
        }
    }

    /**
     * Добавляет новую запись.
     * mood — код ("happy", "normal", "sad"), note — текст записи.
     */
    fun addMood(mood: String, note: String) {
        viewModelScope.launch {
            repository.insert(MoodEntity(mood = mood, note = note))
            loadMoods()
        }
    }
}