package com.example.liks_sports.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.liks_sports.R
import com.example.liks_sports.data.ChatHistoryStore
import com.example.liks_sports.data.ChatMessage
import com.example.liks_sports.data.RoutinesViewModel
import com.example.liks_sports.data.SettingsStore
import com.example.liks_sports.data.WorkoutHistoryStore
import com.example.liks_sports.data.WorkoutSession
import com.example.liks_sports.data.applyAppLanguage
import com.example.liks_sports.ui.screens.ExerciseTimerScreen
import com.example.liks_sports.ui.screens.GeneralSettingsDialog
import com.example.liks_sports.ui.screens.RoutineDetailScreen
import com.example.liks_sports.ui.screens.RoutineListScreen
import com.example.liks_sports.ui.screens.SettingsDialog
import com.example.liks_sports.ui.theme.LikssportsTheme

object Routes {
    const val ROUTINES = "routines"
    const val ROUTINE_DETAIL = "routines/{routineId}"
    const val TIMER = "routines/{routineId}/timer?repeatCount={repeatCount}"

    fun routineDetail(routineId: String) = "routines/$routineId"
    fun timer(routineId: String, repeatCount: Int = 1) = "routines/$routineId/timer?repeatCount=$repeatCount"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val vm: RoutinesViewModel = viewModel()
    val context = LocalContext.current
    val settingsStore = remember { SettingsStore(context) }
    val chatHistoryStore = remember { ChatHistoryStore(context) }
    val deletedChatSnapshot = remember { mutableStateMapOf<String, List<ChatMessage>>() }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    var showGeneralSettings by rememberSaveable { mutableStateOf(false) }
    var themePalette by remember { mutableStateOf(settingsStore.themePalette) }

    val currentLocale = LocalConfiguration.current.locales[0]
    LaunchedEffect(currentLocale) {
        vm.onLocaleChanged()
    }

    LikssportsTheme(palette = themePalette) {
        Surface(modifier = Modifier.fillMaxSize()) {
            if (showSettings) {
                SettingsDialog(
                    store = settingsStore,
                    hasDismissedDefaults = vm.hasDismissedDefaults,
                    modelPresent = vm.modelPresent,
                    downloadState = vm.downloadState,
                    onRestoreDefaults = { vm.restoreDefaults() },
                    onStartDownload = { vm.startDownload() },
                    onCancelDownload = { vm.cancelDownload() },
                    onDeleteModel = { vm.deleteModel() },
                    onDismiss = { showSettings = false },
                )
            }

            if (showGeneralSettings) {
                GeneralSettingsDialog(
                    currentPalette = themePalette,
                    currentLanguage = settingsStore.language,
                    onSave = { palette, language ->
                        settingsStore.themePalette = palette
                        settingsStore.language = language
                        themePalette = palette
                        applyAppLanguage(language)
                        showGeneralSettings = false
                    },
                    onDismiss = { showGeneralSettings = false },
                )
            }

            NavHost(navController = navController, startDestination = Routes.ROUTINES) {
                composable(Routes.ROUTINES) {
                    RoutineListScreen(
                        routines = vm.routines,
                        workoutStats = vm.workoutStats,
                        lastSessionForRoutine = vm.lastSessionForRoutine,
                        routineDoneCounts = vm.routineDoneCounts,
                        onCreateRoutine = { name ->
                            val routine = vm.addRoutine(name)
                            navController.navigate(Routes.routineDetail(routine.id))
                        },
                        onRenameRoutine = { id, name ->
                            vm.renameRoutine(id, name)
                        },
                        onRoutineClick = { routineId ->
                            navController.navigate(Routes.routineDetail(routineId))
                        },
                        onDeleteRoutine = { routineId ->
                            deletedChatSnapshot[routineId] = chatHistoryStore.getMessages(routineId)
                            vm.deleteRoutine(routineId)
                            chatHistoryStore.clear(routineId)
                        },
                        onUndoDeleteRoutine = { routine ->
                            vm.undoDeleteRoutine(routine)
                            val msgs = deletedChatSnapshot.remove(routine.id)
                            if (msgs != null && msgs.isNotEmpty()) {
                                chatHistoryStore.addMessages(routine.id, msgs)
                            }
                        },
                        onOpenGeneralSettings = { showGeneralSettings = true },
                    )
                }
                composable(
                    route = Routes.ROUTINE_DETAIL,
                    arguments = listOf(navArgument("routineId") { type = NavType.StringType }),
                ) { backStackEntry ->
                    val routineId = backStackEntry.arguments?.getString("routineId") ?: return@composable
                    val routine = vm.routines.find { it.id == routineId }

                    if (routine == null) {
                        Text(
                            text = stringResource(R.string.routine_not_found),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        )
                        return@composable
                    }

                    RoutineDetailScreen(
                        routine = routine,
                        onUpdateRoutine = { updated ->
                            vm.updateRoutine(routineId, updated)
                        },
                        onBack = { navController.popBackStack() },
                        onStart = { repeatCount ->
                            navController.navigate(Routes.timer(routineId, repeatCount))
                        },
                        settingsStore = settingsStore,
                        chatHistoryStore = chatHistoryStore,
                        localEngine = vm.localLlmEngine,
                        onOpenSettings = { showSettings = true },
                    )
                }
                composable(
                    route = Routes.TIMER,
                    arguments = listOf(
                        navArgument("routineId") { type = NavType.StringType },
                        navArgument("repeatCount") { type = NavType.IntType; defaultValue = 1 },
                    ),
                ) { backStackEntry ->
                    val routineId = backStackEntry.arguments?.getString("routineId") ?: return@composable
                    val repeatCount = backStackEntry.arguments?.getInt("repeatCount") ?: 1
                    val timerRoutine = vm.routines.find { it.id == routineId }

                    if (timerRoutine == null) {
                        Text(
                            text = stringResource(R.string.routine_not_found),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        )
                        return@composable
                    }

                    ExerciseTimerScreen(
                        routine = timerRoutine,
                        repeatCount = repeatCount,
                        onFinish = { completed, elapsed ->
                            if (completed || elapsed > 0) {
                                vm.recordSession(
                                    WorkoutSession(
                                        routineId = timerRoutine.id,
                                        routineName = timerRoutine.name,
                                        dateEpochDay = WorkoutHistoryStore.todayEpochDay(),
                                        durationSeconds = elapsed,
                                        exerciseCount = timerRoutine.exercises.size,
                                        rounds = repeatCount,
                                        completed = completed,
                                    )
                                )
                            }
                            navController.popBackStack(Routes.ROUTINES, false)
                        },
                    )
                }
            }
        }
    }
}
