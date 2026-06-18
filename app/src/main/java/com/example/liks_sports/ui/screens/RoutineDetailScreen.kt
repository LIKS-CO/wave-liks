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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.liks_sports.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.liks_sports.data.Exercise
import com.example.liks_sports.data.Routine
import com.example.liks_sports.ui.icons.Add
import com.example.liks_sports.ui.icons.ArrowBack
import com.example.liks_sports.ui.icons.ArrowDropDown
import com.example.liks_sports.ui.icons.ArrowDropUp
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
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt

private val ExercisesSaver = Saver<List<Exercise>, String>(
    save = { list ->
        val arr = JSONArray()
        for (e in list) {
            arr.put(JSONObject().apply {
                put("id", e.id)
                put("name", e.name)
                put("reps", e.reps)
                put("exerciseDurationSeconds", e.exerciseDurationSeconds)
                put("restDurationSeconds", e.restDurationSeconds)
                put("overrideDefaults", e.overrideDefaults)
            })
        }
        arr.toString()
    },
    restore = { str ->
        try {
            val arr = JSONArray(str)
            (0 until arr.length()).map { i ->
                val e = arr.getJSONObject(i)
                Exercise(
                    id = e.getString("id"),
                    name = e.getString("name"),
                    reps = e.getInt("reps"),
                    exerciseDurationSeconds = e.getInt("exerciseDurationSeconds"),
                    restDurationSeconds = e.getInt("restDurationSeconds"),
                    overrideDefaults = e.getBoolean("overrideDefaults"),
                )
            }
        } catch (_: Exception) {
            null
        }
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routine: Routine,
    onUpdateRoutine: (Routine) -> Unit,
    onBack: () -> Unit,
    onStart: (repeatCount: Int) -> Unit,
    settingsStore: SettingsStore,
    chatHistoryStore: ChatHistoryStore,
    localEngine: com.example.liks_sports.data.LocalLlmEngine,
    onOpenSettings: () -> Unit,
) {
    var routineName by rememberSaveable(routine.id) { mutableStateOf(routine.name) }
    val savedExercises: List<Exercise> = rememberSaveable(routine.id, saver = ExercisesSaver) {
        routine.exercises
    }
    var exercises by remember(savedExercises) { mutableStateOf(savedExercises) }
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
    var globalExerciseDuration by rememberSaveable(routine.id) {
        mutableIntStateOf(initialDefaults.first)
    }
    var globalRestDuration by rememberSaveable(routine.id) {
        mutableIntStateOf(initialDefaults.second)
    }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newExerciseName by rememberSaveable { mutableStateOf("") }
    var showRenameDialog by rememberSaveable { mutableStateOf(false) }
    var renameText by rememberSaveable { mutableStateOf("") }
    var renameExerciseId by rememberSaveable { mutableStateOf("") }
    var repeatCount by rememberSaveable { mutableStateOf(1) }
    var showRepsMenu by rememberSaveable { mutableStateOf(false) }
    var showAiChat by rememberSaveable { mutableStateOf(false) }
    val chatHistoryState = remember { mutableStateOf(chatHistoryStore.getMessages(routine.id)) }

    var saveCounter by rememberSaveable { mutableIntStateOf(0) }
    var dirty by remember { mutableStateOf(false) }

    fun scheduleSave() {
        dirty = true
        saveCounter++
    }

    fun flush() {
        if (dirty) {
            onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
            dirty = false
        }
    }

    LaunchedEffect(saveCounter) {
        if (saveCounter == 0) return@LaunchedEffect
        delay(1000L)
        onUpdateRoutine(routine.copy(name = routineName, exercises = exercises))
        dirty = false
    }

    DisposableEffect(Unit) {
        onDispose { flush() }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    var pendingDeletedExercise by remember { mutableStateOf<Triple<Int, Exercise, String>?>(null) }
    val undoLabel = stringResource(R.string.undo)

    LaunchedEffect(pendingDeletedExercise) {
        val (idx, ex, msg) = pendingDeletedExercise ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = msg,
            actionLabel = undoLabel,
            duration = SnackbarDuration.Short,
        )
        if (result == SnackbarResult.ActionPerformed) {
            exercises = exercises.toMutableList().apply { add(idx.coerceAtMost(size), ex) }
            scheduleSave()
        }
        pendingDeletedExercise = null
    }

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
                            scheduleSave()
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
                            scheduleSave()
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
            localEngine = localEngine,
            chatHistory = chatHistoryState.value,
            onSendMessage = { msg ->
                chatHistoryStore.addMessage(routine.id, ChatMessage("user", msg))
                chatHistoryState.value = chatHistoryStore.getMessages(routine.id)
            },
            onApplyEdits = { edited ->
                exercises = edited.exercises
                routineName = edited.name
                val nonOverridden = edited.exercises.filter { !it.overrideDefaults }
                if (nonOverridden.isNotEmpty()) {
                    globalExerciseDuration = nonOverridden.groupingBy { it.exerciseDurationSeconds }
                        .eachCount().maxByOrNull { it.value }!!.key.coerceIn(5, 120)
                    globalRestDuration = nonOverridden.groupingBy { it.restDurationSeconds }
                        .eachCount().maxByOrNull { it.value }!!.key.coerceIn(5, 120)
                }
                scheduleSave()
            },
            onAiResponded = { text ->
                chatHistoryStore.addMessage(routine.id, ChatMessage("assistant", text))
                chatHistoryState.value = chatHistoryStore.getMessages(routine.id)
            },
            onClearSession = {
                chatHistoryStore.clear(routine.id)
                chatHistoryState.value = emptyList()
            },
            onOpenSettings = onOpenSettings,
            onDismiss = { showAiChat = false },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routineName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = ArrowBack, contentDescription = stringResource(R.string.back_desc))
                    }
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
                    flush()
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                            contentDesc = stringResource(R.string.exercise_duration_desc),
                            value = globalExerciseDuration,
                            onValueChange = { newVal ->
                                globalExerciseDuration = newVal
                                exercises = exercises.map { ex ->
                                    if (!ex.overrideDefaults) ex.copy(exerciseDurationSeconds = newVal)
                                    else ex
                                }
                                scheduleSave()
                            },
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DurationSlider(
                            label = stringResource(R.string.rest_label),
                            contentDesc = stringResource(R.string.rest_duration_desc),
                            value = globalRestDuration,
                            onValueChange = { newVal ->
                                globalRestDuration = newVal
                                exercises = exercises.map { ex ->
                                    if (!ex.overrideDefaults) ex.copy(restDurationSeconds = newVal)
                                    else ex
                                }
                                scheduleSave()
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

            itemsIndexed(exercises, key = { _, ex -> ex.id }) { index, exercise ->
                val deletedMsg = stringResource(R.string.exercise_deleted, exercise.name)
                ExerciseCard(
                    exercise = exercise,
                    index = index,
                    totalCount = exercises.size,
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
                                index,
                                exercise.copy(
                                    overrideDefaults = overrideOn,
                                    exerciseDurationSeconds = globalExerciseDuration,
                                    restDurationSeconds = globalRestDuration,
                                )
                            )
                        }
                        scheduleSave()
                    },
                    onRepsChange = { newReps ->
                        exercises = exercises.toMutableList().apply {
                            set(index, exercise.copy(reps = newReps))
                        }
                        scheduleSave()
                    },
                    onExerciseDurationChange = { newDuration ->
                        exercises = exercises.toMutableList().apply {
                            set(index, exercise.copy(exerciseDurationSeconds = newDuration))
                        }
                        scheduleSave()
                    },
                    onRestDurationChange = { newDuration ->
                        exercises = exercises.toMutableList().apply {
                            set(index, exercise.copy(restDurationSeconds = newDuration))
                        }
                        scheduleSave()
                    },
                    onDelete = {
                        pendingDeletedExercise = Triple(index, exercise, deletedMsg)
                        exercises = exercises.toMutableList().apply { removeAt(index) }
                    },
                    onMoveUp = if (index > 0) {
                        {
                            exercises = exercises.toMutableList().apply {
                                add(index - 1, removeAt(index))
                            }
                            scheduleSave()
                        }
                    } else null,
                    onMoveDown = if (index < exercises.size - 1) {
                        {
                            exercises = exercises.toMutableList().apply {
                                add(index + 1, removeAt(index))
                            }
                            scheduleSave()
                        }
                    } else null,
                )
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    index: Int,
    totalCount: Int,
    globalExerciseDuration: Int,
    globalRestDuration: Int,
    onRenameExercise: () -> Unit,
    onToggleOverride: (Boolean) -> Unit,
    onRepsChange: (Int) -> Unit,
    onExerciseDurationChange: (Int) -> Unit,
    onRestDurationChange: (Int) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: (() -> Unit)?,
    onMoveDown: (() -> Unit)?,
) {
    var showRepsMenu by rememberSaveable { mutableStateOf(false) }

    val displayExerciseDuration =
        if (exercise.overrideDefaults) exercise.exerciseDurationSeconds else globalExerciseDuration
    val displayRestDuration =
        if (exercise.overrideDefaults) exercise.restDurationSeconds else globalRestDuration

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleOverride(!exercise.overrideDefaults) },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.wrapContentWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onRenameExercise, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Edit,
                        contentDescription = stringResource(R.string.rename_desc),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Delete,
                        contentDescription = stringResource(R.string.delete_exercise_desc),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
                IconButton(
                    onClick = { onMoveUp?.invoke() },
                    modifier = Modifier.size(40.dp),
                    enabled = onMoveUp != null,
                ) {
                    Icon(
                        imageVector = ArrowDropUp,
                        contentDescription = stringResource(R.string.move_up_desc),
                        modifier = Modifier.size(24.dp),
                        tint = if (onMoveUp != null) MaterialTheme.colorScheme.onSurfaceVariant
                               else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                    )
                }
                IconButton(
                    onClick = { onMoveDown?.invoke() },
                    modifier = Modifier.size(40.dp),
                    enabled = onMoveDown != null,
                ) {
                    Icon(
                        imageVector = ArrowDropDown,
                        contentDescription = stringResource(R.string.move_down_desc),
                        modifier = Modifier.size(24.dp),
                        tint = if (onMoveDown != null) MaterialTheme.colorScheme.onSurfaceVariant
                               else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.reps_label),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box {
                    OutlinedButton(
                        onClick = { showRepsMenu = true },
                        modifier = Modifier.height(36.dp),
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
                Text(
                    text = stringResource(R.string.exercise_rest_format, displayExerciseDuration, displayRestDuration),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 16.dp, bottom = 8.dp),
                ) {
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
            }

            AnimatedVisibility(visible = exercise.overrideDefaults) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)) {
                    DurationSlider(
                        label = stringResource(R.string.exercise_label),
                        contentDesc = stringResource(R.string.exercise_duration_desc),
                        value = exercise.exerciseDurationSeconds,
                        onValueChange = onExerciseDurationChange,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DurationSlider(
                        label = stringResource(R.string.rest_label),
                        contentDesc = stringResource(R.string.rest_duration_desc),
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
    contentDesc: String,
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
            modifier = Modifier
                .fillMaxWidth()
                .semantics { this.contentDescription = contentDesc },
        )
    }
}
