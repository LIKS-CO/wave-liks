package com.example.liks_sports.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.liks_sports.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.liks_sports.data.Exercise
import com.example.liks_sports.data.Routine
import com.example.liks_sports.ui.icons.Add
import com.example.liks_sports.ui.icons.ArrowBack
import com.example.liks_sports.ui.icons.Check
import com.example.liks_sports.ui.icons.Close
import com.example.liks_sports.ui.icons.Delete
import com.example.liks_sports.ui.icons.Edit
import com.example.liks_sports.ui.icons.ExpandMore
import com.example.liks_sports.ui.icons.AutoAwesome
import com.example.liks_sports.ui.icons.Timer
import com.example.liks_sports.data.ChatHistoryStore
import com.example.liks_sports.data.ChatMessage
import com.example.liks_sports.data.SettingsStore
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routine: Routine,
    onUpdateRoutine: (Routine) -> Unit,
    onBack: () -> Unit,
    onStart: (repeatCount: Int) -> Unit,
    settingsStore: SettingsStore,
    chatHistoryStore: ChatHistoryStore,
    onOpenSettings: () -> Unit,
) {
    var routineName by rememberSaveable(routine.id) { mutableStateOf(routine.name) }
    var exercises by remember { mutableStateOf(routine.exercises) }
    val initialDefaults = remember(routine.id) {
        if (routine.exercises.isEmpty()) {
            30 to 10
        } else {
            val exerciseMode = routine.exercises.groupingBy { it.exerciseDurationSeconds }
                .eachCount().maxByOrNull { it.value }?.key?.coerceIn(5, 120) ?: 30
            val restMode = routine.exercises.groupingBy { it.restDurationSeconds }
                .eachCount().maxByOrNull { it.value }?.key?.coerceIn(5, 120) ?: 10
            exerciseMode to restMode
        }
    }
    var globalExerciseDuration by remember { mutableStateOf(initialDefaults.first) }
    var globalRestDuration by remember { mutableStateOf(initialDefaults.second) }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newExerciseName by rememberSaveable { mutableStateOf("") }
    var showRenameDialog by rememberSaveable { mutableStateOf(false) }
    var renameText by rememberSaveable { mutableStateOf("") }
    var renameExerciseId by rememberSaveable { mutableStateOf("") }
    var repeatCount by rememberSaveable { mutableStateOf(1) }
    var showRepsMenu by rememberSaveable { mutableStateOf(false) }
    var showAiChat by rememberSaveable { mutableStateOf(false) }
    val chatHistoryState = remember { mutableStateOf(chatHistoryStore.getMessages(routine.id)) }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = {
                showRenameDialog = false
                renameText = ""
                renameExerciseId = ""
            },
            title = { Text(stringResource(R.string.rename_exercise)) },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text(stringResource(R.string.exercise_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (renameText.isNotBlank()) {
                            exercises = exercises.map {
                                if (it.id == renameExerciseId) it.copy(name = renameText.trim()) else it
                            }
                            onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                            showRenameDialog = false
                            renameText = ""
                            renameExerciseId = ""
                        }
                    }
                ) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRenameDialog = false
                        renameText = ""
                        renameExerciseId = ""
                    }
                ) { Text(stringResource(R.string.cancel)) }
            },
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                newExerciseName = ""
            },
            title = { Text(stringResource(R.string.new_exercise)) },
            text = {
                OutlinedTextField(
                    value = newExerciseName,
                    onValueChange = { newExerciseName = it },
                    label = { Text(stringResource(R.string.exercise_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newExerciseName.isNotBlank()) {
                            exercises = exercises + Exercise(name = newExerciseName.trim())
                            onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                            showAddDialog = false
                            newExerciseName = ""
                        }
                    }
                ) {
                    Text(stringResource(R.string.add))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        newExerciseName = ""
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    if (showAiChat) {
        AiChatDialog(
            routine = routine.copy(name = routineName, exercises = exercises),
            settings = settingsStore,
            chatHistory = chatHistoryState.value,
            onSendMessage = { msg ->
                chatHistoryStore.addMessage(routine.id, ChatMessage("user", msg))
            },
            onApplyEdits = { edited ->
                exercises = edited.exercises
                routineName = edited.name
                onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
            },
            onAiResponded = { text ->
                chatHistoryStore.addMessage(routine.id, ChatMessage("assistant", text))
            },
            onClearSession = {
                chatHistoryStore.clear(routine.id)
            },
            onDismiss = { showAiChat = false },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routine.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = ArrowBack, contentDescription = stringResource(R.string.back_desc))
                    }
                },
                actions = {
                    SettingsIconButton(onClick = onOpenSettings)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                    onStart(repeatCount)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Icon(
                    imageVector = Timer,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.start_workout))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = routineName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    val totalSecs = exercises.sumOf { ex ->
                        ex.reps * (ex.exerciseDurationSeconds + ex.restDurationSeconds)
                    }
                    Text(
                        text = stringResource(R.string.total_time, "%d:%02d".format(totalSecs / 60, totalSecs % 60)),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                        text = stringResource(R.string.default_times),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(R.string.default_times_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DurationSlider(
                            label = stringResource(R.string.exercise_label),
                            value = globalExerciseDuration,
                            onValueChange = { newVal ->
                                globalExerciseDuration = newVal
                                exercises = exercises.map { ex ->
                                    if (!ex.overrideDefaults) ex.copy(exerciseDurationSeconds = newVal)
                                    else ex
                                }
                                onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                            },
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DurationSlider(
                            label = stringResource(R.string.rest_label),
                            value = globalRestDuration,
                            onValueChange = { newVal ->
                                globalRestDuration = newVal
                                exercises = exercises.map { ex ->
                                    if (!ex.overrideDefaults) ex.copy(restDurationSeconds = newVal)
                                    else ex
                                }
                                onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                            },
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.exercises_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showAiChat = true }
                        ) {
                            Icon(imageVector = AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.ai_edit_desc))
                        }
                        OutlinedButton(
                            onClick = { showAddDialog = true }
                        ) {
                            Icon(imageVector = Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.add))
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.repeat_count),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        OutlinedButton(onClick = { showRepsMenu = true }) {
                            Text("$repeatCount")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        DropdownMenu(
                            expanded = showRepsMenu,
                            onDismissRequest = { showRepsMenu = false },
                        ) {
                            (1..10).forEach { num ->
                                DropdownMenuItem(
                                    text = { Text("$num") },
                                    onClick = {
                                        repeatCount = num
                                        showRepsMenu = false
                                    },
                                )
                            }
                        }
                    }
                }
            }

            if (exercises.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.no_exercises_yet),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = stringResource(R.string.tap_add_exercise),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        )
                    }
                }
            }

            itemsIndexed(exercises) { index, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    globalExerciseDuration = globalExerciseDuration,
                    globalRestDuration = globalRestDuration,
                    onRenameExercise = {
                        renameText = exercise.name
                        renameExerciseId = exercise.id
                        showRenameDialog = true
                    },
                    onToggleOverride = { overrideOn ->
                        exercises = exercises.toMutableList().apply {
                            set(
                                index, if (overrideOn) {
                                    exercise.copy(
                                        overrideDefaults = true,
                                        exerciseDurationSeconds = globalExerciseDuration,
                                        restDurationSeconds = globalRestDuration,
                                    )
                                } else {
                                    exercise.copy(
                                        overrideDefaults = false,
                                        exerciseDurationSeconds = globalExerciseDuration,
                                        restDurationSeconds = globalRestDuration,
                                    )
                                }
                            )
                        }
                        onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                    },
                    onRepsChange = { newReps ->
                        exercises = exercises.toMutableList().apply {
                            set(index, exercise.copy(reps = newReps))
                        }
                        onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                    },
                    onExerciseDurationChange = { newDuration ->
                        exercises = exercises.toMutableList().apply {
                            set(index, exercise.copy(exerciseDurationSeconds = newDuration))
                        }
                        onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                    },
                    onRestDurationChange = { newDuration ->
                        exercises = exercises.toMutableList().apply {
                            set(index, exercise.copy(restDurationSeconds = newDuration))
                        }
                        onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                    },
                    onDelete = {
                        exercises = exercises.toMutableList().apply { removeAt(index) }
                        onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
                    },
                )
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    globalExerciseDuration: Int,
    globalRestDuration: Int,
    onRenameExercise: () -> Unit,
    onToggleOverride: (Boolean) -> Unit,
    onRepsChange: (Int) -> Unit,
    onExerciseDurationChange: (Int) -> Unit,
    onRestDurationChange: (Int) -> Unit,
    onDelete: () -> Unit,
) {
    var showRepsMenu by rememberSaveable { mutableStateOf(false) }

    val displayExerciseDuration =
        if (exercise.overrideDefaults) exercise.exerciseDurationSeconds else globalExerciseDuration
    val displayRestDuration =
        if (exercise.overrideDefaults) exercise.restDurationSeconds else globalRestDuration

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleOverride(!exercise.overrideDefaults) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f, fill = false),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onRenameExercise,
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = Edit,
                                contentDescription = stringResource(R.string.rename_desc),
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.reps_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box {
                            OutlinedButton(
                                onClick = { showRepsMenu = true },
                                modifier = Modifier.height(28.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            ) {
                                Text(
                                    text = "${exercise.reps}",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                            DropdownMenu(
                                expanded = showRepsMenu,
                                onDismissRequest = { showRepsMenu = false },
                            ) {
                                (1..10).forEach { num ->
                                    DropdownMenuItem(
                                        text = { Text("$num") },
                                        onClick = {
                                            onRepsChange(num)
                                            showRepsMenu = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = stringResource(R.string.exercise_rest_format, displayExerciseDuration, displayRestDuration),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Switch(
                            checked = exercise.overrideDefaults,
                            onCheckedChange = { onToggleOverride(it) },
                            thumbContent = {
                                if (exercise.overrideDefaults) {
                                    Icon(
                                        imageVector = Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                } else {
                                    Icon(
                                        imageVector = Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            },
                        )
                        Text(
                            text = stringResource(R.string.override_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Delete,
                                contentDescription = stringResource(R.string.delete_exercise_desc),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }

            AnimatedVisibility(visible = exercise.overrideDefaults) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                        DurationSlider(
                            label = stringResource(R.string.exercise_label),
                            value = exercise.exerciseDurationSeconds,
                            onValueChange = onExerciseDurationChange,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DurationSlider(
                            label = stringResource(R.string.rest_label),
                            value = exercise.restDurationSeconds,
                            onValueChange = onRestDurationChange,
                        )
                }
            }
        }
    }
}

@Composable
private fun DurationSlider(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "${value}s",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange((it / 5f).roundToInt() * 5) },
            valueRange = 5f..120f,
            steps = 22,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
