package com.example.liks_sports.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.liks_sports.data.RoutinesViewModel
import com.example.liks_sports.data.SettingsStore
import com.example.liks_sports.ui.screens.ExerciseTimerScreen
import com.example.liks_sports.ui.screens.RoutineDetailScreen
import com.example.liks_sports.ui.screens.RoutineListScreen
import com.example.liks_sports.ui.screens.SettingsDialog

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
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(LocalConfiguration.current) {
        vm.onLocaleChanged()
    }

    if (showSettings) {
        SettingsDialog(
            store = settingsStore,
            onDismiss = { showSettings = false },
        )
    }

    NavHost(navController = navController, startDestination = Routes.ROUTINES) {
        composable(Routes.ROUTINES) {
            RoutineListScreen(
                routines = vm.routines,
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
                    vm.deleteRoutine(routineId)
                    chatHistoryStore.clear(routineId)
                },
                onOpenSettings = { showSettings = true },
            )
        }
        composable(
            route = Routes.ROUTINE_DETAIL,
            arguments = listOf(navArgument("routineId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routineId") ?: return@composable
            val routine = vm.routines.find { it.id == routineId }

            if (routine == null) {
                androidx.compose.material3.Text(
                    text = stringResource(R.string.routine_not_found),
                    modifier = androidx.compose.ui.Modifier
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
                onFinish = { navController.popBackStack(Routes.ROUTINES, false) },
                onOpenSettings = { showSettings = true },
            )
        }
    }
}
