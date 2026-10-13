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

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GoalRepository

    private val _goals = MutableStateFlow<List<GoalEntity>>(emptyList())
    val goals: StateFlow<List<GoalEntity>> = _goals

    init {
        val db = AppDatabase.getInstance(application)
        repository = GoalRepository(db.goalDao())
        loadGoals()
    }

    fun loadGoals() {
        viewModelScope.launch {
            _goals.value = repository.getAllGoals()
        }
    }

    fun addGoal(title: String, deadline: String) {
        viewModelScope.launch {
            repository.insert(GoalEntity(title = title, deadline = deadline))
            loadGoals()
        }
    }

    fun toggleGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.update(goal.copy(isCompleted = !goal.isCompleted))
            loadGoals()
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.delete(goal)
            loadGoals()
        }
    }
}