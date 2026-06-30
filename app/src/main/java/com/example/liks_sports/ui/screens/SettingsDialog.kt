package com.example.liks_sports.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.liks_sports.R
import com.example.liks_sports.data.DownloadState
import com.example.liks_sports.data.SettingsStore
import com.example.liks_sports.ui.icons.AutoAwesome
import com.example.liks_sports.ui.icons.CheckCircle
import com.example.liks_sports.ui.icons.Cloud
import com.example.liks_sports.ui.icons.Close
import com.example.liks_sports.ui.icons.Database
import com.example.liks_sports.ui.icons.Delete
import com.example.liks_sports.ui.icons.Download
import com.example.liks_sports.ui.icons.ExpandLess
import com.example.liks_sports.ui.icons.ExpandMore
import com.example.liks_sports.ui.icons.Memory
import com.example.liks_sports.ui.icons.Refresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun SettingsDialog(
    store: SettingsStore,
    hasDismissedDefaults: Boolean = false,
    modelPresent: Boolean,
    downloadState: DownloadState,
    onRestoreDefaults: () -> Unit = {},
    onStartDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onDeleteModel: () -> Unit,
    onDismiss: () -> Unit,
) {
    var useCloud by remember { mutableStateOf(store.useCloudModel) }
    var apiUrl by remember { mutableStateOf(store.apiUrl) }
    var apiKey by remember { mutableStateOf(store.apiKey) }
    var modelId by remember { mutableStateOf(store.modelId) }
    var backend by remember { mutableStateOf(store.localBackend) }
    val scope = rememberCoroutineScope()

    var models by remember { mutableStateOf<List<String>>(emptyList()) }
    var modelsLoading by remember { mutableStateOf(false) }
    var modelsError by remember { mutableStateOf<String?>(null) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    var useCustomModel by remember { mutableStateOf(store.modelId.isNotBlank() && store.modelId !in models) }

    var testResult by remember { mutableStateOf<TestResult?>(null) }
    var testing by remember { mutableStateOf(false) }

    val modelsLoadFailedMsg = stringResource(R.string.models_load_failed)
    val invalidUrlMsg = stringResource(R.string.api_url_invalid)

    LaunchedEffect(useCloud, apiUrl, apiKey) {
        if (useCloud && apiUrl.isNotBlank() && apiKey.isNotBlank() && models.isEmpty()) {
            delay(400L)
            if (!SettingsStore.isValidApiUrl(apiUrl)) {
                modelsError = invalidUrlMsg
                return@LaunchedEffect
            }
            modelsLoading = true
            modelsError = null
            val result = withContext(Dispatchers.IO) { fetchModels(apiUrl.trimEnd('/'), apiKey) }
            result.onSuccess { list ->
                models = list
                useCustomModel = modelId.isNotBlank() && modelId !in list
            }.onFailure { e ->
                modelsError = e.message ?: modelsLoadFailedMsg
            }
            modelsLoading = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .heightIn(max = 620.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SettingsHeader(onDismiss = onDismiss)

                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    SectionLabel(text = stringResource(R.string.ai_mode_section))
                    Spacer(modifier = Modifier.height(8.dp))
                    ModeCard(
                        icon = Database,
                        title = stringResource(R.string.ai_mode_on_device),
                        description = stringResource(R.string.ai_mode_on_device_desc),
                        selected = !useCloud,
                        onClick = { useCloud = false },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ModeCard(
                        icon = Cloud,
                        title = stringResource(R.string.ai_mode_cloud),
                        description = stringResource(R.string.ai_mode_cloud_desc),
                        selected = useCloud,
                        onClick = { useCloud = true },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (useCloud) {
                        CloudModelSection(
                            apiUrl = apiUrl,
                            onApiUrlChange = { apiUrl = it },
                            apiKey = apiKey,
                            onApiKeyChange = { apiKey = it },
                            modelId = modelId,
                            onModelIdChange = { modelId = it },
                            models = models,
                            modelsLoading = modelsLoading,
                            modelsError = modelsError,
                            modelDropdownExpanded = modelDropdownExpanded,
                            onModelDropdownExpandedChange = { modelDropdownExpanded = it },
                            useCustomModel = useCustomModel,
                            onUseCustomModelChange = { useCustomModel = it },
                            onRefreshModels = {
                                if (apiUrl.isBlank() || apiKey.isBlank()) return@CloudModelSection
                                if (!SettingsStore.isValidApiUrl(apiUrl)) {
                                    modelsError = invalidUrlMsg
                                    return@CloudModelSection
                                }
                                scope.launch {
                                    modelsLoading = true
                                    modelsError = null
                                    val result = withContext(Dispatchers.IO) { fetchModels(apiUrl.trimEnd('/'), apiKey) }
                                    result.onSuccess { list ->
                                        models = list
                                        useCustomModel = modelId.isNotBlank() && modelId !in list
                                    }.onFailure { e ->
                                        modelsError = e.message ?: modelsLoadFailedMsg
                                    }
                                    modelsLoading = false
                                }
                            },
                            testResult = testResult,
                            testing = testing,
                            onTest = {
                                if (apiUrl.isBlank() || apiKey.isBlank()) return@CloudModelSection
                                if (!SettingsStore.isValidApiUrl(apiUrl)) {
                                    testResult = TestResult.Error(invalidUrlMsg)
                                    return@CloudModelSection
                                }
                                testing = true
                                testResult = null
                                scope.launch {
                                    testResult = withContext(Dispatchers.IO) {
                                        testConnection(apiUrl.trimEnd('/'), apiKey)
                                    }
                                    testing = false
                                }
                            },
                        )
                    } else {
                        LocalModelSection(
                            modelPresent = modelPresent,
                            downloadState = downloadState,
                            backend = backend,
                            onBackendChange = { backend = it },
                            onDownload = onStartDownload,
                            onCancelDownload = onCancelDownload,
                            onDelete = onDeleteModel,
                        )
                    }

                    if (hasDismissedDefaults) {
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionLabel(text = stringResource(R.string.default_routines_section))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onRestoreDefaults,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.restore_defaults))
                        }
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (useCloud && !SettingsStore.isValidApiUrl(apiUrl)) {
                                testResult = TestResult.Error(invalidUrlMsg)
                                return@Button
                            }
                            store.useCloudModel = useCloud
                            if (useCloud) {
                                store.saveCloud(apiUrl.trim(), apiKey.trim(), modelId.trim())
                            } else {
                                store.saveLocal(backend)
                            }
                            onDismiss()
                        },
                        // Require a complete cloud config (url + key + model)
                        // before saving, otherwise the chat silently does
                        // nothing ("v1 endpoint + key, nothing happens").
                        enabled = !useCloud ||
                            (apiUrl.isNotBlank() && apiKey.isNotBlank() && modelId.isNotBlank()),
                    ) { Text(stringResource(R.string.save)) }
                }
            }
        }
    }
}

