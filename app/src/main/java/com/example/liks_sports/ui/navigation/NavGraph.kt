package com.example.liks_sports.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.liks_sports.data.RoutinesViewModel
import com.example.liks_sports.ui.screens.ExerciseTimerScreen
import com.example.liks_sports.ui.screens.RoutineDetailScreen
import com.example.liks_sports.ui.screens.RoutineListScreen

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
                },
            )
        }
        composable(
            route = Routes.ROUTINE_DETAIL,
            arguments = listOf(navArgument("routineId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routineId") ?: return@composable
            val routine = vm.routines.find { it.id == routineId } ?: return@composable

            RoutineDetailScreen(
                routine = routine,
                onUpdateRoutine = { updated ->
                    vm.updateRoutine(routineId, updated)
                },
                onBack = { navController.popBackStack() },
                onStart = { repeatCount ->
                    navController.navigate(Routes.timer(routineId, repeatCount))
                },
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
            val routine = vm.routines.find { it.id == routineId } ?: return@composable

            ExerciseTimerScreen(
                routine = routine,
                repeatCount = repeatCount,
                onFinish = { navController.popBackStack(Routes.ROUTINES, false) },
            )
        }
    }
}
