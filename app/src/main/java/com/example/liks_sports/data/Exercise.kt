package com.example.liks_sports.data

import java.util.UUID

data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val reps: Int = 1,
    val exerciseDurationSeconds: Int = 30,
    val restDurationSeconds: Int = 10,
    val overrideDefaults: Boolean = false,
)

data class Routine(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val exercises: List<Exercise> = emptyList(),
) {
    val totalDurationSeconds: Int
        get() = exercises.sumOf { ex ->
            ex.reps * (ex.exerciseDurationSeconds + ex.restDurationSeconds)
        }
}
