package com.example.liks_sports.data

import org.json.JSONArray
import org.json.JSONObject

internal object RoutinesJson {

    const val FORMAT_VERSION = 1

    fun toJson(routines: List<Routine>): String {
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

    fun fromJson(json: String): List<Routine> {
        val arr = try {
            val root = JSONObject(json)
            when (root.optInt("format_version", 0)) {
                FORMAT_VERSION -> root.getJSONArray("routines")
                else -> root.optJSONArray("routines") ?: JSONArray(json)
            }
        } catch (_: Exception) {
            JSONArray(json)
        }
        return (0 until arr.length()).mapNotNull { i ->
            try {
                val obj = arr.getJSONObject(i)
                val exercisesArr = obj.getJSONArray("exercises")
                val exercises = (0 until exercisesArr.length()).mapNotNull { j ->
                    try {
                        val e = exercisesArr.getJSONObject(j)
                        Exercise(
                            id = e.getString("id"),
                            name = e.getString("name"),
                            reps = e.optInt("reps", 1),
                            exerciseDurationSeconds = e.optInt("exerciseDurationSeconds", 30),
                            restDurationSeconds = e.optInt("restDurationSeconds", 10),
                            overrideDefaults = e.optBoolean("overrideDefaults", false),
                        )
                    } catch (_: Exception) {
                        null
                    }
                }
                if (exercises.isEmpty() && exercisesArr.length() > 0) return@mapNotNull null
                Routine(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    exercises = exercises,
                )
            } catch (_: Exception) {
                null
            }
        }
    }

    fun toJsonSet(set: Set<String>): String {
        val arr = JSONArray()
        for (s in set) arr.put(s)
        return arr.toString()
    }

    fun fromJsonSet(json: String): Set<String> {
        val arr = JSONArray(json)
        return (0 until arr.length()).map { arr.getString(it) }.toSet()
    }

    fun exercisesToJson(exercises: List<Exercise>): String {
        val arr = JSONArray()
        for (e in exercises) {
            arr.put(
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
        return arr.toString()
    }

    fun exercisesFromJson(json: String): List<Exercise> {
        val arr = JSONArray(json)
        return (0 until arr.length()).mapNotNull { i ->
            try {
                val e = arr.getJSONObject(i)
                Exercise(
                    id = e.getString("id"),
                    name = e.getString("name"),
                    reps = e.optInt("reps", 1),
                    exerciseDurationSeconds = e.optInt("exerciseDurationSeconds", 30),
                    restDurationSeconds = e.optInt("restDurationSeconds", 10),
                    overrideDefaults = e.optBoolean("overrideDefaults", false),
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}
