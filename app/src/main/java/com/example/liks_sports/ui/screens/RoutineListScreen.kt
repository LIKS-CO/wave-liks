package com.example.liks_sports.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.liks_sports.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.liks_sports.data.Routine
import com.example.liks_sports.data.WorkoutSession
import com.example.liks_sports.data.WorkoutStats
import com.example.liks_sports.ui.icons.Add
import com.example.liks_sports.ui.icons.Delete
import com.example.liks_sports.ui.icons.Edit
import com.example.liks_sports.ui.icons.FitnessCenter
import com.example.liks_sports.ui.icons.Settings
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    routines: List<Routine>,
    workoutStats: WorkoutStats,
    lastSessionForRoutine: Map<String, WorkoutSession>,
    routineDoneCounts: Map<String, Int>,
    onCreateRoutine: (String) -> Unit,
    onRenameRoutine: (String, String) -> Unit,
    onRoutineClick: (String) -> Unit,
    onDeleteRoutine: (String) -> Unit,
    onUndoDeleteRoutine: (Routine) -> Unit,
    onOpenGeneralSettings: () -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var createName by remember { mutableStateOf("") }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameId by remember { mutableStateOf("") }
    var renameText by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf("") }
    var deleteName by remember { mutableStateOf("") }
    var pendingDeleteRoutine by remember { mutableStateOf<Pair<Routine, String>?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val undoLabel = stringResource(R.string.undo)

    LaunchedEffect(pendingDeleteRoutine) {
        val (routine, msg) = pendingDeleteRoutine ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = msg,
            actionLabel = undoLabel,
            duration = SnackbarDuration.Short,
        )
        if (result == SnackbarResult.ActionPerformed) {
            onUndoDeleteRoutine(routine)
        }
        pendingDeleteRoutine = null
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                createName = ""
            },
            title = { Text(stringResource(R.string.new_routine)) },
            text = {
                OutlinedTextField(
                    value = createName,
                    onValueChange = { createName = it },
                    label = { Text(stringResource(R.string.routine_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (createName.isNotBlank()) {
                            onCreateRoutine(createName.trim())
                            showCreateDialog = false
                            createName = ""
                        }
                    }
                ) { Text(stringResource(R.string.create)) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                        createName = ""
                    }
                ) { Text(stringResource(R.string.cancel)) }
            },
        )
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = {
                showRenameDialog = false
                renameText = ""
                renameId = ""
            },
            title = { Text(stringResource(R.string.rename_routine)) },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text(stringResource(R.string.routine_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (renameText.isNotBlank()) {
                            onRenameRoutine(renameId, renameText.trim())
                            showRenameDialog = false
                            renameText = ""
                            renameId = ""
                        }
                    }
                ) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRenameDialog = false
                        renameText = ""
                        renameId = ""
                    }
                ) { Text(stringResource(R.string.cancel)) }
            },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                deleteId = ""
                deleteName = ""
            },
            title = { Text(stringResource(R.string.delete_routine_title)) },
            text = { Text(stringResource(R.string.delete_routine_confirm, deleteName)) },
            confirmButton = {
                val deletedRoutine = routines.find { it.id == deleteId }
                val msg = deletedRoutine?.let { stringResource(R.string.routine_deleted, it.name) } ?: ""
                TextButton(
                    onClick = {
                        onDeleteRoutine(deleteId)
                        showDeleteDialog = false
                        pendingDeleteRoutine = deletedRoutine?.let { it to msg }
                        deleteId = ""
                        deleteName = ""
                    }
                ) { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deleteId = ""
                        deleteName = ""
                    }
                ) { Text(stringResource(R.string.cancel)) }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.routine_list_title)) },
                actions = {
                    IconButton(onClick = onOpenGeneralSettings) {
                        Icon(
                            imageVector = Settings,
                            contentDescription = stringResource(R.string.general_settings_desc),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                createName = ""
                showCreateDialog = true
            }) {
                Icon(imageVector = Add, contentDescription = stringResource(R.string.add_routine_desc))
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
            item { DashboardHeader(workoutStats) }
            if (routines.isEmpty()) {
                item { NoRoutinesContent() }
            } else {
                items(routines, key = { it.id }) { routine ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                deleteId = routine.id
                                deleteName = routine.name
                                showDeleteDialog = true
                                false
                            } else {
                                false
                            }
                        }
                    )
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                                label = "bg",
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Icon(
                                    imageVector = Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                    ) {
                        RoutineCard(
                            routine = routine,
                            lastSession = lastSessionForRoutine[routine.id],
                            doneCount = routineDoneCounts[routine.id] ?: 0,
                            onRoutineClick = onRoutineClick,
                            onRenameRoutine = { r ->
                                renameId = r.id
                                renameText = r.name
                                showRenameDialog = true
                            },
                            onDeleteRoutine = { r ->
                                deleteId = r.id
                                deleteName = r.name
                                showDeleteDialog = true
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoRoutinesContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = FitnessCenter,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = stringResource(R.string.no_routines_yet),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.tap_to_create),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun RoutineCard(
    routine: Routine,
    lastSession: WorkoutSession?,
    doneCount: Int,
    onRoutineClick: (String) -> Unit,
    onRenameRoutine: (Routine) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
) {
    Card(
        onClick = { onRoutineClick(routine.id) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { onRenameRoutine(routine) },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = Edit,
                            contentDescription = stringResource(R.string.rename_routine_desc),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                val totalSecs = routine.totalDurationSeconds
                Text(
                    text = stringResource(
                        R.string.exercises_with_time,
                        routine.exercises.size,
                        stringResource(R.string.total_time, "%d:%02d".format(totalSecs / 60, totalSecs % 60)),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (lastSession != null || doneCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (lastSession != null) {
                            BadgeChip(
                                text = stringResource(
                                    R.string.dashboard_last_done,
                                    relativeDayLabel(lastSession.dateEpochDay),
                                ),
                            )
                        }
                        if (doneCount > 0) {
                            BadgeChip(text = stringResource(R.string.dashboard_done_count, doneCount))
                        }
                    }
                }
            }
            IconButton(onClick = { onDeleteRoutine(routine) }) {
                Icon(
                    imageVector = Delete,
                    contentDescription = stringResource(R.string.delete_routine_desc),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun BadgeChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun relativeDayLabel(epochDay: Long): String {
    val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
    val diff = today - epochDay
    return when {
        diff <= 0 -> stringResource(R.string.dashboard_today)
        diff == 1L -> stringResource(R.string.dashboard_yesterday)
        else -> stringResource(R.string.dashboard_days_ago, diff.toInt())
    }
}
