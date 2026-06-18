package com.example.liks_sports.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liks_sports.R
import com.example.liks_sports.data.WorkoutStats
import com.example.liks_sports.ui.icons.BarChart
import com.example.liks_sports.ui.icons.CalendarViewWeek
import com.example.liks_sports.ui.icons.EmojiEvents
import com.example.liks_sports.ui.icons.LocalFireDepartment
import com.example.liks_sports.ui.icons.Schedule
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardHeader(stats: WorkoutStats, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (stats.totalWorkouts == 0) {
            EmptyDashboardCard()
        } else {
            StreakHeroCard(stats)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = CalendarViewWeek,
                    value = stats.thisWeekCount.toString(),
                    label = stringResource(R.string.dashboard_this_week),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = EmojiEvents,
                    value = stats.totalWorkouts.toString(),
                    label = stringResource(R.string.dashboard_total_workouts),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Schedule,
                    value = formatActiveTime(stats.totalActiveSeconds),
                    label = stringResource(R.string.dashboard_total_time),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            ActivityStripCard(stats)
        }
    }
}

@Composable
private fun StreakHeroCard(stats: WorkoutStats) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary

    val animatedStreak = remember(stats.currentStreak) { Animatable(0f) }
    LaunchedEffect(stats.currentStreak) {
        animatedStreak.animateTo(stats.currentStreak.toFloat(), tween(700, easing = LinearEasing))
    }

    val infinite = rememberInfiniteTransition(label = "flame")
    val pulse by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse",
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(primary, tertiary))
                )
                .background(Color.Black.copy(alpha = 0.12f))
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp).padding(start = 2.dp),
                        tint = Color.White,
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.animateContentSize()) {
                    Text(
                        text = stringResource(R.string.dashboard_day_streak, animatedStreak.value.toInt()),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Text(
                        text = stringResource(R.string.dashboard_longest_streak, stats.longestStreak),
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Icon(
                imageVector = LocalFireDepartment,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(96.dp)
                    .padding(start = 2.dp),
                tint = Color.White.copy(alpha = 0.16f * pulse),
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    tint: Color,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ActivityStripCard(stats: WorkoutStats) {
    val dayLetters = rememberDayLetters()
    val maxCount = (stats.last7Days.maxOrNull() ?: 0).coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = BarChart,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.dashboard_7day_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                stats.last7Days.forEachIndexed { index, count ->
                    val isToday = index == stats.last7Days.lastIndex
                    ActivityBar(
                        modifier = Modifier.weight(1f),
                        letter = dayLetters.getOrElse(index) { "" },
                        fraction = count.toFloat() / maxCount.toFloat(),
                        active = count > 0,
                        isToday = isToday,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityBar(
    modifier: Modifier = Modifier,
    letter: String,
    fraction: Float,
    active: Boolean,
    isToday: Boolean,
) {
    val grow by animateFloatAsState(
        targetValue = fraction.coerceIn(0.06f, 1f),
        animationSpec = tween(600, easing = LinearEasing),
        label = "grow",
    )
    val barColor = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val todayRing = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.55f),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp * grow)
                    .clip(RoundedCornerShape(6.dp))
                    .background(barColor),
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = letter,
            style = MaterialTheme.typography.labelSmall,
            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            fontSize = 11.sp,
        )
    }
}

@Composable
private fun EmptyDashboardCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = LocalFireDepartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.dashboard_empty_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.dashboard_empty_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                )
            }
        }
    }
}

@Composable
private fun rememberDayLetters(): List<String> {
    val locale = LocalConfiguration.current.locales.get(0) ?: Locale.getDefault()
    val formatter = remember(locale) { DateTimeFormatter.ofPattern("EEEEE", locale) }
    return remember(formatter) {
        val today = LocalDate.now()
        (6 downTo 0).map { back ->
            today.minusDays(back.toLong()).format(formatter)
        }
    }
}

private fun formatActiveTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val mins = (totalSeconds % 3600) / 60
    return if (hours > 0) "%dh %dm".format(hours, mins) else "%dm".format(mins)
}