@Composable
private fun SettingsHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.settings_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Close,
                contentDescription = stringResource(R.string.settings_close_desc),
            )
        }
    }
}

@Composable
private fun LocalModelSection(
    modelPresent: Boolean,
    downloadState: DownloadState,
    backend: String,
    onBackendChange: (String) -> Unit,
    onDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onDelete: () -> Unit,
) {
    val downloading = downloadState is DownloadState.Downloading
    val downloadProgress = (downloadState as? DownloadState.Downloading)?.progress ?: 0
    val downloadError = (downloadState as? DownloadState.Error)?.message

    SectionLabel(text = stringResource(R.string.on_device_model_section))
    Spacer(modifier = Modifier.height(8.dp))
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Database,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.local_model_default_name),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Text(
                        text = stringResource(R.string.local_model_size, "2.58 GB"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                    )
                }
                StatusChip(
                    ready = modelPresent,
                    downloading = downloading,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (downloading) {
                LinearProgressIndicator(
                    progress = { downloadProgress / 100f },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.local_model_downloading, downloadProgress),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            if (downloadError != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.local_model_download_failed, downloadError),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (downloading) {
                    OutlinedButton(
                        onClick = onCancelDownload,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.local_model_cancel_download))
                    }
                } else if (!modelPresent) {
                    OutlinedButton(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(imageVector = Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.local_model_download))
                    }
                } else {
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(imageVector = Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.local_model_delete))
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Memory,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.backend_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.weight(1f))
        FilterChip(
            selected = backend == SettingsStore.BACKEND_GPU,
            onClick = { onBackendChange(SettingsStore.BACKEND_GPU) },
            label = { Text(stringResource(R.string.backend_gpu)) },
        )
        Spacer(modifier = Modifier.width(6.dp))
        FilterChip(
            selected = backend == SettingsStore.BACKEND_CPU,
            onClick = { onBackendChange(SettingsStore.BACKEND_CPU) },
            label = { Text(stringResource(R.string.backend_cpu)) },
        )
    }
}

