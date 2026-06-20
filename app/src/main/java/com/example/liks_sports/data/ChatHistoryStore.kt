package com.example.liks_sports.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class ChatMessage(val role: String, val content: String)

class ChatHistoryStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val lock = Any()

    fun getMessages(routineId: String): List<ChatMessage> = synchronized(lock) {
        val json = prefs.getString(keyFor(routineId), null) ?: return emptyList()
        try {
            val arr = JSONArray(json)
            (0 until arr.length()).mapNotNull { i ->
                try {
                    val obj = arr.getJSONObject(i)
                    ChatMessage(obj.getString("role"), obj.getString("content"))
                } catch (_: Exception) {
                    null
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun addMessage(routineId: String, message: ChatMessage) {
        synchronized(lock) {
            val messages = getMessages(routineId).toMutableList()
            messages.add(message)
            writeMessages(routineId, trim(messages))
        }
    }

    fun addMessages(routineId: String, newMessages: List<ChatMessage>) {
        if (newMessages.isEmpty()) return
        synchronized(lock) {
            val messages = getMessages(routineId).toMutableList()
            messages.addAll(newMessages)
            writeMessages(routineId, trim(messages))
        }
    }

    fun clear(routineId: String) {
        prefs.edit().remove(keyFor(routineId)).apply()
    }

    fun removeLastUserMessage(routineId: String) {
        synchronized(lock) {
            val messages = getMessages(routineId).toMutableList()
            if (messages.isNotEmpty() && messages.last().role == "user") {
                messages.removeAt(messages.lastIndex)
                writeMessages(routineId, messages)
            }
        }
    }

    private fun writeMessages(routineId: String, messages: List<ChatMessage>) {
        val arr = JSONArray()
        for (msg in messages) {
            arr.put(JSONObject().apply {
                put("role", msg.role)
                put("content", msg.content)
            })
        }
        prefs.edit().putString(keyFor(routineId), arr.toString()).apply()
    }

    private fun trim(messages: List<ChatMessage>): List<ChatMessage> =
        if (messages.size > MAX_MESSAGES) messages.takeLast(MAX_MESSAGES) else messages

    private fun keyFor(routineId: String) = "chat_$routineId"

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val MAX_MESSAGES = 50
    }
}
