package com.example.liks_sports.data

import android.app.Application
import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.liks_sports.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutinesViewModel(application: Application) : AndroidViewModel(application) {
    private var savedRoutines by mutableStateOf(listOf<Routine>())
    private var dismissedDefaults by mutableStateOf(setOf<String>())
    private var localeVersion by mutableIntStateOf(0)

    private val workoutHistoryStore by lazy { WorkoutHistoryStore(getApplication()) }
    private var workoutSessions by mutableStateOf<List<WorkoutSession>>(emptyList())

    val routines by derivedStateOf {
        localeVersion
        val overrides = savedRoutines.associateBy { it.id }
        val builtins = builtinRoutines().map { overrides[it.id] ?: it }
        val customs = savedRoutines.filter { !isBuiltin(it.id) }
        (builtins + customs).filter { it.id !in dismissedDefaults }
    }

    val hasDismissedDefaults by derivedStateOf {
        localeVersion
        dismissedDefaults.isNotEmpty()
    }

    val workoutStats by derivedStateOf {
        localeVersion
        WorkoutStatsCalculator.compute(workoutSessions)
    }

    val lastSessionForRoutine by derivedStateOf {
        localeVersion
        lastSessionByRoutine(workoutSessions)
    }

    val routineDoneCounts by derivedStateOf {
        localeVersion
        countByRoutine(workoutSessions)
    }

    fun recordSession(session: WorkoutSession) {
        workoutHistoryStore.addSession(session)
        workoutSessions = workoutHistoryStore.getSessions()
    }

    val localLlmEngine: LocalLlmEngine by lazy { LocalLlmEngine(getApplication()) }

    val localModelManager: LocalModelManager by lazy { LocalModelManager(getApplication()) }

    private val settingsStore by lazy { SettingsStore(getApplication()) }

    var modelPresent by mutableStateOf(localModelManager.isModelPresent())
        private set

    var downloadState by mutableStateOf<DownloadState>(DownloadState.Idle)
        private set
    private var downloadJob: Job? = null

    fun startDownload() {
        if (downloadState is DownloadState.Downloading) return
        downloadState = DownloadState.Downloading(0)
        downloadJob = viewModelScope.launch {
            try {
                val path = localModelManager.download { pct ->
                    downloadState = DownloadState.Downloading(pct)
                }
                settingsStore.localModelPath = path
                settingsStore.localModelId = SettingsStore.DEFAULT_LOCAL_MODEL_ID
                modelPresent = true
                localLlmEngine.close()
                downloadState = DownloadState.Idle
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                downloadState = DownloadState.Error(e.message ?: "error")
            }
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
        downloadJob = null
        downloadState = DownloadState.Idle
    }

    fun deleteModel() {
        if (downloadState is DownloadState.Downloading) cancelDownload()
        localModelManager.deleteModel()
        settingsStore.localModelPath = ""
        modelPresent = false
        localLlmEngine.close()
    }

    fun clearDownloadError() {
        if (downloadState is DownloadState.Error) downloadState = DownloadState.Idle
    }

    init {
        loadState()
    }

    fun onLocaleChanged() {
        localeVersion++
    }

    fun addRoutine(name: String): Routine {
        val routine = Routine(name = name)
        savedRoutines = savedRoutines + routine
        saveState()
        return routine
    }

    fun renameRoutine(id: String, name: String) {
        if (savedRoutines.any { it.id == id }) {
            savedRoutines = savedRoutines.map { if (it.id == id) it.copy(name = name) else it }
        } else if (isBuiltin(id)) {
            val builtin = builtinRoutines().first { it.id == id }
            savedRoutines = savedRoutines + builtin.copy(name = name)
        }
        saveState()
    }

    fun updateRoutine(id: String, updated: Routine) {
        savedRoutines = if (savedRoutines.any { it.id == id }) {
            savedRoutines.map { if (it.id == id) updated else it }
        } else {
            savedRoutines + updated
        }
        saveState()
    }

    fun deleteRoutine(id: String) {
        savedRoutines = savedRoutines.filter { it.id != id }
        if (isBuiltin(id)) {
            dismissedDefaults = dismissedDefaults + id
        }
        saveState()
    }

    fun undoDeleteRoutine(routine: Routine) {
        if (isBuiltin(routine.id)) {
            dismissedDefaults = dismissedDefaults - routine.id
        }
        savedRoutines = if (savedRoutines.any { it.id == routine.id }) {
            savedRoutines.map { if (it.id == routine.id) routine else it }
        } else {
            savedRoutines + routine
        }
        saveState()
    }

    fun restoreDefaults() {
        dismissedDefaults = emptySet()
        saveState()
    }

    override fun onCleared() {
        localLlmEngine.close()
        super.onCleared()
    }

    private fun isBuiltin(id: String) = id == BUILTIN_PARKOUR_ID || id == BUILTIN_FOOTBALL_ID

    private fun builtinRoutines(): List<Routine> {
        val res = getApplication<Application>().resources
        return listOf(
            Routine(
                id = BUILTIN_PARKOUR_ID,
                name = res.getString(R.string.parkour_name),
                exercises = listOf(
                    Exercise(
                        id = "parkour_trotar_suave",
                        name = res.getString(R.string.parkour_rotar_suave),
                        reps = 1,
                        exerciseDurationSeconds = 120,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_saltar_cuerda",
                        name = res.getString(R.string.parkour_saltar_cuerda),
                        reps = 1,
                        exerciseDurationSeconds = 30,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_sentadillas",
                        name = res.getString(R.string.parkour_sentadillas),
                        reps = 1,
                        exerciseDurationSeconds = 40,
                        restDurationSeconds = 25,
                        overrideDefaults = false,
                    ),
                    Exercise(
                        id = "parkour_plancha",
                        name = res.getString(R.string.parkour_plancha),
                        reps = 1,
                        exerciseDurationSeconds = 20,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_un_pie_1",
                        name = res.getString(R.string.parkour_un_pie_1),
                        reps = 1,
                        exerciseDurationSeconds = 15,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_un_pie_2",
                        name = res.getString(R.string.parkour_un_pie_2),
                        reps = 1,
                        exerciseDurationSeconds = 15,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_elevaciones_talones",
                        name = res.getString(R.string.parkour_elevaciones_talones),
                        reps = 1,
                        exerciseDurationSeconds = 40,
                        restDurationSeconds = 25,
                        overrideDefaults = false,
                    ),
                    Exercise(
                        id = "parkour_saltos_suaves",
                        name = res.getString(R.string.parkour_saltos_suaves),
                        reps = 1,
                        exerciseDurationSeconds = 40,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_tecnica",
                        name = res.getString(R.string.parkour_tecnica),
                        reps = 1,
                        exerciseDurationSeconds = 30,
                        restDurationSeconds = 25,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "parkour_caidas_suaves",
                        name = res.getString(R.string.parkour_caidas_suaves),
                        reps = 1,
                        exerciseDurationSeconds = 40,
                        restDurationSeconds = 25,
                        overrideDefaults = false,
                    ),
                    Exercise(
                        id = "parkour_saltos_precision",
                        name = res.getString(R.string.parkour_saltos_precision),
                        reps = 1,
                        exerciseDurationSeconds = 40,
                        restDurationSeconds = 25,
                        overrideDefaults = false,
                    ),
                ),
            ),
            Routine(
                id = BUILTIN_FOOTBALL_ID,
                name = res.getString(R.string.football_name),
                exercises = listOf(
                    Exercise(
                        id = "football_rote_suave",
                        name = res.getString(R.string.football_rote_suave),
                        reps = 1,
                        exerciseDurationSeconds = 45,
                        restDurationSeconds = 30,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_zig_zag_platos",
                        name = res.getString(R.string.football_zig_zag_platos),
                        reps = 1,
                        exerciseDurationSeconds = 45,
                        restDurationSeconds = 30,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_skipping_platos",
                        name = res.getString(R.string.football_skipping_platos),
                        reps = 1,
                        exerciseDurationSeconds = 45,
                        restDurationSeconds = 30,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_metralletas",
                        name = res.getString(R.string.football_metralletas),
                        reps = 1,
                        exerciseDurationSeconds = 45,
                        restDurationSeconds = 30,
                        overrideDefaults = true,
                    ),
                ),
            ),
        )
    }

    private fun saveState() {
        val prefs = getApplication<Application>()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_ROUTINES, RoutinesJson.toJson(savedRoutines))
            .putString(KEY_DISMISSED, RoutinesJson.toJsonSet(dismissedDefaults))
            .apply()
    }

    private fun loadState() {
        viewModelScope.launch {
            val (saved, dismissed, sessions) = withContext(Dispatchers.IO) {
                val prefs = getApplication<Application>()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val savedRoutines = try {
                    prefs.getString(KEY_ROUTINES, null)?.let { RoutinesJson.fromJson(it) }
                        ?: emptyList()
                } catch (_: Exception) {
                    emptyList()
                }
                val dismissed = try {
                    prefs.getString(KEY_DISMISSED, null)?.let { RoutinesJson.fromJsonSet(it) }
                        ?: emptySet()
                } catch (_: Exception) {
                    emptySet()
                }
                val sessions = try {
                    workoutHistoryStore.getSessions()
                } catch (_: Exception) {
                    emptyList()
                }
                Triple(savedRoutines, dismissed, sessions)
            }
            savedRoutines = saved
            dismissedDefaults = dismissed
            workoutSessions = sessions
        }
    }


    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val KEY_ROUTINES = "routines"
        private const val KEY_DISMISSED = "dismissed_defaults"
        private const val BUILTIN_PARKOUR_ID = "builtin_parkour"
        private const val BUILTIN_FOOTBALL_ID = "builtin_football"
    }
}
