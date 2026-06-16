package com.example.liks_sports.data

import android.app.Application
import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.liks_sports.R
import org.json.JSONArray
import org.json.JSONObject

class RoutinesViewModel(application: Application) : AndroidViewModel(application) {
    private var savedRoutines by mutableStateOf(listOf<Routine>())
    private var dismissedDefaults by mutableStateOf(setOf<String>())
    private var localeVersion by mutableIntStateOf(0)

    val routines by derivedStateOf {
        localeVersion
        (builtinRoutines() + savedRoutines).filter { it.id !in dismissedDefaults }
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
        if (isBuiltin(id)) return
        savedRoutines = savedRoutines.map { if (it.id == id) it.copy(name = name) else it }
        saveState()
    }

    fun updateRoutine(id: String, updated: Routine) {
        if (isBuiltin(id)) return
        savedRoutines = savedRoutines.map { if (it.id == id) updated else it }
        saveState()
    }

    fun deleteRoutine(id: String) {
        if (isBuiltin(id)) {
            dismissedDefaults = dismissedDefaults + id
            saveState()
        } else {
            savedRoutines = savedRoutines.filter { it.id != id }
            saveState()
        }
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
                        id = "football_hip_glute",
                        name = res.getString(R.string.football_hip_glute),
                        reps = 2,
                        exerciseDurationSeconds = 30,
                        restDurationSeconds = 30,
                        overrideDefaults = false,
                    ),
                    Exercise(
                        id = "football_lateral_lunge",
                        name = res.getString(R.string.football_lateral_lunge),
                        reps = 3,
                        exerciseDurationSeconds = 45,
                        restDurationSeconds = 45,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_nordic_curl",
                        name = res.getString(R.string.football_nordic_curl),
                        reps = 3,
                        exerciseDurationSeconds = 60,
                        restDurationSeconds = 60,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_ankle_hold",
                        name = res.getString(R.string.football_ankle_hold),
                        reps = 2,
                        exerciseDurationSeconds = 30,
                        restDurationSeconds = 20,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_lateral_shuffle",
                        name = res.getString(R.string.football_lateral_shuffle),
                        reps = 4,
                        exerciseDurationSeconds = 30,
                        restDurationSeconds = 30,
                        overrideDefaults = false,
                    ),
                    Exercise(
                        id = "football_sprint",
                        name = res.getString(R.string.football_sprint),
                        reps = 4,
                        exerciseDurationSeconds = 30,
                        restDurationSeconds = 45,
                        overrideDefaults = true,
                    ),
                    Exercise(
                        id = "football_calf_raise",
                        name = res.getString(R.string.football_calf_raise),
                        reps = 2,
                        exerciseDurationSeconds = 15,
                        restDurationSeconds = 10,
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
            .putString(KEY_ROUTINES, toJson(savedRoutines))
            .putString(KEY_DISMISSED, toJsonSet(dismissedDefaults))
            .apply()
    }

    private fun loadState() {
        val prefs = getApplication<Application>()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        try {
            val json = prefs.getString(KEY_ROUTINES, null)
            if (json != null) {
                savedRoutines = fromJson(json)
            }
        } catch (_: Exception) {
            savedRoutines = emptyList()
        }
        try {
            val dismissedJson = prefs.getString(KEY_DISMISSED, null)
            if (dismissedJson != null) {
                dismissedDefaults = fromJsonSet(dismissedJson)
            }
        } catch (_: Exception) {
            dismissedDefaults = emptySet()
        }
    }

    private fun toJson(routines: List<Routine>): String {
        val arr = JSONArray()
        for (r in routines) {
            val exercises = JSONArray()
            for (e in r.exercises) {
                exercises.put(
                    JSONObject().apply {
                        put("id", e.id)
                        put("name", e.name)
                        put("reps", e.reps)
                        put("exerciseDurationSeconds", e.exerciseDurationSeconds)
                        put("restDurationSeconds", e.restDurationSeconds)
                        put("overrideDefaults", e.overrideDefaults)
                    }
                )
            }
            arr.put(
                JSONObject().apply {
                    put("id", r.id)
                    put("name", r.name)
                    put("exercises", exercises)
                }
            )
        }
        return JSONObject().apply {
            put("format_version", FORMAT_VERSION)
            put("routines", arr)
        }.toString()
    }

    private fun fromJson(json: String): List<Routine> {
        val arr = try {
            val root = JSONObject(json)
            root.getJSONArray("routines")
        } catch (_: Exception) {
            JSONArray(json)
        }
        return (0 until arr.length()).map { i ->
            val obj = arr.getJSONObject(i)
            val exercisesArr = obj.getJSONArray("exercises")
            val exercises = (0 until exercisesArr.length()).map { j ->
                val e = exercisesArr.getJSONObject(j)
                Exercise(
                    id = e.getString("id"),
                    name = e.getString("name"),
                    reps = e.getInt("reps"),
                    exerciseDurationSeconds = e.getInt("exerciseDurationSeconds"),
                    restDurationSeconds = e.getInt("restDurationSeconds"),
                    overrideDefaults = e.getBoolean("overrideDefaults"),
                )
            }
            Routine(
                id = obj.getString("id"),
                name = obj.getString("name"),
                exercises = exercises,
            )
        }
    }

    private fun toJsonSet(set: Set<String>): String {
        val arr = JSONArray()
        for (s in set) arr.put(s)
        return arr.toString()
    }

    private fun fromJsonSet(json: String): Set<String> {
        val arr = JSONArray(json)
        return (0 until arr.length()).map { arr.getString(it) }.toSet()
    }

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val KEY_ROUTINES = "routines"
        private const val KEY_DISMISSED = "dismissed_defaults"
        private const val BUILTIN_PARKOUR_ID = "builtin_parkour"
        private const val BUILTIN_FOOTBALL_ID = "builtin_football"
        private const val FORMAT_VERSION = 1
    }
}
