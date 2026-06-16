package com.example.liks_sports.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.liks_sports.R
import com.example.liks_sports.data.ChatMessage
import com.example.liks_sports.data.Routine
import com.example.liks_sports.data.SettingsStore
import com.example.liks_sports.ui.icons.Send
import kotlinx.coroutines.Dispatchers
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
    chatHistory: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onApplyEdits: (Routine) -> Unit,
    onAiResponded: (String) -> Unit,
    onClearSession: () -> Unit,
    onDismiss: () -> Unit,
) {
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(chatHistory) }
    var streamingContent by remember { mutableStateOf<String?>(null) }
    var streamingReasoning by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, streamingContent) {
        listState.animateScrollToItem(Int.MAX_VALUE)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.65f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.ai_assistant_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
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
                                                text = "Thinking",
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
                                        text = if (streamingReasoning.isEmpty()) "Thinking…" else "Generating…",
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
                            input = ""
                            errorText = null
                            val userMsg = ChatMessage("user", msg)
                            messages = messages + userMsg
                            onSendMessage(msg)
                            streamingContent = ""
                            val fullMessages = messages

                            scope.launch {
                                try {
                                    streamFromAi(settings, routine, fullMessages,
                                        onToken = { token ->
                                            streamingContent = (streamingContent ?: "") + token
                                        },
                                        onReasoningToken = { token ->
                                            streamingReasoning = streamingReasoning + token
                                        },
                                    )
                                    val fullResponse = streamingContent ?: ""
                                    val cleanText = extractMessageText(fullResponse)
                                    val displayText = cleanText ?: fullResponse
                                    streamingContent = displayText
                                    val edited = applyAiEdits(routine, fullResponse)
                                    if (edited != null) {
                                        onApplyEdits(edited)
                                    }
                                    onAiResponded(displayText)
                                    messages = messages + ChatMessage("assistant", displayText)
                                    streamingContent = null
                                    streamingReasoning = ""
                                } catch (e: Exception) {
                                    errorText = e.message ?: "Unknown error"
                                    streamingContent = null
                                    streamingReasoning = ""
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
                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = {
                            messages = emptyList()
                            streamingContent = null
                            streamingReasoning = ""
                            errorText = null
                            onClearSession()
                        },
                        enabled = messages.isNotEmpty() && streamingContent == null,
                    ) {
                        Text(
                            text = "Clear",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    isStreaming: Boolean = false,
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
                    text = if (isUser) "You" else "AI",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                )
                if (isStreaming) {
                    Spacer(modifier = Modifier.width(6.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 1.5.dp,
                    )
                }
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

private suspend fun streamFromAi(
    settings: SettingsStore,
    routine: Routine,
    messages: List<ChatMessage>,
    onToken: (String) -> Unit,
    onReasoningToken: (String) -> Unit,
) = withContext(Dispatchers.IO) {
    val baseUrl = settings.apiUrl.trimEnd('/')
    val url = URL("$baseUrl/chat/completions")
    val connection = url.openConnection() as HttpURLConnection
    try {
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "Bearer ${settings.apiKey}")
        connection.doOutput = true
        connection.connectTimeout = 30_000
        connection.readTimeout = 120_000

        val systemPrompt = buildSystemPrompt(routine)
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
        }

        connection.outputStream.use { os ->
            os.write(body.toString().toByteArray(Charsets.UTF_8))
        }

        val reader = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val l = line ?: continue
            if (l.startsWith("data: ")) {
                val data = l.removePrefix("data: ").trim()
                if (data == "[DONE]") break
                try {
                    val chunk = JSONObject(data)
                    val choices = chunk.optJSONArray("choices")
                    if (choices != null && choices.length() > 0) {
                        val delta = choices.getJSONObject(0).optJSONObject("delta")

                        val raw = delta?.opt("content")
                        val content = if (raw is String) raw else ""
                        if (content.isNotEmpty()) {
                            withContext(Dispatchers.Main) { onToken(content) }
                        }

                        val rawReasoning = delta?.opt("reasoning_content")
                        val reasoning = if (rawReasoning is String) rawReasoning else ""
                        if (reasoning.isNotEmpty()) {
                            withContext(Dispatchers.Main) { onReasoningToken(reasoning) }
                        }
                    }
                } catch (_: Exception) {}
            }
        }
    } finally {
        connection.disconnect()
    }
}

private fun extractMessageText(fullResponse: String): String? {
    val jsonMatch = Regex("```json\\s*([\\s\\S]*?)```").find(fullResponse)
    val jsonStr = jsonMatch?.groupValues?.get(1) ?: return null
    return try {
        val response = JSONObject(jsonStr)
        val actions = response.optJSONArray("actions") ?: return null
        for (i in 0 until actions.length()) {
            val action = actions.getJSONObject(i)
            if (action.getString("type") == "message") {
                return action.getString("text")
            }
        }
        null
    } catch (_: Exception) {
        null
    }
}

private fun buildSystemPrompt(routine: Routine): String {
    val exercisesJson = JSONArray()
    for (ex in routine.exercises) {
        exercisesJson.put(JSONObject().apply {
            put("id", ex.id)
            put("name", ex.name)
            put("reps", ex.reps)
            put("exerciseDurationSeconds", ex.exerciseDurationSeconds)
            put("restDurationSeconds", ex.restDurationSeconds)
            put("overrideDefaults", ex.overrideDefaults)
        })
    }
    return """You are an AI assistant that helps edit workout routines. The user will ask you to modify a routine.

Current routine name: "${routine.name}"
Current exercises:
$exercisesJson

When the user asks for changes, respond with a JSON block wrapped in ```json fences containing an "actions" array. Available action types:

- {"type": "add_exercise", "name": "Exercise Name", "exerciseDurationSeconds": 30, "restDurationSeconds": 10, "reps": 1}
- {"type": "remove_exercise", "exerciseId": "the-id"}
- {"type": "rename_exercise", "exerciseId": "the-id", "name": "New Name"}
- {"type": "modify_exercise", "exerciseId": "the-id", "changes": {"exerciseDurationSeconds": 45, "restDurationSeconds": 15, "reps": 3, "overrideDefaults": true}}
- {"type": "rename_routine", "name": "New Routine Name"}
- {"type": "message", "text": "Some chat message explaining what you did"}

Include a "message" action explaining what you did. Example response:

```json
{
  "actions": [
    {"type": "add_exercise", "name": "Plank", "exerciseDurationSeconds": 60, "restDurationSeconds": 25, "reps": 1},
    {"type": "message", "text": "I added a 60-second Plank exercise."}
  ]
}
```

Only respond with the JSON actions block — do not include any other text outside the JSON block."""
}

private fun applyAiEdits(routine: Routine, aiResponse: String): Routine? {
    val jsonMatch = Regex("```json\\s*([\\s\\S]*?)```").find(aiResponse)
    val jsonStr = jsonMatch?.groupValues?.get(1) ?: return null
    val response = JSONObject(jsonStr)
    val actions = response.optJSONArray("actions") ?: return null

    var updated = routine
    for (i in 0 until actions.length()) {
        val action = actions.getJSONObject(i)
        when (action.getString("type")) {
            "add_exercise" -> {
                val ex = com.example.liks_sports.data.Exercise(
                    name = action.getString("name"),
                    exerciseDurationSeconds = action.optInt("exerciseDurationSeconds", 30),
                    restDurationSeconds = action.optInt("restDurationSeconds", 10),
                    reps = action.optInt("reps", 1),
                )
                updated = updated.copy(exercises = updated.exercises + ex)
            }
            "remove_exercise" -> {
                val id = action.getString("exerciseId")
                updated = updated.copy(exercises = updated.exercises.filter { it.id != id })
            }
            "rename_exercise" -> {
                val id = action.getString("exerciseId")
                val name = action.getString("name")
                updated = updated.copy(exercises = updated.exercises.map {
                    if (it.id == id) it.copy(name = name) else it
                })
            }
            "modify_exercise" -> {
                val id = action.getString("exerciseId")
                val changes = action.getJSONObject("changes")
                updated = updated.copy(exercises = updated.exercises.map { ex ->
                    if (ex.id == id) {
                        ex.copy(
                            name = changes.optString("name", ex.name),
                            exerciseDurationSeconds = changes.optInt("exerciseDurationSeconds", ex.exerciseDurationSeconds),
                            restDurationSeconds = changes.optInt("restDurationSeconds", ex.restDurationSeconds),
                            reps = changes.optInt("reps", ex.reps),
                            overrideDefaults = changes.optBoolean("overrideDefaults", ex.overrideDefaults),
                        )
                    } else ex
                })
            }
            "rename_routine" -> {
                updated = updated.copy(name = action.getString("name"))
            }
        }
    }
    return if (updated != routine) updated else null
}
