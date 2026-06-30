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
    Use this to change reps, or to give one exercise a unique time. When you set "exerciseDurationSeconds" or "restDurationSeconds" the app AUTOMATICALLY marks that exercise as overridden (overrideDefaults=true) so its time is protected from later set_global_defaults changes. You usually do NOT need to set "overrideDefaults" yourself; only set it to false to make an exercise follow the global default again.
- {"type": "rename_routine", "name": "New Routine Name"}
- {"type": "message", "text": "Some chat message explaining what you did"}

IMPORTANT RULES
1. Keep overrideDefaults=false for MOST exercises. The global default is the main way to control routine length. Do NOT set overrideDefaults=true on every exercise — that freezes them and makes the global default useless.
2. To hit a target total (e.g. "30 minutes"), do the arithmetic first. 1 minute = 60 seconds. Pick a combination of: the number of exercises, their reps, and the global default times. Because each duration is capped at 120s, long routines need enough exercises and/or reps. For example, 10 exercises × 2 reps × (75s exercise + 15s rest) = 10 × 2 × 90 = 1800s = 30 min.
3. Use set_global_defaults to change the duration scale for all non-overridden exercises at once. Use modify_exercise only to change reps or to single out one exercise with a unique time.
4. Before responding, compute the resulting per-round total and adjust your numbers until it matches the user's target. State the resulting total in your message.
5. A per-exercise duration you set in add_exercise or modify_exercise is ALWAYS pinned to that exercise (the app forces overrideDefaults=true). To rescale the whole routine, use set_global_defaults — do NOT set durations on every exercise.

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
        // Prefer a fenced ```json block, but take everything up to the LAST
        // closing fence so that stray ``` appearing inside JSON string values
        // don't truncate the body.
        val openFence = Regex("```(?:json)?\\s*").find(text)
        if (openFence != null) {
            val bodyStart = openFence.range.last + 1
            val lastClose = text.lastIndexOf("```")
            val region = if (lastClose > bodyStart) {
                text.substring(bodyStart, lastClose)
            } else {
                // Opening fence with no closing fence — scan the remainder.
                text.substring(bodyStart)
            }
            firstBalancedObjectWithActions(region)?.let { return it }
        }
        // Fallback: scan the whole text for the first balanced object that
        // contains "actions". This tolerates leading/trailing prose and a
        // missing closing fence.
        return firstBalancedObjectWithActions(text)
    }

    /**
     * Returns the substring of [source] corresponding to the first balanced
     * `{...}` object that contains an `"actions"` key, or null if none is
     * found. String literals and escapes are respected so braces inside JSON
     * strings don't confuse the scanner.
     */
    private fun firstBalancedObjectWithActions(source: String): String? {
        var i = 0
        val n = source.length
        while (i < n) {
            val open = source.indexOf('{', i)
            if (open < 0) break
            var depth = 0
            var inString = false
            var escaped = false
            var end = -1
            var j = open
            while (j < n) {
                val c = source[j]
                if (inString) {
                    if (escaped) {
                        escaped = false
                    } else if (c == '\\') {
                        escaped = true
                    } else if (c == '"') {
                        inString = false
                    }
                } else {
                    when (c) {
                        '"' -> inString = true
                        '{' -> depth++
                        '}' -> {
                            depth--
                            if (depth == 0) { end = j; break }
                        }
                    }
                }
                j++
            }
            if (end < 0) break // Unbalanced; no further complete objects exist.
            val candidate = source.substring(open, end + 1)
            if (candidate.contains("\"actions\"")) return candidate
            i = end + 1
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
        val response = try {
            JSONObject(jsonStr)
        } catch (_: Exception) {
            return null
        }
        val actions = response.optJSONArray("actions") ?: return null

        var updated = routine
        for (i in 0 until actions.length()) {
            val action = try {
                actions.getJSONObject(i)
            } catch (_: Exception) {
                continue
            }
            val type = action.optString("type", "")
            // One malformed action must not abort the rest of the batch.
            val result = try {
                when (type) {
                    "set_global_defaults" -> applySetGlobalDefaults(updated, action)
                    "add_exercise" -> applyAddExercise(updated, action)
                    "remove_exercise" -> applyRemoveExercise(updated, action)
                    "rename_exercise" -> applyRenameExercise(updated, action)
                    "modify_exercise" -> applyModifyExercise(updated, action)
                    "rename_routine" -> applyRenameRoutine(updated, action)
                    else -> null // Unknown action type — skip silently.
                }
            } catch (_: Exception) {
                null
            }
            if (result != null) updated = result
        }
        return if (updated != routine) updated else null
    }

    private fun applySetGlobalDefaults(routine: Routine, action: JSONObject): Routine {
        val (curEx, curRest) = computeGlobalDefaults(routine)
        val exerciseDur = snapDuration(action.optInt("exerciseDurationSeconds", curEx))
        val restDur = snapDuration(action.optInt("restDurationSeconds", curRest))
        return routine.copy(exercises = routine.exercises.map { ex ->
            if (!ex.overrideDefaults)
                ex.copy(exerciseDurationSeconds = exerciseDur, restDurationSeconds = restDur)
            else ex
        })
    }

    private fun applyAddExercise(routine: Routine, action: JSONObject): Routine {
        val name = action.optString("name", "").trim()
        if (name.isEmpty()) return routine
        val (curEx, curRest) = computeGlobalDefaults(routine)
        val exDur = if (action.has("exerciseDurationSeconds"))
            snapDuration(action.getInt("exerciseDurationSeconds")) else curEx
        val restDur = if (action.has("restDurationSeconds"))
            snapDuration(action.getInt("restDurationSeconds")) else curRest
        // A duration that differs from the global default pins the exercise;
        // an explicit overrideDefaults wins over the inferred value.
        val inferredOverride = exDur != curEx || restDur != curRest
        val override = action.optBoolean("overrideDefaults", inferredOverride)
        val ex = Exercise(
            name = name,
            exerciseDurationSeconds = exDur,
            restDurationSeconds = restDur,
            reps = action.optInt("reps", 1).coerceIn(1, 10),
            overrideDefaults = override,
        )
        return routine.copy(exercises = routine.exercises + ex)
    }

    private fun applyRemoveExercise(routine: Routine, action: JSONObject): Routine {
        val id = action.optString("exerciseId", "")
        if (id.isEmpty()) return routine
        return routine.copy(exercises = routine.exercises.filter { it.id != id })
    }

    private fun applyRenameExercise(routine: Routine, action: JSONObject): Routine {
        val id = action.optString("exerciseId", "")
        val name = action.optString("name", "").trim()
        if (id.isEmpty() || name.isEmpty()) return routine
        return routine.copy(exercises = routine.exercises.map {
            if (it.id == id) it.copy(name = name) else it
        })
    }

    private fun applyModifyExercise(routine: Routine, action: JSONObject): Routine {
        val id = action.optString("exerciseId", "")
        if (id.isEmpty()) return routine
        val changes = action.optJSONObject("changes") ?: return routine
        val hasDurationChange =
            changes.has("exerciseDurationSeconds") || changes.has("restDurationSeconds")
        // Supplying a duration pins the exercise unless the caller explicitly
        // requests overrideDefaults=false (which re-attaches it to the global
        // default — the duration is then overwritten by the next set_global_defaults).
        val explicitOverride: Boolean? =
            if (changes.has("overrideDefaults")) changes.getBoolean("overrideDefaults") else null
        return routine.copy(exercises = routine.exercises.map { ex ->
            if (ex.id == id) {
                val newOverride = when {
                    explicitOverride != null -> explicitOverride
                    hasDurationChange -> true
                    else -> ex.overrideDefaults
                }
                ex.copy(
                    name = changes.optString("name", ex.name),
                    exerciseDurationSeconds = if (changes.has("exerciseDurationSeconds"))
                        snapDuration(changes.getInt("exerciseDurationSeconds")) else ex.exerciseDurationSeconds,
                    restDurationSeconds = if (changes.has("restDurationSeconds"))
                        snapDuration(changes.getInt("restDurationSeconds")) else ex.restDurationSeconds,
                    reps = changes.optInt("reps", ex.reps).coerceIn(1, 10),
                    overrideDefaults = newOverride,
                )
            } else ex
        })
    }

    private fun applyRenameRoutine(routine: Routine, action: JSONObject): Routine {
        val name = action.optString("name", "").trim()
        if (name.isEmpty()) return routine
        return routine.copy(name = name)
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
