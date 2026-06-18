package com.example.liks_sports.data

import org.json.JSONArray
import org.json.JSONObject

internal object AiEditParser {

    fun buildSystemPrompt(routine: Routine): String {
        val (globalExercise, globalRest) = computeGlobalDefaults(routine)
        val totalSeconds = routine.totalDurationSeconds
        val exercisesJson = JSONArray()
        for (ex in routine.exercises) {
            exercisesJson.put(JSONObject().apply {
                put("id", ex.id)
                put("name", ex.name)
                put("reps", ex.reps)
                put("exerciseDurationSeconds", ex.exerciseDurationSeconds)
                put("restDurationSeconds", ex.restDurationSeconds)
                put("overrideDefaults", ex.overrideDefaults)
            })
        }
        return """You are an AI assistant that helps edit workout routines. The user will ask you to modify a routine.

Current routine name: "${routine.name}"
Current global default times (applied to every exercise with overrideDefaults=false): exercise = ${globalExercise}s, rest = ${globalRest}s
Current exercises (overrideDefaults=true means the exercise uses its OWN time and ignores the global default):
$exercisesJson

DURATION MODEL
- Each exercise runs for exerciseDurationSeconds, followed by restDurationSeconds, repeated `reps` times.
- Per-round total duration = sum over all exercises of (reps × (exerciseDurationSeconds + restDurationSeconds)).
- Current per-round total: ${formatDuration(totalSeconds)} ($totalSeconds seconds).
- CONSTRAINTS: exerciseDurationSeconds and restDurationSeconds must be between 5 and 120, in steps of 5. reps must be between 1 and 10.
- Exercises with overrideDefaults=false follow the GLOBAL DEFAULT times. Changing the global default updates all of them at once.
- Exercises with overrideDefaults=true keep their own time and are NOT affected by the global default.

When the user asks for changes, respond with a JSON block wrapped in ```json fences containing an "actions" array. Available action types:

- {"type": "set_global_defaults", "exerciseDurationSeconds": 45, "restDurationSeconds": 15}
    Sets the global default times. This updates EVERY exercise whose overrideDefaults is false, in one action. Use this to make the whole routine longer or shorter. It does NOT touch exercises with overrideDefaults=true.
- {"type": "add_exercise", "name": "Exercise Name", "reps": 1}
    Adds an exercise that follows the global default (overrideDefaults=false). You usually do NOT need to set exerciseDurationSeconds/restDurationSeconds for new exercises — they inherit the global default automatically.
- {"type": "remove_exercise", "exerciseId": "the-id"}
- {"type": "rename_exercise", "exerciseId": "the-id", "name": "New Name"}
- {"type": "modify_exercise", "exerciseId": "the-id", "changes": {"reps": 3}}
    Use this to change reps, or to give one exercise a unique time. Only set "overrideDefaults": true when this exercise genuinely needs a duration DIFFERENT from the global default; once true it stops following the global default.
- {"type": "rename_routine", "name": "New Routine Name"}
- {"type": "message", "text": "Some chat message explaining what you did"}

IMPORTANT RULES
1. Keep overrideDefaults=false for MOST exercises. The global default is the main way to control routine length. Do NOT set overrideDefaults=true on every exercise — that freezes them and makes the global default useless.
2. To hit a target total (e.g. "30 minutes"), do the arithmetic first. 1 minute = 60 seconds. Pick a combination of: the number of exercises, their reps, and the global default times. Because each duration is capped at 120s, long routines need enough exercises and/or reps. For example, 10 exercises × 2 reps × (75s exercise + 15s rest) = 10 × 2 × 90 = 1800s = 30 min.
3. Use set_global_defaults to change the duration scale for all non-overridden exercises at once. Use modify_exercise only to change reps or to single out one exercise with a unique time.
4. Before responding, compute the resulting per-round total and adjust your numbers until it matches the user's target. State the resulting total in your message.

Example — user wants a 30-minute relaxation routine with 10 exercises, currently 1 exercise:

```json
{
  "actions": [
    {"type": "set_global_defaults", "exerciseDurationSeconds": 75, "restDurationSeconds": 15},
    {"type": "add_exercise", "name": "Cat-Cow", "reps": 2},
    {"type": "add_exercise", "name": "Child's Pose", "reps": 2},
    {"type": "message", "text": "Set global defaults to 75s exercise / 15s rest. With 10 exercises × 2 reps × 90s = 1800s = 30 min."}
  ]
}
```

Only respond with the JSON actions block — do not include any other text outside the JSON block."""
    }

