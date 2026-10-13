package com.example.mindlog.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Notes : Screen("notes", "Заметки", Icons.Filled.StickyNote2)
    object Goals : Screen("goals", "Цели", Icons.Filled.Flag)
    object Mood : Screen("mood", "Эмоции", Icons.Filled.EmojiEmotions)
    object Achievements : Screen("achievements", "Достижения", Icons.Filled.EmojiEvents)

    // Универсальный экран заметки: создание (id=-1) и редактирование (id>0)
    object NoteEdit : Screen("note_edit/{id}", "Заметка", Icons.Filled.StickyNote2) {
        fun createRoute(id: Int) = "note_edit/$id"
    }
}

val bottomNavItems = listOf(
    Screen.Notes,
    Screen.Goals,
    Screen.Mood,
    Screen.Achievements
)