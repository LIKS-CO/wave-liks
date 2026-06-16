package com.example.liks_sports.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.liks_sports.R
import com.example.liks_sports.data.SettingsStore
import com.example.liks_sports.ui.icons.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun SettingsDialog(
    store: SettingsStore,
    onDismiss: () -> Unit,
) {
    var apiUrl by remember { mutableStateOf(store.apiUrl) }
    var apiKey by remember { mutableStateOf(store.apiKey) }
    var modelId by remember { mutableStateOf(store.modelId) }
    var testResult by remember { mutableStateOf<String?>(null) }
    var testing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = apiUrl,
                    onValueChange = { apiUrl = it },
                    label = { Text(stringResource(R.string.api_url_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text(stringResource(R.string.api_key_label)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = modelId,
                    onValueChange = { modelId = it },
                    label = { Text(stringResource(R.string.model_id_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        if (apiUrl.isBlank() || apiKey.isBlank()) return@OutlinedButton
                        testing = true
                        testResult = null
                        scope.launch {
                            testResult = withContext(Dispatchers.IO) {
                                testConnection(apiUrl.trimEnd('/'), apiKey)
                            }
                            testing = false
                        }
                    },
                    enabled = !testing && apiUrl.isNotBlank() && apiKey.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (testing) {
                        CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp).let { Modifier.height(16.dp) }, strokeWidth = 2.dp)
                        Text(stringResource(R.string.ai_testing))
                    } else {
                        Text(stringResource(R.string.ai_test_connection))
                    }
                }
                if (testResult != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = testResult!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (testResult?.startsWith("✓") == true)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    store.saveAll(apiUrl.trim(), apiKey.trim(), modelId.trim())
                    onDismiss()
                }
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}

private fun testConnection(baseUrl: String, apiKey: String): String {
    return try {
        val url = URL("$baseUrl/models")
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            val code = connection.responseCode
            if (code == HttpURLConnection.HTTP_OK) {
                "✓ Connection successful"
            } else if (code == 401 || code == 403) {
                "Authentication failed. Check your API key."
            } else {
                "Server responded with code $code"
            }
        } finally {
            connection.disconnect()
        }
    } catch (e: Exception) {
        "Connection failed: ${e.message}"
    }
}

@Composable
fun SettingsIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Settings,
            contentDescription = stringResource(R.string.settings_desc),
        )
    }
}
