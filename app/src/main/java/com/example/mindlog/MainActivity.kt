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

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createChannel(this)

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
            MindLogTheme {
                val navController = rememberNavController()
                val noteViewModel: NoteViewModel = viewModel()

                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        // Скрываем нижнее меню на экране заметки
                        if (currentRoute?.startsWith("note_edit") != true) {
                            NavigationBar {
                                bottomNavItems.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                                        label = { Text(screen.label) },
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Notes.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Notes.route) {
                            NotesScreen(
                                viewModel = noteViewModel,
                                onAddNoteClick = {
                                    navController.navigate(Screen.NoteEdit.createRoute(-1))
                                },
                                onNoteClick = { id ->
                                    navController.navigate(Screen.NoteEdit.createRoute(id))
                                }
                            )
                        }
                        composable(Screen.Goals.route) { GoalsScreen() }
                        composable(Screen.Mood.route) { MoodScreen() }
                        composable(Screen.Achievements.route) { AchievementsScreen() }

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