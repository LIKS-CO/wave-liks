package com.example.liks_sports.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val securePrefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME_SECURE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    var apiUrl: String
        get() = prefs.getString(KEY_API_URL, "") ?: ""
        set(value) { prefs.edit().putString(KEY_API_URL, value).apply() }

    var apiKey: String
        get() = securePrefs.getString(KEY_API_KEY, "") ?: ""
        set(value) { securePrefs.edit().putString(KEY_API_KEY, value).apply() }

    var modelId: String
        get() = prefs.getString(KEY_MODEL_ID, "") ?: ""
        set(value) { prefs.edit().putString(KEY_MODEL_ID, value).apply() }

    val isConfigured: Boolean
        get() = apiUrl.isNotBlank() && apiKey.isNotBlank() && modelId.isNotBlank()

    fun saveAll(apiUrl: String, apiKey: String, modelId: String) {
        this.apiUrl = apiUrl
        this.apiKey = apiKey
        this.modelId = modelId
    }

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val PREFS_NAME_SECURE = "liks_sports_secure"
        private const val KEY_API_URL = "api_url"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_MODEL_ID = "model_id"
    }
}
