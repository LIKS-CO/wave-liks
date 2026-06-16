package com.example.liks_sports.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class ChatMessage(val role: String, val content: String)

class ChatHistoryStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getMessages(routineId: String): List<ChatMessage> {
        val json = prefs.getString(keyFor(routineId), null) ?: return emptyList()
        val arr = JSONArray(json)
        return (0 until arr.length()).map { i ->
            val obj = arr.getJSONObject(i)
            ChatMessage(obj.getString("role"), obj.getString("content"))
        }
    }

    fun addMessage(routineId: String, message: ChatMessage) {
        val messages = getMessages(routineId).toMutableList()
        messages.add(message)
        val arr = JSONArray()
        for (msg in messages) {
            arr.put(JSONObject().apply {
                put("role", msg.role)
                put("content", msg.content)
            })
        }
        prefs.edit().putString(keyFor(routineId), arr.toString()).apply()
    }

    fun clear(routineId: String) {
        prefs.edit().remove(keyFor(routineId)).apply()
    }

    private fun keyFor(routineId: String) = "chat_$routineId"

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
    }
}
