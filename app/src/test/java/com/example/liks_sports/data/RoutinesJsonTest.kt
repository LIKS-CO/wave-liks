package com.example.liks_sports.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutinesJsonTest {

    private fun sampleRoutine(): Routine = Routine(
        id = "r1",
        name = "Morning",
        exercises = listOf(
            Exercise(
                id = "e1",
                name = "Squats",
                reps = 3,
                exerciseDurationSeconds = 40,
                restDurationSeconds = 20,
                overrideDefaults = true,
            ),
            Exercise(
                id = "e2",
                name = "Plank",
                reps = 1,
                exerciseDurationSeconds = 60,
                restDurationSeconds = 15,
                overrideDefaults = false,
            ),
        ),
    )

    @Test
    fun routineRoundTrip_preservesData() {
        val routines = listOf(sampleRoutine())
        val json = RoutinesJson.toJson(routines)
        val restored = RoutinesJson.fromJson(json)
        assertEquals(routines, restored)
    }

    @Test
    fun emptyListRoundTrip() {
        val json = RoutinesJson.toJson(emptyList())
        assertEquals(emptyList<Routine>(), RoutinesJson.fromJson(json))
    }

    @Test
    fun fromJson_acceptsLegacyArrayFormat() {
        val legacy = """[{"id":"r1","name":"Legacy","exercises":[{"id":"e1","name":"X","reps":1,"exerciseDurationSeconds":30,"restDurationSeconds":10,"overrideDefaults":false}]}]"""
        val restored = RoutinesJson.fromJson(legacy)
        assertEquals(1, restored.size)
        assertEquals("r1", restored[0].id)
        assertEquals("Legacy", restored[0].name)
        assertEquals(1, restored[0].exercises.size)
        assertEquals("X", restored[0].exercises[0].name)
    }

    @Test
    fun toJson_includesFormatVersion() {
        val json = RoutinesJson.toJson(emptyList())
        assertTrue(json.contains("\"format_version\":${RoutinesJson.FORMAT_VERSION}"))
    }

    @Test
    fun dismissedSetRoundTrip() {
        val set = setOf("builtin_parkour", "builtin_football")
        val json = RoutinesJson.toJsonSet(set)
        assertEquals(set, RoutinesJson.fromJsonSet(json))
    }

    @Test
    fun emptySetRoundTrip() {
        val json = RoutinesJson.toJsonSet(emptySet())
        assertEquals(emptySet<String>(), RoutinesJson.fromJsonSet(json))
    }

    @Test
    fun fromJson_skipsCorruptRoutineButKeepsGoodOnes() {
        // Good routine followed by a corrupt one (missing "exercises" array),
        // then another good routine. Only the good ones should survive.
        val json = """{"format_version":1,"routines":[
            {"id":"good1","name":"Good 1","exercises":[{"id":"e1","name":"X","reps":1,"exerciseDurationSeconds":30,"restDurationSeconds":10,"overrideDefaults":false}]},
            {"id":"bad","name":"Bad"},
            {"id":"good2","name":"Good 2","exercises":[]}
        ]}"""
        val restored = RoutinesJson.fromJson(json)
        assertEquals(2, restored.size)
        assertEquals(listOf("good1", "good2"), restored.map { it.id })
    }

    @Test
    fun fromJson_skipsCorruptExerciseButKeepsGoodOnes() {
        val json = """{"format_version":1,"routines":[
            {"id":"r1","name":"R","exercises":[
                {"id":"e1","name":"Good","reps":1,"exerciseDurationSeconds":30,"restDurationSeconds":10,"overrideDefaults":false},
                {"id":"e2"},
                {"id":"e3","name":"Good3","reps":2,"exerciseDurationSeconds":45,"restDurationSeconds":15,"overrideDefaults":true}
            ]}
        ]}"""
        val restored = RoutinesJson.fromJson(json)
        assertEquals(1, restored.size)
        assertEquals(2, restored[0].exercises.size)
        assertEquals(listOf("e1", "e3"), restored[0].exercises.map { it.id })
    }

    @Test
    fun exercisesRoundTrip_preservesData() {
        val exercises = listOf(
            Exercise(id = "e1", name = "Squats", reps = 3, exerciseDurationSeconds = 40, restDurationSeconds = 20, overrideDefaults = true),
            Exercise(id = "e2", name = "Plank", reps = 1, exerciseDurationSeconds = 60, restDurationSeconds = 15, overrideDefaults = false),
        )
        val json = RoutinesJson.exercisesToJson(exercises)
        assertEquals(exercises, RoutinesJson.exercisesFromJson(json))
    }

    @Test
    fun exercisesFromJson_emptyArray() {
        assertEquals(emptyList<Exercise>(), RoutinesJson.exercisesFromJson("[]"))
    }
}
