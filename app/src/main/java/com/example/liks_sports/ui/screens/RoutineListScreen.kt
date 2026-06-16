package com.example.liks_sports.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.liks_sports.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.liks_sports.data.Routine
import com.example.liks_sports.ui.icons.Add
import com.example.liks_sports.ui.icons.Delete
import com.example.liks_sports.ui.icons.Edit
import com.example.liks_sports.ui.icons.FitnessCenter
import com.example.liks_sports.ui.icons.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    routines: List<Routine>,
    onCreateRoutine: (String) -> Unit,
    onRenameRoutine: (String, String) -> Unit,
    onRoutineClick: (String) -> Unit,
    onDeleteRoutine: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var createName by remember { mutableStateOf("") }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameId by remember { mutableStateOf("") }
    var renameText by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf("") }
    var deleteName by remember { mutableStateOf("") }

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
                TextButton(
                    onClick = {
                        onDeleteRoutine(deleteId)
                        showDeleteDialog = false
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
                    SettingsIconButton(onClick = onOpenSettings)
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
        }
    ) { padding ->
        if (routines.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(routines, key = { it.id }) { routine ->
                    Card(
                        onClick = { onRoutineClick(routine.id) },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = FitnessCenter,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
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
                                        onClick = {
                                            renameId = routine.id
                                            renameText = routine.name
                                            showRenameDialog = true
                                        },
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
                            }
                            IconButton(onClick = {
                                deleteId = routine.id
                                deleteName = routine.name
                                showDeleteDialog = true
                            }) {
                                Icon(
                                    imageVector = Delete,
                                    contentDescription = stringResource(R.string.delete_routine_desc),
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
