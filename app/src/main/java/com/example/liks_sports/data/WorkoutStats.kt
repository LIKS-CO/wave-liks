package com.example.liks_sports.data

import java.time.LocalDate
import java.time.ZoneId

data class WorkoutStats(
    val currentStreak: Int,
    val longestStreak: Int,
    val totalWorkouts: Int,
    val totalActiveSeconds: Int,
    val thisWeekCount: Int,
    val last7Days: List<Int>,
    val lastSessionDate: Long?,
)

object WorkoutStatsCalculator {

    fun compute(
        sessions: List<WorkoutSession>,
        zone: ZoneId = ZoneId.systemDefault(),
        today: LocalDate = LocalDate.now(zone),
    ): WorkoutStats {
        if (sessions.isEmpty()) {
            return WorkoutStats(
                currentStreak = 0,
                longestStreak = 0,
                totalWorkouts = 0,
                totalActiveSeconds = 0,
                thisWeekCount = 0,
                last7Days = List(7) { 0 },
                lastSessionDate = null,
            )
        }

        val completedByDay: Map<Long, Int> = sessions
            .filter { it.completed }
            .groupingBy { it.dateEpochDay }
            .eachCount()

        val todayEpoch = today.toEpochDay()
        val currentStreak = computeCurrentStreak(completedByDay, todayEpoch)
        val longestStreak = computeLongestStreak(completedByDay)

        val totalWorkouts = sessions.count { it.completed }
        val totalActiveSeconds = sessions.sumOf { it.durationSeconds }

        val firstOfWeek = today.minusDays((today.dayOfWeek.value - 1).toLong())
        val firstOfWeekEpoch = firstOfWeek.toEpochDay()
        val thisWeekCount = completedByDay.entries
            .filter { it.key in firstOfWeekEpoch..todayEpoch }
            .sumOf { it.value }

        val last7Days = (6 downTo 0).map { back ->
            val day = today.minusDays(back.toLong()).toEpochDay()
            completedByDay[day] ?: 0
        }

        val lastSessionDate = sessions.maxOf { it.dateEpochDay }

        return WorkoutStats(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalWorkouts = totalWorkouts,
            totalActiveSeconds = totalActiveSeconds,
            thisWeekCount = thisWeekCount,
            last7Days = last7Days,
            lastSessionDate = lastSessionDate,
        )
    }

    private fun computeCurrentStreak(completedByDay: Map<Long, Int>, todayEpoch: Long): Int {
        if (completedByDay.isEmpty()) return 0
        var day = todayEpoch
        if (!completedByDay.containsKey(day)) {
            val yesterday = todayEpoch - 1
            if (!completedByDay.containsKey(yesterday)) return 0
            day = yesterday
        }
        var streak = 0
        while (completedByDay.containsKey(day)) {
            streak++
            day--
        }
        return streak
    }

    private fun computeLongestStreak(completedByDay: Map<Long, Int>): Int {
        if (completedByDay.isEmpty()) return 0
        val sortedDays = completedByDay.keys.sorted()
        var longest = 1
        var run = 1
        for (i in 1 until sortedDays.size) {
            if (sortedDays[i] == sortedDays[i - 1] + 1) {
                run++
                if (run > longest) longest = run
            } else {
                run = 1
            }
        }
        return longest
    }
}

fun lastSessionByRoutine(sessions: List<WorkoutSession>): Map<String, WorkoutSession> {
    val latest = mutableMapOf<String, WorkoutSession>()
    for (s in sessions) {
        val existing = latest[s.routineId]
        if (existing == null || s.dateEpochDay > existing.dateEpochDay) {
            latest[s.routineId] = s
        }
    }
    return latest
}

fun countByRoutine(sessions: List<WorkoutSession>): Map<String, Int> =
    sessions.countingBy { it.routineId }

private inline fun <T> Iterable<T>.countingBy(key: (T) -> String): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    for (e in this) {
        val k = key(e)
        map[k] = (map[k] ?: 0) + 1
    }
    return map
}
