package com.example.mindlog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindlog.viewmodel.AchievementViewModel

/**
 * Экран достижений. Показывает список из 6 достижений:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(viewModel: AchievementViewModel = viewModel()) {

    // Подписываемся на StateFlow из ViewModel — при изменении список сам обновится.
    val achievements by viewModel.achievements.collectAsState()

    // Каждый раз при входе на экран перепроверяем условия разблокировки.
    // LaunchedEffect(Unit) срабатывает один раз при появлении экрана.
    LaunchedEffect(Unit) {
        viewModel.checkAchievements()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Достижения") }) }
    ) { padding ->
        if (achievements.isEmpty()) {
            // Пока база инициализируется — показываем заглушку.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Загрузка достижений...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(achievements) { a ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        // Цвет карточки зависит от статуса: получено или нет.
                        colors = CardDefaults.cardColors(
                            containerColor = if (a.isUnlocked)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Эмодзи-иконка достижения.
                            Text(a.icon, style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(a.title, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(4.dp))
                                Text(a.description)
                            }

                            // Метка в правой части карточки.
                            Text(
                                if (a.isUnlocked) "✅ Получено" else "🔒 Не получено",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (a.isUnlocked) Color(0xFF2E7D32)
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}