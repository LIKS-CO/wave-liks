package com.example.liks_sports.data

import android.content.Context
import android.util.Log
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Wraps the LiteRT-LM [Engine] running the on-device Gemma-4-E2B-it model.
 *
 * The engine is lazily created and kept alive across calls so repeated prompts
 * don't pay the ~10s model-load cost. Call [close] when the model path or
 * backend changes, or on app teardown.
 */
class LocalLlmEngine(private val context: Context) {

    @Volatile private var engine: Engine? = null
    @Volatile private var loadedPath: String? = null
    @Volatile private var loadedBackend: String? = null
    @Volatile private var initializing: Boolean = false

    /** True once [initialize] has completed successfully for the current config. */
    fun isReady(): Boolean = engine != null && loadedPath != null

    /** True if [initialize] is currently running. */
    fun isInitializing(): Boolean = initializing

    /**
     * Loads the model if not already loaded for [settings.localModelPath] /
     * [settings.localBackend]. Safe to call repeatedly. Returns true on success
     * or if already ready. Must be called off the UI thread.
     */
    suspend fun initialize(settings: SettingsStore): Boolean = withContext(Dispatchers.IO) {
        val path = settings.localModelPath
        val backend = settings.localBackend
        if (path.isBlank() || !java.io.File(path).exists()) {
            return@withContext false
        }
        if (engine != null && path == loadedPath && backend == loadedBackend) {
            return@withContext true
        }
        closeInternal()
        initializing = true
        try {
            val backendObj = when (backend) {
                SettingsStore.BACKEND_CPU -> Backend.CPU()
                else -> Backend.GPU()
            }
            val config = EngineConfig(
                modelPath = path,
                backend = backendObj,
                cacheDir = context.cacheDir.path,
            )
            val eng = Engine(config)
            eng.initialize()
            engine = eng
            loadedPath = path
            loadedBackend = backend
            true
        } catch (e: Exception) {
            Log.e(TAG, "GPU init failed, falling back to CPU", e)
            try {
                val config = EngineConfig(
                    modelPath = path,
                    backend = Backend.CPU(),
                    cacheDir = context.cacheDir.path,
                )
                val eng = Engine(config)
                eng.initialize()
                engine = eng
                loadedPath = path
                loadedBackend = SettingsStore.BACKEND_CPU
                true
            } catch (e2: Exception) {
                Log.e(TAG, "CPU init also failed", e2)
                engine = null
                loadedPath = null
                loadedBackend = null
                false
            }
        } finally {
            initializing = false
        }
    }

    /**
     * Streams a response for [userText] given a [systemPrompt] and prior
     * [messages]. Emits partial text deltas via [onToken] on the Main dispatcher
     * and returns the full concatenated text on completion.
     */
    suspend fun stream(
        settings: SettingsStore,
        systemPrompt: String,
        messages: List<ChatMessage>,
        userText: String,
        onToken: (String) -> Unit,
    ): String = withContext(Dispatchers.IO) {
        val eng = engine ?: throw IllegalStateException("LocalLlmEngine not initialized")
        val conversation = buildConversation(eng, systemPrompt, messages)
        try {
            val sb = StringBuilder()
            val flow: Flow<Message> = conversation.sendMessageAsync(userText)
            flow.collect { msg ->
                val text = msg.toString()
                if (text.isNotEmpty()) {
                    sb.append(text)
                    withContext(Dispatchers.Main) { onToken(text) }
                }
            }
            sb.toString()
        } finally {
            runCatching { conversation.close() }
        }
    }

    private fun buildConversation(
        eng: Engine,
        systemPrompt: String,
        messages: List<ChatMessage>,
    ): Conversation {
        val initial = messages.mapNotNull { msg ->
            when (msg.role) {
                "user" -> Message.user(msg.content)
                "assistant" -> Message.model(msg.content)
                "system" -> null
                else -> null
            }
        }
        val config = ConversationConfig(
            systemInstruction = Contents.of(systemPrompt),
            initialMessages = initial,
        )
        return eng.createConversation(config)
    }

    fun close() {
        runCatching { closeInternal() }
    }

    private fun closeInternal() {
        engine?.let { runCatching { it.close() } }
        engine = null
        loadedPath = null
        loadedBackend = null
    }

    companion object {
        private const val TAG = "LocalLlmEngine"
    }
}
