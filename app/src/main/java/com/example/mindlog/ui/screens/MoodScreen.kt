package com.example.mindlog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindlog.viewmodel.MoodViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Экран дневника эмоций.
 * Вверху — три кнопки с эмодзи, снизу — история записей.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen(viewModel: MoodViewModel = viewModel()) {

    val moods by viewModel.moods.collectAsState()

    // Необязательная заметка, которую можно добавить к эмоции.
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Дневник эмоций") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Как ты себя чувствуешь сегодня?",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            // Три кнопки для выбора настроения.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodButton(emoji = "😊", label = "Счастлив") {
                    viewModel.addMood("happy", note.ifBlank { "Без заметки" })
                    note = ""
                }
                MoodButton(emoji = "😐", label = "Нормально") {
                    viewModel.addMood("normal", note.ifBlank { "Без заметки" })
                    note = ""
                }
                MoodButton(emoji = "😢", label = "Грустно") {
                    viewModel.addMood("sad", note.ifBlank { "Без заметки" })
                    note = ""
                }
            }

            Spacer(Modifier.height(12.dp))

            // Поле для необязательной записи.
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Запись в дневник (необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Text("История эмоций", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            if (moods.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Записей пока нет")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moods) { mood ->
                        // Преобразуем код настроения в читаемый текст с эмодзи.
                        val emoji = when (mood.mood) {
                            "happy" -> "😊 Счастлив"
                            "normal" -> "😐 Нормально"
                            "sad" -> "😢 Грустно"
                            else -> mood.mood
                        }
                        // Форматируем дату в привычный вид.
                        val date = SimpleDateFormat(
                            "dd.MM.yyyy HH:mm",
                            Locale.getDefault()
                        ).format(Date(mood.timestamp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(emoji, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(4.dp))
                                Text(mood.note)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    date,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Кнопка эмоции: большая кнопка с эмодзи и подписью снизу.
 */
@Composable
fun MoodButton(emoji: String, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(emoji, style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}