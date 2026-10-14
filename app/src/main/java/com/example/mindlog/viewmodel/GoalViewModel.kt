package com.example.mindlog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindlog.data.AppDatabase
import com.example.mindlog.data.GoalEntity
import com.example.mindlog.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel экрана целей.
 * Наследуемся от AndroidViewModel, чтобы получить доступ к Application
 * и через него — к базе данных.
 */
class GoalViewModel(application: Application) : AndroidViewModel(application) {

    // Репозиторий получаем из синглтона базы данных.
    private val repository: GoalRepository

    // StateFlow — поток, на который подписывается UI.
    // При изменении списка экран сам перерисуется.
    private val _goals = MutableStateFlow<List<GoalEntity>>(emptyList())
    val goals: StateFlow<List<GoalEntity>> = _goals

    init {
        val db = AppDatabase.getInstance(application)
        repository = GoalRepository(db.goalDao())
        loadGoals()
    }

    /**
     * Загружает все цели из базы в StateFlow.
     * Вызывается при старте и после каждого изменения.
     */
    fun loadGoals() {
        viewModelScope.launch {
            _goals.value = repository.getAllGoals()
        }
    }

    /**
     * Создать новую цель.
     * После вставки перезагружаем список, чтобы UI обновился.
     */
    fun addGoal(title: String, deadline: String) {
        viewModelScope.launch {
            repository.insert(GoalEntity(title = title, deadline = deadline))
            loadGoals()
        }
    }

    /**
     * Переключить статус выполнения.
     * Копируем сущность через copy и меняем только isCompleted.
     */
    fun toggleGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.update(goal.copy(isCompleted = !goal.isCompleted))
            loadGoals()
        }
    }

    /**
     * Удалить цель.
     */
    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.delete(goal)
            loadGoals()
        }
    }
}