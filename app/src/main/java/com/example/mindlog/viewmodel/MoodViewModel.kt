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

class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoodRepository

    private val _moods = MutableStateFlow<List<MoodEntity>>(emptyList())
    val moods: StateFlow<List<MoodEntity>> = _moods

    init {
        val db = AppDatabase.getInstance(application)
        repository = MoodRepository(db.moodDao())
        loadMoods()
    }

    fun loadMoods() {
        viewModelScope.launch {
            _moods.value = repository.getAllMoods()
        }
    }

    fun addMood(mood: String, note: String) {
        viewModelScope.launch {
            repository.insert(MoodEntity(mood = mood, note = note))
            loadMoods()
        }
    }
}