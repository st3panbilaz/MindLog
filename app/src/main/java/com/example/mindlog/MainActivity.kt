package com.example.mindlog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mindlog.ui.navigation.Screen
import com.example.mindlog.ui.navigation.bottomNavItems
import com.example.mindlog.ui.screens.AchievementsScreen
import com.example.mindlog.ui.screens.GoalsScreen
import com.example.mindlog.ui.screens.MoodScreen
import com.example.mindlog.ui.screens.NoteEditScreen
import com.example.mindlog.ui.screens.NotesScreen
import com.example.mindlog.ui.theme.MindLogTheme
import com.example.mindlog.viewmodel.NoteViewModel

/**
 * Единственная Activity в приложении.
 * Всё остальное — Compose-экраны и навигация между ними.
 */
class MainActivity : ComponentActivity() {

    // Контракт для запроса разрешения на уведомления (Android 13+).
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Канал уведомлений создаём сразу — до первого показа уведомления.
        NotificationHelper.createChannel(this)

        // На Android 13+ нужно явно спросить разрешение на уведомления.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            // Оборачиваем всё в нашу тему приложения.
            MindLogTheme {
                // Контроллер навигации — управляет переходами между экранами.
                val navController = rememberNavController()

                // NoteViewModel создаётся на уровне Activity,
                // чтобы данные сохранялись при переходах между экранами.
                val noteViewModel: NoteViewModel = viewModel()

                Scaffold(
                    bottomBar = {
                        // Получаем текущий маршрут, чтобы знать, какой пункт подсветить.
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        // На экране редактирования заметки нижнюю панель скрываем —
                        // там она не нужна и занимает место.
                        if (currentRoute?.startsWith("note_edit") != true) {
                            NavigationBar {
                                bottomNavItems.forEach { screen ->
                                    NavigationBarItem(
                                        icon = {
                                            Icon(screen.icon, contentDescription = screen.label)
                                        },
                                        label = { Text(screen.label) },
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Сохраняем состояние экрана при возврате.
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                // Не создаём дубликат экрана, если уже открыт.
                                                launchSingleTop = true
                                                // Восстанавливаем состояние при возврате.
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    // NavHost — «сцена», где переключаются экраны.
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Notes.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Экран списка заметок.
                        composable(Screen.Notes.route) {
                            NotesScreen(
                                viewModel = noteViewModel,
                                // Создание: id = -1.
                                onAddNoteClick = {
                                    navController.navigate(Screen.NoteEdit.createRoute(-1))
                                },
                                // Редактирование: id существующей заметки.
                                onNoteClick = { id ->
                                    navController.navigate(Screen.NoteEdit.createRoute(id))
                                }
                            )
                        }

                        // Экран целей.
                        composable(Screen.Goals.route) { GoalsScreen() }

                        // Экран эмоций.
                        composable(Screen.Mood.route) { MoodScreen() }

                        // Экран достижений.
                        composable(Screen.Achievements.route) { AchievementsScreen() }

                        // Универсальный экран заметки (создание/редактирование).
                        composable(
                            route = Screen.NoteEdit.route,
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: -1
                            NoteEditScreen(
                                noteId = id,
                                viewModel = noteViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}