    fun extractJsonBlock(text: String): String? {
        val fenced = Regex("```(?:json)?\\s*([\\s\\S]*?)```").find(text)
        if (fenced != null) {
            val body = fenced.groupValues[1].trim()
            if (body.isNotBlank()) return body
        }
        val trimmed = text.trim()
        if (trimmed.startsWith("{") && trimmed.contains("\"actions\"")) {
            return trimmed
        }
        return null
    }

    fun extractMessageText(fullResponse: String): String? {
        val jsonMatch = extractJsonBlock(fullResponse) ?: return null
        return try {
            val response = JSONObject(jsonMatch)
            val actions = response.optJSONArray("actions") ?: return null
            for (i in 0 until actions.length()) {
                val action = actions.getJSONObject(i)
                if (action.getString("type") == "message") {
                    return action.getString("text")
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }

    fun applyAiEdits(routine: Routine, aiResponse: String): Routine? {
        val jsonStr = extractJsonBlock(aiResponse) ?: return null
        val response = JSONObject(jsonStr)
        val actions = response.optJSONArray("actions") ?: return null

        var updated = routine
        for (i in 0 until actions.length()) {
            val action = actions.getJSONObject(i)
            when (action.getString("type")) {
                "set_global_defaults" -> {
                    val (curEx, curRest) = computeGlobalDefaults(updated)
                    val exerciseDur = snapDuration(action.optInt("exerciseDurationSeconds", curEx))
                    val restDur = snapDuration(action.optInt("restDurationSeconds", curRest))
                    updated = updated.copy(exercises = updated.exercises.map { ex ->
                        if (!ex.overrideDefaults)
                            ex.copy(exerciseDurationSeconds = exerciseDur, restDurationSeconds = restDur)
                        else ex
                    })
                }
                "add_exercise" -> {
                    val (curEx, curRest) = computeGlobalDefaults(updated)
                    val ex = Exercise(
                        name = action.getString("name"),
                        exerciseDurationSeconds = snapDuration(action.optInt("exerciseDurationSeconds", curEx)),
                        restDurationSeconds = snapDuration(action.optInt("restDurationSeconds", curRest)),
                        reps = action.optInt("reps", 1).coerceIn(1, 10),
                        overrideDefaults = false,
                    )
                    updated = updated.copy(exercises = updated.exercises + ex)
                }
                "remove_exercise" -> {
                    val id = action.getString("exerciseId")
                    updated = updated.copy(exercises = updated.exercises.filter { it.id != id })
                }
                "rename_exercise" -> {
                    val id = action.getString("exerciseId")
                    val name = action.getString("name")
                    updated = updated.copy(exercises = updated.exercises.map {
                        if (it.id == id) it.copy(name = name) else it
                    })
                }
                "modify_exercise" -> {
                    val id = action.getString("exerciseId")
                    val changes = action.getJSONObject("changes")
                    updated = updated.copy(exercises = updated.exercises.map { ex ->
                        if (ex.id == id) {
                            ex.copy(
                                name = changes.optString("name", ex.name),
                                exerciseDurationSeconds = if (changes.has("exerciseDurationSeconds"))
                                    snapDuration(changes.getInt("exerciseDurationSeconds")) else ex.exerciseDurationSeconds,
                                restDurationSeconds = if (changes.has("restDurationSeconds"))
                                    snapDuration(changes.getInt("restDurationSeconds")) else ex.restDurationSeconds,
                                reps = changes.optInt("reps", ex.reps).coerceIn(1, 10),
                                overrideDefaults = changes.optBoolean("overrideDefaults", ex.overrideDefaults),
                            )
                        } else ex
                    })
                }
                "rename_routine" -> {
                    updated = updated.copy(name = action.getString("name"))
                }
            }
        }
        return if (updated != routine) updated else null
    }

    /** Returns the (exercise, rest) global default seconds, derived from the non-overridden
     *  exercises' modal values (matching the UI's logic), falling back to 30/10. */
    private fun computeGlobalDefaults(routine: Routine): Pair<Int, Int> {
        val base = routine.exercises.filter { !it.overrideDefaults }
        val pool = if (base.isNotEmpty()) base else routine.exercises
        val exercise = modeOf(pool.map { it.exerciseDurationSeconds }, 30)
        val rest = modeOf(pool.map { it.restDurationSeconds }, 10)
        return exercise to rest
    }

    private fun modeOf(values: List<Int>, default: Int): Int {
        if (values.isEmpty()) return default
        return values.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key.coerceIn(5, 120)
    }

    private fun snapDuration(v: Int): Int =
        ((v.coerceIn(5, 120) + 2) / 5) * 5

    private fun formatDuration(totalSeconds: Int): String {
        val m = totalSeconds / 60
        val s = totalSeconds % 60
        return if (s == 0) "${m}m" else "${m}m ${s}s"
    }
}
