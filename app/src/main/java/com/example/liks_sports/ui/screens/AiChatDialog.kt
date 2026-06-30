package com.example.liks_sports.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.liks_sports.BuildConfig
import com.example.liks_sports.R
import com.example.liks_sports.data.AiEditParser
import com.example.liks_sports.data.ChatMessage
import com.example.liks_sports.data.LocalLlmEngine
import com.example.liks_sports.data.NetworkUtil
import com.example.liks_sports.data.Routine
import com.example.liks_sports.data.SettingsStore
import com.example.liks_sports.ui.icons.Close
import com.example.liks_sports.ui.icons.Send
import com.example.liks_sports.ui.icons.Settings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun AiChatDialog(
    routine: Routine,
    settings: SettingsStore,
    localEngine: LocalLlmEngine,
    chatHistory: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onApplyEdits: (Routine) -> Unit,
    onAiResponded: (String) -> Unit,
    onSendFailed: () -> Unit,
    onClearSession: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    var input by remember { mutableStateOf("") }
    var messages by remember(chatHistory) { mutableStateOf(chatHistory) }
    var streamingContent by remember { mutableStateOf<String?>(null) }
    var streamingReasoning by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var streamingJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val notConfiguredMsg = stringResource(R.string.ai_not_configured)
    val noModelMsg = stringResource(R.string.ai_no_model)
    val notDownloadedMsg = stringResource(R.string.ai_not_downloaded)
    val localLoadingMsg = stringResource(R.string.ai_local_loading)
    val noNetworkMsg = stringResource(R.string.ai_network_error, "No internet connection")

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    LaunchedEffect(streamingContent) {
        if (streamingContent != null && messages.isNotEmpty()) {
            listState.scrollToItem(messages.size)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.65f)
                .imePadding(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.ai_assistant_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                    if (streamingJob != null) {
                        TextButton(onClick = { streamingJob?.cancel() }) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    } else {
                        TextButton(
                            onClick = {
                                messages = emptyList()
                                streamingContent = null
                                streamingReasoning = ""
                                errorText = null
                                onClearSession()
                            },
                            enabled = messages.isNotEmpty(),
                        ) {
                            Text(
                                text = stringResource(R.string.clear_action),
                                color = if (messages.isNotEmpty())
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                            )
                        }
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Settings,
                            contentDescription = stringResource(R.string.settings_desc),
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Close,
                            contentDescription = stringResource(R.string.close_desc),
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(4.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.padding(4.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = listState,
                ) {
                    if (messages.isEmpty() && streamingContent == null) {
                        item {
                            Text(
                                text = stringResource(R.string.ai_system_context),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                        }
                    }
                    itemsIndexed(messages) { _, msg ->
                        ChatBubble(message = msg)
                    }
                    if (streamingContent != null) {
                        item {
                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                if (streamingReasoning.isNotEmpty()) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = stringResource(R.string.ai_thinking),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                            Spacer(modifier = Modifier.padding(2.dp))
                                            Text(
                                                text = streamingReasoning,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.padding(4.dp))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (streamingReasoning.isEmpty())
                                            stringResource(R.string.ai_thinking_ellipsis)
                                        else
                                            stringResource(R.string.ai_generating),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    } else if (errorText != null) {
                        item {
                            Text(
                                text = errorText!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(vertical = 4.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        placeholder = { Text(stringResource(R.string.ai_placeholder)) },
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier.weight(1f),
                        enabled = streamingContent == null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            val msg = input.trim()
                            if (msg.isEmpty() || streamingContent != null) return@IconButton
                            if (!settings.isAiReady()) {
                                errorText = when {
                                    !settings.useCloudModel -> notDownloadedMsg
                                    settings.modelId.isBlank() -> noModelMsg
                                    else -> notConfiguredMsg
                                }
                                return@IconButton
                            }
                            if (settings.useCloudModel && !NetworkUtil.isAvailable(context)) {
                                errorText = noNetworkMsg
                                return@IconButton
                            }
                            input = ""
                            errorText = null
                            val userMsg = ChatMessage("user", msg)
                            messages = messages + userMsg
                            onSendMessage(msg)
                            streamingContent = ""
                            val fullMessages = messages

                            streamingJob = scope.launch {
                                try {
                                    val fullResponse = if (settings.useCloudModel) {
                                        streamFromCloud(settings, routine, fullMessages,
                                            onToken = { token ->
                                                streamingContent = (streamingContent ?: "") + token
                                            },
                                            onReasoningToken = { token ->
                                                streamingReasoning = streamingReasoning + token
                                            },
                                        )
                                    } else {
                                        streamFromLocal(settings, localEngine, routine, fullMessages.dropLast(1),
                                            userText = msg,
                                            onToken = { token ->
                                                streamingContent = (streamingContent ?: "") + token
                                            },
                                            onLoading = { streamingReasoning = localLoadingMsg },
                                        )
                                    }
                                    val cleanText = AiEditParser.extractMessageText(fullResponse)
                                    val displayText = cleanText ?: fullResponse
                                    val edited = AiEditParser.applyAiEdits(routine, fullResponse)
                                    if (edited != null) {
                                        onApplyEdits(edited)
                                    }
                                    onAiResponded(displayText)
                                    messages = messages + ChatMessage("assistant", displayText)
                                } catch (e: CancellationException) {
                                    throw e
                                } catch (e: Exception) {
                                    errorText = e.message ?: "Unknown error"
                                    onSendFailed()
                                } finally {
                                    streamingContent = null
                                    streamingReasoning = ""
                                    streamingJob = null
                                }
                            }
                        },
                        enabled = input.isNotBlank() && streamingContent == null,
                    ) {
                        Icon(
                            imageVector = Send,
                            contentDescription = stringResource(R.string.ai_send),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
) {
    val isUser = message.role == "user"
    Surface(
        color = if (isUser) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isUser) stringResource(R.string.ai_you) else stringResource(R.string.ai_ai),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

private class AiStreamException(message: String) : Exception(message)

/** Signals a non-200 HTTP response from the cloud endpoint. Used internally
 *  to decide whether to retry without JSON mode (some servers 400 on the
 *  response_format field). */
private class AiStreamHttpException(val code: Int, message: String) : Exception(message)

private suspend fun streamFromLocal(
    settings: SettingsStore,
    engine: LocalLlmEngine,
    routine: Routine,
    messages: List<ChatMessage>,
    userText: String,
    onToken: (String) -> Unit,
    onLoading: () -> Unit,
): String {
    if (!engine.isReady()) {
        onLoading()
        val ok = engine.initialize(settings)
        if (!ok) throw AiStreamException("Failed to load on-device model")
    }
    val systemPrompt = AiEditParser.buildSystemPrompt(routine)
    return engine.stream(settings, systemPrompt, messages, userText, onToken)
}

private suspend fun streamFromCloud(
    settings: SettingsStore,
    routine: Routine,
    messages: List<ChatMessage>,
    onToken: (String) -> Unit,
    onReasoningToken: (String) -> Unit,
): String = withContext(Dispatchers.IO) {
    val baseUrl = settings.apiUrl.trimEnd('/')
    // Try with JSON mode first (request a JSON object so the model is less
    // likely to emit prose around the actions block). A few OpenAI-compatible
    // servers reject the response_format field with a 400; on that specific
    // error we retry once without JSON mode so those endpoints still work.
    try {
        doCloudStream(baseUrl, settings, routine, messages, useJsonMode = true, onToken, onReasoningToken)
    } catch (e: AiStreamHttpException) {
        if (e.code == 400) {
            doCloudStream(baseUrl, settings, routine, messages, useJsonMode = false, onToken, onReasoningToken)
        } else {
            throw e
        }
    }
}

private suspend fun doCloudStream(
    baseUrl: String,
    settings: SettingsStore,
    routine: Routine,
    messages: List<ChatMessage>,
    useJsonMode: Boolean,
    onToken: (String) -> Unit,
    onReasoningToken: (String) -> Unit,
): String = withContext(Dispatchers.IO) {
    val url = URL("$baseUrl/chat/completions")
    val connection = url.openConnection() as HttpURLConnection
    try {
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "text/event-stream")
        connection.setRequestProperty("Authorization", "Bearer ${settings.apiKey}")
        connection.instanceFollowRedirects = false
        connection.doOutput = true
        connection.connectTimeout = 30_000
        connection.readTimeout = 120_000

        val systemPrompt = AiEditParser.buildSystemPrompt(routine)
        val requestMessages = JSONArray()
        requestMessages.put(JSONObject().apply {
            put("role", "system")
            put("content", systemPrompt)
        })
        for (msg in messages) {
            requestMessages.put(JSONObject().apply {
                put("role", msg.role)
                put("content", msg.content)
            })
        }

        val body = JSONObject().apply {
            put("model", settings.modelId)
            put("messages", requestMessages)
            put("temperature", 0.7)
            put("stream", true)
            if (useJsonMode) {
                put("response_format", JSONObject().apply {
                    put("type", "json_object")
                })
            }
        }

        connection.outputStream.use { os ->
            os.write(body.toString().toByteArray(Charsets.UTF_8))
        }

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            val errorStream = connection.errorStream
            val errorBody = errorStream?.bufferedReader(Charsets.UTF_8)?.readText() ?: ""
            errorStream?.close()
            if (responseCode == 400) {
                // Bubble up as a retryable signal; streamFromCloud may retry
                // without JSON mode before surfacing the error.
                throw AiStreamHttpException(400, "Bad request: $errorBody")
            }
            val msg = when (responseCode) {
                401 -> "Authentication failed. Check your API key."
                429 -> "Rate limited. Wait and try again."
                500 -> "Server error. The API may be unavailable."
                else -> "Unexpected response ($responseCode)"
            }
            throw AiStreamException(msg)
        }

        var sawReasoning = false
        BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8)).use { reader ->
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val l = line ?: continue
                // Match "data:" flexibly so servers that emit "data:{...}"
                // (no space) are not silently dropped — this was causing the
                // chat to show no response ("nothing happens").
                if (l.startsWith("data:")) {
                    val data = l.removePrefix("data:").trim()
                    if (data == "[DONE]") {
                        break
                    }
                    if (data.isEmpty()) continue
                    try {
                        val chunk = JSONObject(data)
                        val choices = chunk.optJSONArray("choices")
                        if (choices != null && choices.length() > 0) {
                            val choice = choices.getJSONObject(0)
                            val delta = choice.optJSONObject("delta")

                            val raw = delta?.opt("content")
                            val content = if (raw is String) raw else ""
                            if (content.isNotEmpty()) {
                                sb.append(content)
                                withContext(Dispatchers.Main) { onToken(content) }
                            }

                            val rawReasoning = delta?.opt("reasoning_content")
                            val reasoning = if (rawReasoning is String) rawReasoning else ""
                            if (reasoning.isNotEmpty()) {
                                sawReasoning = true
                                withContext(Dispatchers.Main) { onReasoningToken(reasoning) }
                            }
                        }
                    } catch (e: Exception) {
                        if (BuildConfig.DEBUG) Log.w("AiChatDialog", "SSE parse error", e)
                    }
                }
            }
            val result = sb.toString()
            // A 200 with no content and no reasoning means the endpoint did not
            // stream a usable response (e.g. ignored stream:true). Surface a
            // clear error instead of an empty assistant bubble ("nothing happens").
            if (result.isEmpty() && !sawReasoning) {
                throw AiStreamException(
                    "No content received. The endpoint may not support streaming, " +
                        "the model ID may be wrong, or the server ignored the stream request.",
                )
            }
            result
        }
    } finally {
        connection.disconnect()
    }
}
