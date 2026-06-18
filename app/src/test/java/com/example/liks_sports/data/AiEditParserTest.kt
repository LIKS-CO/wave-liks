package com.example.liks_sports.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiEditParserTest {

    private fun routine(): Routine = Routine(
        id = "r1",
        name = "Original",
        exercises = listOf(
            Exercise(
                id = "e1",
                name = "Squats",
                reps = 2,
                exerciseDurationSeconds = 30,
                restDurationSeconds = 10,
                overrideDefaults = false,
            ),
        ),
    )

    @Test
    fun extractJsonBlock_prefersJsonFence() {
        val text = "Here you go:\n```json\n{\"actions\":[]}\n```\nDone."
        assertEquals("{\"actions\":[]}", AiEditParser.extractJsonBlock(text))
    }

    @Test
    fun extractJsonBlock_acceptsPlainFence() {
        val text = "```\n{\"actions\":[]}\n```"
        assertEquals("{\"actions\":[]}", AiEditParser.extractJsonBlock(text))
    }

    @Test
    fun extractJsonBlock_acceptsBareJson() {
        val text = "{\"actions\":[{\"type\":\"message\",\"text\":\"hi\"}]}"
        assertEquals(text, AiEditParser.extractJsonBlock(text))
    }

    @Test
    fun extractJsonBlock_returnsNullWhenNoJson() {
        assertNull(AiEditParser.extractJsonBlock("Just chatting, no JSON here."))
    }

    @Test
    fun applyAiEdits_addExercise() {
        val resp = """```json
          {"actions":[{"type":"add_exercise","name":"Plank","exerciseDurationSeconds":60,"restDurationSeconds":25,"reps":1}]}
        ```"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertNotNull(edited)
        assertEquals(2, edited!!.exercises.size)
        assertEquals("Plank", edited.exercises[1].name)
        assertEquals(60, edited.exercises[1].exerciseDurationSeconds)
    }

    @Test
    fun applyAiEdits_removeExercise() {
        val resp = """{"actions":[{"type":"remove_exercise","exerciseId":"e1"}]}"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertNotNull(edited)
        assertTrue(edited!!.exercises.isEmpty())
    }

    @Test
    fun applyAiEdits_renameExercise() {
        val resp = """{"actions":[{"type":"rename_exercise","exerciseId":"e1","name":"Lunges"}]}"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertEquals("Lunges", edited!!.exercises[0].name)
    }

    @Test
    fun applyAiEdits_modifyExercise() {
        val resp = """{"actions":[{"type":"modify_exercise","exerciseId":"e1","changes":{"reps":5,"exerciseDurationSeconds":45,"overrideDefaults":true}}]}"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertEquals(5, edited!!.exercises[0].reps)
        assertEquals(45, edited.exercises[0].exerciseDurationSeconds)
        assertTrue(edited.exercises[0].overrideDefaults)
    }

    @Test
    fun applyAiEdits_renameRoutine() {
        val resp = """{"actions":[{"type":"rename_routine","name":"New Name"}]}"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertEquals("New Name", edited!!.name)
    }

    @Test
    fun applyAiEdits_multipleActions() {
        val resp = """{"actions":[
            {"type":"add_exercise","name":"A","reps":1},
            {"type":"add_exercise","name":"B","reps":2}
        ]}"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertEquals(3, edited!!.exercises.size)
    }

    @Test
    fun applyAiEdits_returnsNullForInvalidJson() {
        assertNull(AiEditParser.applyAiEdits(routine(), "no json here"))
    }

    @Test
    fun applyAiEdits_returnsNullWhenNoChange() {
        val resp = """{"actions":[{"type":"message","text":"hi"}]}"""
        assertNull(AiEditParser.applyAiEdits(routine(), resp))
    }

    @Test
    fun extractMessageText_returnsMessageActionText() {
        val resp = """{"actions":[{"type":"add_exercise","name":"A"},{"type":"message","text":"Added A"}]}"""
        assertEquals("Added A", AiEditParser.extractMessageText(resp))
    }

    @Test
    fun extractMessageText_returnsNullWhenNoMessageAction() {
        val resp = """{"actions":[{"type":"add_exercise","name":"A"}]}"""
        assertNull(AiEditParser.extractMessageText(resp))
    }

    @Test
    fun buildSystemPrompt_containsRoutineNameAndExercises() {
        val prompt = AiEditParser.buildSystemPrompt(routine())
        assertTrue(prompt.contains("Original"))
        assertTrue(prompt.contains("Squats"))
        assertTrue(prompt.contains("actions"))
    }

    @Test
    fun buildSystemPrompt_includesGlobalDefaultsAndTotal() {
        val prompt = AiEditParser.buildSystemPrompt(routine())
        assertTrue(prompt.contains("global default times"))
        assertTrue(prompt.contains("exercise = 30s")) // global exercise default
        assertTrue(prompt.contains("rest = 10s"))     // global rest default
        assertTrue(prompt.contains("DURATION MODEL"))
        assertTrue(prompt.contains("set_global_defaults"))
        // 2 reps × (30 + 10) = 80s -> "1m 20s"
        assertTrue(prompt.contains("1m 20s"))
        assertTrue(prompt.contains("80 seconds"))
    }

    @Test
    fun applyAiEdits_setGlobalDefaults_updatesNonOverriddenOnly() {
        val r = Routine(
            id = "r1",
            name = "Test",
            exercises = listOf(
                Exercise(id = "a", name = "A", reps = 1, exerciseDurationSeconds = 30, restDurationSeconds = 10, overrideDefaults = false),
                Exercise(id = "b", name = "B", reps = 1, exerciseDurationSeconds = 45, restDurationSeconds = 20, overrideDefaults = true),
            ),
        )
        val resp = """{"actions":[{"type":"set_global_defaults","exerciseDurationSeconds":60,"restDurationSeconds":15}]}"""
        val edited = AiEditParser.applyAiEdits(r, resp)
        assertNotNull(edited)
        val a = edited!!.exercises.first { it.id == "a" }
        val b = edited.exercises.first { it.id == "b" }
        assertEquals(60, a.exerciseDurationSeconds)
        assertEquals(15, a.restDurationSeconds)
        assertFalse(a.overrideDefaults)
        // Overridden exercise is untouched.
        assertEquals(45, b.exerciseDurationSeconds)
        assertEquals(20, b.restDurationSeconds)
        assertTrue(b.overrideDefaults)
    }

    @Test
    fun applyAiEdits_setGlobalDefaults_clampsAndSnapsToStepsOf5() {
        val r = Routine(
            id = "r1",
            name = "Test",
            exercises = listOf(
                Exercise(id = "a", name = "A", reps = 1, exerciseDurationSeconds = 30, restDurationSeconds = 10, overrideDefaults = false),
            ),
        )
        val resp = """{"actions":[{"type":"set_global_defaults","exerciseDurationSeconds":150,"restDurationSeconds":3}]}"""
        val edited = AiEditParser.applyAiEdits(r, resp)
        // 150 -> clamped to 120; 3 -> clamped to 5.
        assertEquals(120, edited!!.exercises[0].exerciseDurationSeconds)
        assertEquals(5, edited.exercises[0].restDurationSeconds)
    }

    @Test
    fun applyAiEdits_addExerciseWithoutDurationInheritsGlobalDefault() {
        val r = Routine(
            id = "r1",
            name = "Test",
            exercises = listOf(
                Exercise(id = "a", name = "A", reps = 1, exerciseDurationSeconds = 50, restDurationSeconds = 20, overrideDefaults = false),
            ),
        )
        val resp = """{"actions":[{"type":"add_exercise","name":"B","reps":2}]}"""
        val edited = AiEditParser.applyAiEdits(r, resp)
        val added = edited!!.exercises.first { it.name == "B" }
        assertEquals(50, added.exerciseDurationSeconds)
        assertEquals(20, added.restDurationSeconds)
        assertFalse(added.overrideDefaults)
        assertEquals(2, added.reps)
    }

    @Test
    fun applyAiEdits_modifyExerciseRepsClampedToRange() {
        val resp = """{"actions":[{"type":"modify_exercise","exerciseId":"e1","changes":{"reps":99}}]}"""
        val edited = AiEditParser.applyAiEdits(routine(), resp)
        assertEquals(10, edited!!.exercises[0].reps)
    }
}
