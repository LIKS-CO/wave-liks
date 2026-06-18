package com.example.liks_sports.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SettingsStore(context: Context) {

    enum class ThemePalette { MATERIAL_YOU, KOVA, LIKS }

    enum class Language { SYSTEM, ENGLISH, SPANISH }

    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val securePrefs by lazy {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            appContext,
            PREFS_NAME_SECURE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    /** When false (default) the app runs AI on-device via LiteRT-LM Gemma 4 E2B. */
    var useCloudModel: Boolean
        get() = prefs.getBoolean(KEY_USE_CLOUD_MODEL, false)
        set(value) { prefs.edit().putBoolean(KEY_USE_CLOUD_MODEL, value).apply() }

    var apiUrl: String
        get() = prefs.getString(KEY_API_URL, "") ?: ""
        set(value) { prefs.edit().putString(KEY_API_URL, value).apply() }

    var apiKey: String
        get() = securePrefs.getString(KEY_API_KEY, "") ?: ""
        set(value) { securePrefs.edit().putString(KEY_API_KEY, value).apply() }

    /** Cloud model id (used only when useCloudModel == true). */
    var modelId: String
        get() = prefs.getString(KEY_MODEL_ID, "") ?: ""
        set(value) { prefs.edit().putString(KEY_MODEL_ID, value).apply() }

    /** On-device model id; defaults to Gemma 4 E2B-it. */
    var localModelId: String
        get() = prefs.getString(KEY_LOCAL_MODEL_ID, DEFAULT_LOCAL_MODEL_ID) ?: DEFAULT_LOCAL_MODEL_ID
        set(value) { prefs.edit().putString(KEY_LOCAL_MODEL_ID, value).apply() }

    /** On-device backend: "GPU" (default) or "CPU". */
    var localBackend: String
        get() = prefs.getString(KEY_LOCAL_BACKEND, DEFAULT_LOCAL_BACKEND) ?: DEFAULT_LOCAL_BACKEND
        set(value) { prefs.edit().putString(KEY_LOCAL_BACKEND, value).apply() }

    /** Active color palette; defaults to Material You. */
    var themePalette: ThemePalette
        get() {
            val name = prefs.getString(KEY_THEME_PALETTE, ThemePalette.MATERIAL_YOU.name) ?: ThemePalette.MATERIAL_YOU.name
            return runCatching { ThemePalette.valueOf(name) }.getOrDefault(ThemePalette.MATERIAL_YOU)
        }
        set(value) { prefs.edit().putString(KEY_THEME_PALETTE, value.name).apply() }

    /** Preferred app language; defaults to System. */
    var language: Language
        get() {
            val name = prefs.getString(KEY_LANGUAGE, Language.SYSTEM.name) ?: Language.SYSTEM.name
            return runCatching { Language.valueOf(name) }.getOrDefault(Language.SYSTEM)
        }
        set(value) { prefs.edit().putString(KEY_LANGUAGE, value.name).apply() }

    /** Absolute path to the downloaded .litertlm file, or "" if not yet downloaded. */
    var localModelPath: String
        get() = prefs.getString(KEY_LOCAL_MODEL_PATH, "") ?: ""
        set(value) { prefs.edit().putString(KEY_LOCAL_MODEL_PATH, value).apply() }

    /** True when the cloud endpoint is fully configured (url + key + modelId). */
    val isCloudConfigured: Boolean
        get() = apiUrl.isNotBlank() && apiKey.isNotBlank() && modelId.isNotBlank()

    /** True when the on-device model file is present. */
    fun isLocalReady(): Boolean = localModelPath.isNotBlank() &&
        java.io.File(localModelPath).exists()

    /** Mode-aware readiness check. */
    fun isAiReady(): Boolean =
        if (useCloudModel) isCloudConfigured else isLocalReady()

    /** @deprecated use [isCloudConfigured]; kept for source compatibility. */
    val isConfigured: Boolean
        get() = isCloudConfigured

    fun saveCloud(apiUrl: String, apiKey: String, modelId: String) {
        this.apiUrl = apiUrl
        this.apiKey = apiKey
        this.modelId = modelId
    }

    fun saveLocal(backend: String) {
        this.localBackend = backend
    }

    companion object {
        private const val PREFS_NAME = "liks_sports_prefs"
        private const val PREFS_NAME_SECURE = "liks_sports_secure"
        private const val KEY_USE_CLOUD_MODEL = "use_cloud_model"
        private const val KEY_API_URL = "api_url"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_MODEL_ID = "model_id"
        private const val KEY_LOCAL_MODEL_ID = "local_model_id"
        private const val KEY_LOCAL_BACKEND = "local_backend"
        private const val KEY_LOCAL_MODEL_PATH = "local_model_path"
        private const val KEY_THEME_PALETTE = "theme_palette"
        private const val KEY_LANGUAGE = "language"

        const val DEFAULT_LOCAL_MODEL_ID = "gemma-4-e2b-it"
        const val DEFAULT_LOCAL_MODEL_FILE = "gemma-4-E2B-it.litertlm"
        const val DEFAULT_LOCAL_MODEL_URL =
            "https://huggingface.co/litert-community/gemma-4-E2B-it-litert-lm/resolve/main/gemma-4-E2B-it.litertlm"
        const val DEFAULT_LOCAL_BACKEND = "GPU"
        const val BACKEND_GPU = "GPU"
        const val BACKEND_CPU = "CPU"
    }
}
