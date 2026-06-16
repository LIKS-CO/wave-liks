package com.example.liks_sports.data

import android.content.Context

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var apiUrl: String
        get() = prefs.getString(KEY_API_URL, "") ?: ""
        set(value) { prefs.edit().putString(KEY_API_URL, value).apply() }

    var apiKey: String
        get() = prefs.getString(KEY_API_KEY, "") ?: ""
        set(value) { prefs.edit().putString(KEY_API_KEY, value).apply() }

    var modelId: String
        get() = prefs.getString(KEY_MODEL_ID, "") ?: ""
        set(value) { prefs.edit().putString(KEY_MODEL_ID, value).apply() }

    val isConfigured: Boolean
        get() = apiUrl.isNotBlank() && apiKey.isNotBlank() && modelId.isNotBlank()

    fun saveAll(apiUrl: String, apiKey: String, modelId: String) {
        prefs.edit()
            .putString(KEY_API_URL, apiUrl)
            .putString(KEY_API_KEY, apiKey)
            .putString(KEY_MODEL_ID, modelId)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val KEY_API_URL = "api_url"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_MODEL_ID = "model_id"
    }
}