@Composable
private fun StatusChip(ready: Boolean, downloading: Boolean) {
    val (text, color, onColor) = when {
        downloading -> Triple(
            stringResource(R.string.local_model_status_downloading),
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
        )
        ready -> Triple(
            stringResource(R.string.local_model_ready),
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
        )
        else -> Triple(
            stringResource(R.string.local_model_not_downloaded),
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    Surface(color = color, shape = MaterialTheme.shapes.small) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = onColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun CloudModelSection(
    apiUrl: String,
    onApiUrlChange: (String) -> Unit,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    modelId: String,
    onModelIdChange: (String) -> Unit,
    models: List<String>,
    modelsLoading: Boolean,
    modelsError: String?,
    modelDropdownExpanded: Boolean,
    onModelDropdownExpandedChange: (Boolean) -> Unit,
    useCustomModel: Boolean,
    onUseCustomModelChange: (Boolean) -> Unit,
    onRefreshModels: () -> Unit,
    testResult: TestResult?,
    testing: Boolean,
    onTest: () -> Unit,
) {
    SectionLabel(text = stringResource(R.string.cloud_model_section))
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = apiUrl,
        onValueChange = onApiUrlChange,
        label = { Text(stringResource(R.string.api_url_label)) },
        supportingText = { Text(stringResource(R.string.api_url_helper)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = apiKey,
        onValueChange = onApiKeyChange,
        label = { Text(stringResource(R.string.api_key_label)) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(12.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.cloud_model_dropdown_label),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onRefreshModels, enabled = !modelsLoading) {
            if (modelsLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Icon(
                    imageVector = Refresh,
                    contentDescription = stringResource(R.string.refresh_models_desc),
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = if (useCustomModel) stringResource(R.string.cloud_model_custom)
                else if (modelId.isBlank()) stringResource(R.string.cloud_model_dropdown_hint)
                else modelId,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = if (modelDropdownExpanded) ExpandLess else ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onModelDropdownExpandedChange(!modelDropdownExpanded) },
        )
        DropdownMenu(
            expanded = modelDropdownExpanded,
            onDismissRequest = { onModelDropdownExpandedChange(false) },
        ) {
            if (modelsLoading) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.models_loading)) },
                    onClick = {},
                    enabled = false,
                )
            } else if (models.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.cloud_model_empty)) },
                    onClick = {},
                    enabled = false,
                )
            }
            models.forEach { id ->
                DropdownMenuItem(
                    text = { Text(id) },
                    onClick = {
                        onModelIdChange(id)
                        onUseCustomModelChange(false)
                        onModelDropdownExpandedChange(false)
                    },
                )
            }
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.cloud_model_custom)) },
                onClick = {
                    onUseCustomModelChange(true)
                    onModelDropdownExpandedChange(false)
                },
            )
        }
    }
    if (useCustomModel) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = modelId,
            onValueChange = onModelIdChange,
            label = { Text(stringResource(R.string.cloud_model_custom_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    if (modelsError != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = modelsError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedButton(
        onClick = onTest,
        enabled = !testing && apiUrl.isNotBlank() && apiKey.isNotBlank(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (testing) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.ai_testing))
        } else {
            Text(stringResource(R.string.ai_test_connection))
        }
    }
    if (testResult != null) {
        Spacer(modifier = Modifier.height(10.dp))
        TestResultRow(result = testResult!!)
    }
}

@Composable
private fun TestResultRow(result: TestResult) {
    val isSuccess = result is TestResult.Success
    val container = if (isSuccess) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.errorContainer
    val onContainer = if (isSuccess) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onErrorContainer
    val (text, icon) = when (result) {
        is TestResult.Success -> stringResource(R.string.test_success) to CheckCircle
        is TestResult.Auth -> stringResource(R.string.test_auth_failed) to Close
        is TestResult.Http -> stringResource(R.string.test_http_error, result.code) to Close
        is TestResult.Error -> stringResource(R.string.test_failed, result.message) to Close
    }
    Surface(color = container, shape = MaterialTheme.shapes.small) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = onContainer,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = onContainer,
            )
        }
    }
}

private sealed interface TestResult {
    data object Success : TestResult
    data object Auth : TestResult
    data class Http(val code: Int) : TestResult
    data class Error(val message: String) : TestResult
}

private fun openModelsConnection(baseUrl: String, apiKey: String): HttpURLConnection {
    val url = URL("$baseUrl/models")
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Authorization", "Bearer $apiKey")
    connection.connectTimeout = 10_000
    connection.readTimeout = 10_000
    return connection
}

private fun fetchModels(baseUrl: String, apiKey: String): Result<List<String>> = runCatching {
    val connection = openModelsConnection(baseUrl, apiKey)
    try {
        val code = connection.responseCode
        if (code != HttpURLConnection.HTTP_OK) {
            throw RuntimeException("HTTP $code")
        }
        val body = connection.inputStream.bufferedReader(Charsets.UTF_8).readText()
        val json = JSONObject(body)
        val data = json.optJSONArray("data") ?: return@runCatching emptyList()
        (0 until data.length()).mapNotNull { i ->
            val obj = data.optJSONObject(i) ?: return@mapNotNull null
            obj.optString("id", "")
        }.filter { it.isNotBlank() }
    } finally {
        connection.disconnect()
    }
}

private fun testConnection(baseUrl: String, apiKey: String): TestResult {
    return try {
        val connection = openModelsConnection(baseUrl, apiKey)
        try {
            val code = connection.responseCode
            when {
                code == HttpURLConnection.HTTP_OK -> TestResult.Success
                code == 401 || code == 403 -> TestResult.Auth
                else -> TestResult.Http(code)
            }
        } finally {
            connection.disconnect()
        }
    } catch (e: Exception) {
        TestResult.Error(e.message ?: "")
    }
}
