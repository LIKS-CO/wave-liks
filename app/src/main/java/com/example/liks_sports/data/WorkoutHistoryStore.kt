package com.example.liks_sports.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.ZoneId

data class WorkoutSession(
    val routineId: String,
    val routineName: String,
    val dateEpochDay: Long,
    val durationSeconds: Int,
    val exerciseCount: Int,
    val rounds: Int,
    val completed: Boolean,
)

class WorkoutHistoryStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val lock = Any()

    fun getSessions(): List<WorkoutSession> {
        val json = prefs.getString(KEY, null) ?: return emptyList()
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { i ->
                val obj = arr.getJSONObject(i)
                WorkoutSession(
                    routineId = obj.getString("routineId"),
                    routineName = obj.getString("routineName"),
                    dateEpochDay = obj.getLong("dateEpochDay"),
                    durationSeconds = obj.getInt("durationSeconds"),
                    exerciseCount = obj.getInt("exerciseCount"),
                    rounds = obj.getInt("rounds"),
                    completed = obj.getBoolean("completed"),
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun addSession(session: WorkoutSession) {
        synchronized(lock) {
            val sessions = getSessions().toMutableList()
            sessions.add(session)
            write(sessions)
        }
    }

    fun clearAll() {
        prefs.edit().remove(KEY).apply()
    }

    private fun write(sessions: List<WorkoutSession>) {
        val arr = JSONArray()
        for (s in sessions) {
            arr.put(JSONObject().apply {
                put("routineId", s.routineId)
                put("routineName", s.routineName)
                put("dateEpochDay", s.dateEpochDay)
                put("durationSeconds", s.durationSeconds)
                put("exerciseCount", s.exerciseCount)
                put("rounds", s.rounds)
                put("completed", s.completed)
            })
        }
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val KEY = "workout_history"

        fun todayEpochDay(zone: ZoneId = ZoneId.systemDefault()): Long =
            LocalDate.now(zone).toEpochDay()
    }
}
