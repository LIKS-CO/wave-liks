package com.example.liks_sports.ui.screens

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.liks_sports.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liks_sports.data.Routine
import com.example.liks_sports.ui.icons.ArrowBack
import com.example.liks_sports.ui.icons.CheckCircle
import com.example.liks_sports.ui.icons.Pause
import com.example.liks_sports.ui.icons.PlayArrow
import com.example.liks_sports.ui.icons.SkipNext
import com.example.liks_sports.ui.icons.Stop
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseTimerScreen(
    routine: Routine,
    repeatCount: Int = 1,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current

    var currentRound by rememberSaveable { mutableIntStateOf(1) }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var currentRep by rememberSaveable { mutableIntStateOf(1) }
    var remainingSeconds by rememberSaveable {
        mutableIntStateOf(
            routine.exercises.firstOrNull()?.exerciseDurationSeconds ?: 0
        )
    }
    var isResting by rememberSaveable { mutableStateOf(false) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var isComplete by rememberSaveable { mutableStateOf(false) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    val currentExercise = remember(currentIndex) {
        routine.exercises.getOrNull(currentIndex)
    }

    val initialDuration = remember(currentIndex, isResting, currentExercise) {
        if (currentExercise != null) {
            if (isResting) currentExercise.restDurationSeconds
            else currentExercise.exerciseDurationSeconds
        } else 0
    }

    var currentRingtone by remember { mutableStateOf<android.media.Ringtone?>(null) }

    fun advancePhase() {
        val exercise = routine.exercises.getOrNull(currentIndex) ?: return
        if (isResting) {
            if (currentRep < exercise.reps) {
                currentRep += 1
                isResting = false
                remainingSeconds = exercise.exerciseDurationSeconds
            } else {
                val nextIndex = currentIndex + 1
                if (nextIndex < routine.exercises.size) {
                    currentIndex = nextIndex
                    currentRep = 1
                    isResting = false
                    remainingSeconds = routine.exercises.getOrNull(nextIndex)?.exerciseDurationSeconds ?: 0
                } else if (currentRound < repeatCount) {
                    currentRound += 1
                    currentIndex = 0
                    currentRep = 1
                    isResting = false
                    remainingSeconds = routine.exercises.firstOrNull()?.exerciseDurationSeconds ?: 0
                } else {
                    isComplete = true
                    isRunning = false
                }
            }
        } else {
            isResting = true
            remainingSeconds = currentExercise?.restDurationSeconds ?: 10
        }
    }

    LaunchedEffect(isRunning, isPaused, remainingSeconds) {
        if (!isRunning || isPaused) return@LaunchedEffect
        if (remainingSeconds <= 0) return@LaunchedEffect
        delay(1000L)
        if (remainingSeconds > 1) {
            remainingSeconds -= 1
        } else {
            remainingSeconds = 0
            playAlert(context, currentRingtone) { currentRingtone = it }
            advancePhase()
        }
    }

    var autoStarted by rememberSaveable { mutableStateOf(false) }
    val view = LocalView.current
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
            currentRingtone?.stop()
        }
    }
    if (!autoStarted && !isComplete && routine.exercises.isNotEmpty()) {
        LaunchedEffect(Unit) {
            isRunning = true
            autoStarted = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isComplete) stringResource(R.string.workout_complete) else routine.name) },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(imageVector = ArrowBack, contentDescription = stringResource(R.string.back_desc))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (isComplete) {
                Icon(
                    imageVector = CheckCircle,
                    contentDescription = stringResource(R.string.workout_complete),
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.all_exercises_complete),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.great_work),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onFinish) {
                    Text(stringResource(R.string.done))
                }
            } else if (routine.exercises.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_exercises_yet),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onFinish) {
                    Text(stringResource(R.string.back_desc))
                }
            } else if (currentExercise != null) {
                Text(
                    text = if (isResting) stringResource(R.string.rest_phase) else currentExercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isResting) stringResource(R.string.get_ready) else stringResource(R.string.current_exercise),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(32.dp))

                CircularProgressIndicator(
                    progress = { remainingSeconds.toFloat() / initialDuration.coerceAtLeast(1).toFloat() },
                    modifier = Modifier.size(200.dp),
                    strokeWidth = 12.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = if (isResting) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = formatTime(remainingSeconds),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isResting) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.height(8.dp))
                val totalRepsPerRound = routine.exercises.sumOf { it.reps }
                val totalExerciseCount = totalRepsPerRound * repeatCount
                val repsBefore = routine.exercises.take(currentIndex).sumOf { it.reps }
                val currentExerciseNumber =
                    (currentRound - 1) * totalRepsPerRound + repsBefore + currentRep

                LinearProgressIndicator(
                    progress = { currentExerciseNumber.toFloat() / totalExerciseCount.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                )
                Text(
                    text = stringResource(R.string.exercise_of, currentExerciseNumber, totalExerciseCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = {
                            isRunning = false
                            isPaused = false
                            isComplete = false
                            currentRound = 1
                            currentIndex = 0
                            currentRep = 1
                            isResting = false
                            remainingSeconds = routine.exercises.firstOrNull()?.exerciseDurationSeconds ?: 0
                            isRunning = true
                        }
                    ) {
                        Icon(imageVector = Stop, contentDescription = stringResource(R.string.reset), modifier = Modifier.size(20.dp))
                        Text(stringResource(R.string.reset), modifier = Modifier.padding(start = 4.dp))
                    }

                    Button(
                        onClick = { isPaused = !isPaused },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPaused) MaterialTheme.colorScheme.secondary
                                else MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Icon(
                            imageVector = if (isPaused) PlayArrow else Pause,
                            contentDescription = if (isPaused) stringResource(R.string.resume_desc) else stringResource(R.string.pause_desc),
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    OutlinedButton(
                        onClick = { advancePhase() }
                    ) {
                        Icon(imageVector = SkipNext, contentDescription = stringResource(R.string.skip), modifier = Modifier.size(20.dp))
                        Text(stringResource(R.string.skip), modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(mins, secs)
}

private fun playAlert(context: Context, previous: android.media.Ringtone?, setRingtone: (android.media.Ringtone?) -> Unit) {
    try {
        previous?.stop()
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, notification)
        ringtone?.play()
        setRingtone(ringtone)
    } catch (_: Exception) {}

    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }
    } catch (_: Exception) {}
}
